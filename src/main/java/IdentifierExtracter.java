import java.util.*;
import java.util.stream.Collectors;

public class IdentifierExtracter {

    // Info : you need to use the Web Archive to read those documents
    // Grammalecte lost the former website since I have made the project
    // Nature etc lets put everything here for now
    // From https://grammalecte.net/documentation.php?prj=fr#orga
    // Extra documentation ici https://grammalecte.net/thread.php?prj=fr&t=769
    // TODO : I see that "emprunts" from foreign languages are not here : add them
    // I am not sure wether or not it is still true, I do not find them and no exceptions are thrown
    public enum Identifier {
        // Nature : noms
        Nom("nom"), // 	Nom commun (ou un nom propre s’utilisant avec déterminant : la France, l’Everest, les États-Unis, etc.).
        Adjectif("adj"),
        // Noms propres
        Prenom("prn"),
        Patronyme("patr"),
        NomPropre("npr"), // Indique un nom propre qui ne nécessite pas de déterminant : Berlin, Hawaï, Linux, etc.
        Titre("titr"),// Non documenté sur le site Grammalecte
        // Inflexions
        Singulier("sg"),
        Pluriel("pl"),
        Invariable("inv"),
        Masculin("mas"),
        Feminin("fem"),
        Epicene("epi"), // Masculin ou feminin
        // Verbs
        Verbe("--nothing--"), // Identification : si commence par v + 9 chars (10 total)
        // Verbes : inflexions
        Infinitif("infi"),
        ParticipePresent("ppre"),
        ParticipePasse("ppas"),
        IndicatifPresent("ipre"),
        PasseSimple("ipsi"),
        Imparfait("iimp"),
        Futur("ifut"),
        PresentConditionnel("cond"),
        SubjonctifPresent("spre"),
        ImparfaitDuSubjonctif("simp"),
        Imperatif("impe"),
        PremierePersonneSingulier("1sg"),
        SecondePersonneSingulier("2sg"),
        TroisiemePersonneSingulier("3sg"),
        PremierePersonnePluriel("1pl"),
        SecondePersonnePluriel("2pl"),
        TroisiemePersonnePluriel("3pl"),
        PremierePersonneSingulierForme1("1isg"),// 	1re personne du singulier, forme interrogative en -è (ou en -é si pas de confusion possible avec un participe passé : puissé, dussé, fussé, eussé).
        PremierePersonneSingulierForme2("1jsg"),// 1re personne du singulier, forme interrogative en -é (confusion possible avec un participe passé).
        // Divers
        Divers("div"),// Non documenté sur le site Grammalecte ; symbole "_"
        Erreur("err"),// Non documenté sur le site Grammalecte ; not sure what it means
        Adverbe("adv"),
        AdverbeDeNegation("negadv"),// Non documenté sur le site Grammalecte
        AdverbeInterrogatif("advint"),// Non documenté sur le site Grammalecte
        LocutionAdverbiale("loc.adv"),
        LocutionAdjectivale("loc.adj"),
        LocutionVerbale("loc.verb"),
        LocutionNominale("loc.nom"), // Non documenté sur le site Grammalecte
        LocutionPatronymique("loc.patr"), // Non documenté sur le site Grammalecte
        LocutionInterjective("loc.interj"), // Non documenté sur le site Grammalecte
        LocutionPrepositive("loc.prep"), // Non documenté sur le site Grammalecte
        LocutionPrepositiveVerbale("loc.prepv"), // Non documenté sur le site Grammalecte
        LocutionConjonctive("loc.cj"), // Non documenté sur le site Grammalecte
        LocutionConjonctiveDeSubordination("loc.cjsub"), // Non documenté sur le site Grammalecte
        Interjection("interj"),
        MotGrammatical("mg"),
        Determinant("det"),
        DeterminantDemonstratif("detdem"),// Non documenté sur le site Grammalecte
        DeterminantExclamatif("detex"),// Non documenté sur le site Grammalecte
        DeterminantIndefini("detind"),// Non documenté sur le site Grammalecte
        DeterminantNegatif("detneg"),// Non documenté sur le site Grammalecte
        DeterminantPossessif("detpos"),// Non documenté sur le site Grammalecte
        Preposition("prep"),
        PrepositionVerbale("prepv"),// Non documenté sur le site Grammalecte
        Nombre("nb"),
        NombreLatin("nbro"),// Non documenté sur le site Grammalecte (II, III, IV etc)
        Conjonction("cj"),
        ConjonctionDeCoordination("cjco"),// Non documenté sur le site Grammalecte
        ConjonctionDeSubordination("cjsub"),// Non documenté sur le site Grammalecte
        PrefixeVerbal("preverb"),// Non documenté sur le site Grammalecte (preverbe)
        // Divers : Pronoms
        Pronom("pro"),
        // Divers : personnes des pronoms
        PremierePersonne("1pe"),// Non documenté sur le site Grammalecte
        SecondePersonne("2pe"),// Non documenté sur le site Grammalecte
        TroisiemePersonne("3pe"),// Non documenté sur le site Grammalecte
        PronomAdverbial("proadv"),// Non documenté sur le site Grammalecte
        PronomDemonstratif("prodem"),// Non documenté sur le site Grammalecte
        PronomIndefini("proind"),// Non documenté sur le site Grammalecte
        PronomIndefiniNegatif("proneg"),// Non documenté sur le site Grammalecte
        PronomInterrogatif("proint"),// Non documenté sur le site Grammalecte
        PronomPersonnelComplementDObjet("properobj"),// Non documenté sur le site Grammalecte
        PronomPersonnelSujet("propersuj"),// Non documenté sur le site Grammalecte
        PronomRelatif("prorel"),// Non documenté sur le site Grammalecte
        Prefixe("pfx"),// Non documenté sur le site Grammalecte
        Suffixe("sfx")// Non documenté sur le site Grammalecte
        ;

        private static Map<String, Identifier> map =
                Arrays.stream(
                        Identifier.values())
                        .collect(Collectors.toMap(Identifier::getIdentifier, identifier1 -> identifier1));

        private Identifier(String identifier) {
            this.identifier = identifier;
        }

        public static Identifier fromString(String identifier) {
            return map.get(identifier);
        }

        public String identifier;

        public String getIdentifier() {
            return identifier;
        }

        public VerbeIdentifier verbeIdentifier;

        public VerbeIdentifier getVerbe() {
            return verbeIdentifier;
        }

        public void setVerbe(VerbeIdentifier verbeIdentifier) {
            this.verbeIdentifier = verbeIdentifier;
        }
    }

    // TODO : test this
    public static List<Identifier> extractIdentifiers(final String identifier) {
        List<Identifier> result = new ArrayList<>();

        final String[] parts = identifier.split(" ");

        for (final String part : parts) {
            if (part.equals("#")) continue;
            if (part.startsWith("po:") || part.startsWith("is:")) {
                final String[] subParts = part.split(":");
                if (subParts.length != 2) {
                    throw new RuntimeException("unknown identifier format");// TODO : different exceptions types
                }
                final String id = subParts[1];
                Identifier identifierEnum = Identifier.fromString(id);
                if (identifierEnum == null) {
                    if (id.startsWith("v")) {
                        VerbeIdentifier verbeIdentifier = new VerbeIdentifier(id);
                        identifierEnum = Identifier.Verbe;
                        identifierEnum.setVerbe(verbeIdentifier);
                    } else {
                        throw new RuntimeException("unknown identifier : " + id);// TODO : different exceptions types
                    }
                }
                result.add(identifierEnum);
            } else if (part.startsWith("st:")) {
                // st : indique la racine sans feminin, pluriel etc ; non documenté
                // pass
            } else if (part.startsWith("al:")) {
                // al : Allomorphe ; utilisé 1 seul fois ; non documenté
                // pass
            } else {
                throw new RuntimeException("unknown starting part : " + part);// TODO : what is this ?
            }
        }

        return result;
    }

}
