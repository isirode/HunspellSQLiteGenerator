import fr.fanaen.wordlist.WordListGenerator;
import fr.fanaen.wordlist.WordListGeneratorListener;
import fr.fanaen.wordlist.model.Word;

import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

public class Main {

    // TODO : use a command system
    // TODO : use log4j
    // TODO : build the database as JSON as an argument (maybe), it is too large for any use though
    // TODO : move the DAO parts inside a DAO package
    public static void main(String[] args) throws Exception {

        System.out.println(FileSystems.getDefault().getPath(".").toAbsolutePath());

        // create a database connection
        Connection connection = null;
        // TODO : make it an argument
        // TODO : include the license in the SQLite database somehow
        connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.executeUpdate("drop table if exists words");
        statement.executeUpdate("drop table if exists sequences");

        /*
        content = "-"
        affixes = "#"
        identifiers = "# po:ponc po:sign"
         */

        // TODO : move this somewhere else
        String createWordTable = """
            create table words (
                id INTEGER PRIMARY KEY, 
                
            """;

        var fields = MyCustomWord.class.getDeclaredFields();
        String join = Arrays.stream(fields).map(x -> {
            String type = "";
            if (x.getType().isAssignableFrom(int.class)) {
                type = "INTEGER NOT NULL";
            } else if (x.getType().isAssignableFrom(boolean.class)) {
                type = "INTEGER NOT NULL CHECK (" + x.getName() + " IN (0, 1))";
            } else if (x.getType().isAssignableFrom(String.class)) {
                type = "String ";// NOT NULL
            } else {
                throw new RuntimeException("type of " + x.getName() + " unknown");
            }
            return x.getName() + " " + type;
        }).collect(Collectors.joining(",\n"));
        createWordTable += join + "\n)";

        System.out.println(createWordTable);

        statement.executeUpdate(createWordTable);

        String createSequencesTable = """
            create table sequences (
                id INTEGER PRIMARY KEY, 
                sequence STRING,
                occurences INTEGER
            )
            """;
        statement.executeUpdate(createSequencesTable);

        // TODO : checkout those words, put one or two in comment
        // Les mots peuvent contenir l', m' etc (expressions : m'as-tu-vu)
        // Predicate<String> myPredicate = item -> item.matches(".*\\d+.*");
        List<Predicate<String>> predicates = new ArrayList<Predicate<String>>();
        predicates.add(item -> item.matches(".*\\d+.*"));
        predicates.add(str -> str.contains("-"));
        predicates.add(str -> str.contains("'"));
        // predicates.add(str -> str.length() == 1);

        Predicate<String> removeStrangeWords = item -> predicates.stream().reduce(x->false, Predicate::or).test(item);

        List<Predicate<String>> affixPredicates = new ArrayList<Predicate<String>>();
        // Majuscules
        affixPredicates.add(item ->item.equals("L'"));
        affixPredicates.add(item ->item.equals("D'"));
        affixPredicates.add(item ->item.equals("Q'"));
        affixPredicates.add(item ->item.equals("T'"));
        affixPredicates.add(item ->item.equals("N'"));
        affixPredicates.add(item ->item.equals("M'"));
        affixPredicates.add(item ->item.equals("J'"));
        affixPredicates.add(item ->item.equals("S'"));
        // Minuscules
        affixPredicates.add(item ->item.equals("l'"));
        affixPredicates.add(item ->item.equals("d'"));
        affixPredicates.add(item ->item.equals("q'"));
        affixPredicates.add(item ->item.equals("t'"));
        affixPredicates.add(item ->item.equals("n'"));
        affixPredicates.add(item ->item.equals("m'"));
        affixPredicates.add(item ->item.equals("j'"));
        affixPredicates.add(item ->item.equals("s'"));
        // Autres affixes
        affixPredicates.add(item ->item.equals("Q*"));
        affixPredicates.add(item ->item.equals("Qj"));
        affixPredicates.add(item ->item.equals("Si"));

        Predicate<String> removeBadAffix = item -> affixPredicates.stream().reduce(x->false, Predicate::or).test(item);

        // TODO : vérifier les flexions https://grammalecte.net/entry.php?prj=fr&id=125904

        // Prepared statement
        String preparedQuery = "insert into words values (null, ";
        String insertJoin = Arrays.stream(fields).map(x -> {
            return "?";
        }).collect(Collectors.joining(",\n"));
        preparedQuery += insertJoin;
        preparedQuery += ")";

        System.out.println(preparedQuery);

        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement(preparedQuery);

        // List<MyCustomWord> words = new ArrayList<>();
        Map<String, Integer> sequences = new HashMap<>();

        WordListGenerator generator = new WordListGenerator(removeStrangeWords, removeBadAffix);
        WordListGeneratorListener listener = new WordListGeneratorListener() {
            @Override
            public void onNewWord(Word newWord) {
                System.out.println(newWord.getContent());

                // TODO : parametrize this constraint
                if (newWord.getContent().length() < 4) {
                    return;
                }

                List<IdentifierExtracter.Identifier> identifierList;
                try {
                    identifierList = IdentifierExtracter.extractIdentifiers(newWord.getIdentifiers());
                } catch (RuntimeException e) {
                    throw e;
                }

                MyCustomWord myCustomWord = new MyCustomWord(newWord, identifierList);

                for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
                    Field x = fields[i];
                    if (x.getType().isAssignableFrom(int.class)) {
                        try {
                            ps.setInt(i + 1, x.getInt(myCustomWord));
                        } catch (SQLException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (x.getType().isAssignableFrom(boolean.class)) {
                        try {
                            ps.setInt(i + 1, (x.getBoolean(myCustomWord) ? 1 : 0));
                        } catch (SQLException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (x.getType().isAssignableFrom(String.class)) {
                        try {
                            ps.setString(i + 1, (String) x.get(myCustomWord));
                        } catch (SQLException | IllegalAccessException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("i : " + i);
                            throw new RuntimeException("i : " + i, e);
                        }
                    } else {
                        throw new RuntimeException("type of " + x.getName() + " unknown");
                    }
                }

                try {
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // words.add(myCustomWord);

                StringBuilder seqBuilder = new StringBuilder();
                String seq = "";
                int length = myCustomWord.word.length();
                for (int i = 0; i < length; i++) {
                    seqBuilder.append(myCustomWord.word.charAt(i));
                    if (seqBuilder.length() >= 4) {
                        seq = StringUtils.stripAccents(seqBuilder.toString()).toUpperCase(Locale.FRANCE);
                        seqBuilder.deleteCharAt(0);
                        if (sequences.containsKey(seq)) {
                            Integer count = sequences.get(seq);
                            count += 1;
                            sequences.put(seq, count);
                        } else {
                            sequences.put(seq, 1);
                        }
                    }
                }
            }

            @Override
            public void onGenerationEnd() { }
        };

        // TODO : pass it as a parameter
        generator.setFileName(
            "./src/main/resources/data/fr-reforme1990"
        );

        generator.addListener(listener);
        generator.readFile();

        System.out.println("done, committing query");

        connection.commit();

        System.out.println(sequences.size() + " sequences found");

        try {
            String insertSequencesQuery = "insert into sequences values (null, ?, ?)";
            PreparedStatement insertSequencesStatement= connection.prepareStatement(insertSequencesQuery);
            sequences.forEach((sequence, occurences) -> {
                try {
                    insertSequencesStatement.setString(1, sequence);
                    insertSequencesStatement.setInt(2, occurences);
                    insertSequencesStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Note : it is too big to be saved this way
        // ObjectMapper mapper = new ObjectMapper();
        // mapper.writeValue(Paths.get("words.json").toFile(), words);

        // generator.displayStatistics();
    }

}
