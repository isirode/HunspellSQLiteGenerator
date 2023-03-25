import fr.fanaen.wordlist.model.Word;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class MyCustomWord {

    public final String word;
    public final String normalized_word;
    public final String affixes;
    public final String identifiers;

    // Noms
    public boolean nom = false;
    public boolean adjectif = false;
    // Noms propres
    public boolean prenom = false;
    public boolean patronyme = false;
    public boolean nomPropre = false;
    public boolean titre = false;
    // Inflexions
    public boolean singulier = false;
    public boolean pluriel = false;
    public boolean invariable = false;
    public boolean masculin = false;
    public boolean feminin = false;
    public boolean epicene = false;
    // Verbes
    public boolean Verbe = false;
    // Verbes : configuration
    public int groupe;
    public boolean avoir = false;
    public boolean etre = false;
    public boolean intransitif = false;
    public boolean transitifDirect = false;
    public boolean transitifIndirect = false;
    public boolean verbePronominal = false;
    public boolean verbeImpersonnel = false;
    public boolean avecVerbeAuxiliaireEtre = false;
    public boolean avecVerbeAuxiliaireAvoir = false;
    // Verbes : inflexions
    public boolean Infinitif = false;
    public boolean ParticipePresent = false;
    public boolean ParticipePasse = false;
    public boolean IndicatifPresent = false;
    public boolean PasseSimple = false;
    public boolean Imparfait = false;
    public boolean Futur = false;
    public boolean PresentConditionnel = false;
    public boolean SubjonctifPresent = false;
    public boolean ImparfaitDuSubjonctif = false;
    public boolean Imperatif = false;
    public boolean PremierePersonneSingulier = false;
    public boolean SecondePersonneSingulier = false;
    public boolean TroisiemePersonneSingulier = false;
    public boolean PremierePersonnePluriel = false;
    public boolean SecondePersonnePluriel = false;
    public boolean TroisiemePersonnePluriel = false;
    public boolean PremierePersonneSingulierForme1 = false;
    public boolean PremierePersonneSingulierForme2 = false;
    // Divers
    public boolean Divers = false;
    public boolean Erreur = false;
    public boolean Adverbe = false;
    public boolean AdverbeDeNegation = false;
    public boolean AdverbeInterrogatif = false;
    // Divers : Locutions
    public boolean LocutionAdverbiale = false;
    public boolean LocutionAdjectivale = false;
    public boolean LocutionVerbale = false;
    public boolean LocutionNominale = false;
    public boolean LocutionPatronymique = false;
    public boolean LocutionInterjective = false;
    public boolean LocutionPrepositive = false;
    public boolean LocutionPrepositiveVerbale = false;
    public boolean LocutionConjonctive = false;
    public boolean LocutionConjonctiveDeSubordination = false;
    public boolean Interjection = false;
    public boolean MotGrammatical = false;
    // Divers : Determinants
    public boolean Determinant = false;
    public boolean DeterminantDemonstratif = false;
    public boolean DeterminantExclamatif = false;
    public boolean DeterminantIndefini = false;
    public boolean DeterminantNegatif = false;
    public boolean DeterminantPossessif = false;
    public boolean Preposition = false;
    public boolean PrepositionVerbale = false;
    public boolean Nombre = false;
    public boolean NombreLatin = false;
    public boolean Conjonction = false;
    public boolean ConjonctionDeCoordination = false;
    public boolean ConjonctionDeSubordination = false;
    public boolean PrefixeVerbal = false;
    // Divers : Pronoms
    public boolean Pronom = false;
    // Divers : personnes des pronoms
    public boolean PremierePersonne = false;
    public boolean SecondePersonne = false;
    public boolean TroisiemePersonne = false;
    public boolean PronomAdverbial = false;
    public boolean PronomDemonstratif = false;
    public boolean PronomIndefini = false;
    public boolean PronomIndefiniNegatif = false;
    public boolean PronomInterrogatif = false;
    public boolean PronomPersonnelComplementDObjet = false;
    public boolean PronomPersonnelSujet = false;
    public boolean PronomRelatif = false;
    public boolean Prefixe = false;
    public boolean Suffixe = false;

    public MyCustomWord(Word word, List<IdentifierExtracter.Identifier> identifierList) {
        this.word = word.getContent();
        // TODO : use a better way ?
        this.normalized_word = StringUtils.stripAccents(word.getContent()).toLowerCase(Locale.FRANCE);
        this.affixes = word.getAffixes();
        this.identifiers = word.getIdentifiers();

        // TODO : nom et nom propre : doit être capable de bien identifier quoi est quoi
        for (var identifier : identifierList) {
            switch (identifier) {
                case Nom -> {
                    this.nom = true;
                }
                case Adjectif -> {
                    this.adjectif = true;
                }
                case Prenom -> {
                    this.prenom = true;
                }
                case Patronyme -> {
                    this.patronyme = true;
                }
                case NomPropre -> {
                    this.nomPropre = true;
                }
                case Titre -> {
                    this.titre = true;
                }
                case Singulier -> {
                    this.singulier = true;
                }
                case Pluriel -> {
                    this.pluriel = true;
                }
                case Invariable -> {
                    this.invariable = true;
                }
                case Masculin -> {
                    this.masculin = true;
                }
                case Feminin -> {
                    this.feminin = true;
                }
                case Epicene -> {
                    this.epicene = true;
                }
                case Verbe -> {
                    this.Verbe = true;
                    if (identifier.getVerbe() == null) {
                        throw new RuntimeException("verbe should not be null");
                    }
                    VerbeIdentifier verbe = identifier.getVerbe();
                    this.groupe = verbe.getGroupe().getValue();
                    this.avoir = verbe.avoir;
                    this.avoir = verbe.etre;
                    this.avoir = verbe.intransitif;
                    this.avoir = verbe.transitifDirect;
                    this.avoir = verbe.transitifIndirect;
                    this.avoir = verbe.verbePronominal;
                    this.avoir = verbe.verbeImpersonnel;
                    this.avoir = verbe.avecVerbeAuxiliaireEtre;
                    this.avoir = verbe.avecVerbeAuxiliaireAvoir;
                }
                case Infinitif -> {
                    this.Infinitif = true;
                }
                case ParticipePresent -> {
                    this.ParticipePresent = true;
                }
                case ParticipePasse -> {
                    this.ParticipePasse = true;
                }
                case IndicatifPresent -> {
                    this.IndicatifPresent = true;
                }
                case PasseSimple -> {
                    this.PasseSimple = true;
                }
                case Imparfait -> {
                    this.Imparfait = true;
                }
                case Futur -> {
                    this.Futur = true;
                }
                case PresentConditionnel -> {
                    this.PresentConditionnel = true;
                }
                case SubjonctifPresent -> {
                    this.SubjonctifPresent = true;
                }
                case ImparfaitDuSubjonctif -> {
                    this.ImparfaitDuSubjonctif = true;
                }
                case Imperatif -> {
                    this.Imperatif = true;
                }
                case PremierePersonneSingulier -> {
                    this.PremierePersonneSingulier = true;
                }
                case SecondePersonneSingulier -> {
                    this.SecondePersonneSingulier = true;
                }
                case TroisiemePersonneSingulier -> {
                    this.TroisiemePersonneSingulier = true;
                }
                case PremierePersonnePluriel -> {
                    this.PremierePersonnePluriel = true;
                }
                case SecondePersonnePluriel -> {
                    this.SecondePersonnePluriel = true;
                }
                case TroisiemePersonnePluriel -> {
                    this.TroisiemePersonnePluriel = true;
                }
                case PremierePersonneSingulierForme1 -> {
                    this.PremierePersonneSingulierForme1 = true;
                }
                case PremierePersonneSingulierForme2 -> {
                    this.PremierePersonneSingulierForme2 = true;
                }
                case Divers -> {
                    this.Divers = true;
                }
                case Erreur -> {
                    this.Erreur = true;
                }
                case Adverbe -> {
                    this.Adverbe = true;
                }
                case AdverbeDeNegation -> {
                    this.AdverbeDeNegation = true;
                }
                case AdverbeInterrogatif -> {
                    this.AdverbeInterrogatif = true;
                }
                case LocutionAdverbiale -> {
                    this.LocutionAdverbiale = true;
                }
                case LocutionAdjectivale -> {
                    this.LocutionAdjectivale = true;
                }
                case LocutionVerbale -> {
                    this.LocutionVerbale = true;
                }
                case LocutionNominale -> {
                    this.LocutionNominale = true;
                }
                case LocutionPatronymique -> {
                    this.LocutionPatronymique = true;
                }
                case LocutionInterjective -> {
                    this.LocutionInterjective = true;
                }
                case LocutionPrepositive -> {
                    this.LocutionPrepositive = true;
                }
                case LocutionPrepositiveVerbale -> {
                    this.LocutionPrepositiveVerbale = true;
                }
                case LocutionConjonctive -> {
                    this.LocutionConjonctive = true;
                }
                case LocutionConjonctiveDeSubordination -> {
                    this.LocutionConjonctiveDeSubordination = true;
                }
                case Interjection -> {
                    this.Interjection = true;
                }
                case MotGrammatical -> {
                    this.MotGrammatical = true;
                }
                case Determinant -> {
                    this.Determinant = true;
                }
                case DeterminantDemonstratif -> {
                    this.DeterminantDemonstratif = true;
                }
                case DeterminantExclamatif -> {
                    this.DeterminantExclamatif = true;
                }
                case DeterminantIndefini -> {
                    this.DeterminantIndefini = true;
                }
                case DeterminantNegatif -> {
                    this.DeterminantNegatif = true;
                }
                case DeterminantPossessif -> {
                    this.DeterminantPossessif = true;
                }
                case Preposition -> {
                    this.Preposition = true;
                }
                case PrepositionVerbale -> {
                    this.PrepositionVerbale = true;
                }
                case Nombre -> {
                    this.Nombre = true;
                }
                case NombreLatin -> {
                    this.NombreLatin = true;
                }
                case Conjonction -> {
                    this.Conjonction = true;
                }
                case ConjonctionDeCoordination -> {
                    this.ConjonctionDeCoordination = true;
                }
                case ConjonctionDeSubordination -> {
                    this.ConjonctionDeSubordination = true;
                }
                case PrefixeVerbal -> {
                    this.PrefixeVerbal = true;
                }
                case Pronom -> {
                    this.Pronom = true;
                }
                case PremierePersonne -> {
                    this.PremierePersonne = true;
                }
                case SecondePersonne -> {
                    this.SecondePersonne = true;
                }
                case TroisiemePersonne -> {
                    this.TroisiemePersonne = true;
                }
                case PronomAdverbial -> {
                    this.PronomAdverbial = true;
                }
                case PronomDemonstratif -> {
                    this.PronomDemonstratif = true;
                }
                case PronomIndefini -> {
                    this.PronomIndefini = true;
                }
                case PronomIndefiniNegatif -> {
                    this.PronomIndefiniNegatif = true;
                }
                case PronomInterrogatif -> {
                    this.PronomInterrogatif = true;
                }
                case PronomPersonnelComplementDObjet -> {
                    this.PronomPersonnelComplementDObjet = true;
                }
                case PronomPersonnelSujet -> {
                    this.PronomPersonnelSujet = true;
                }
                case PronomRelatif -> {
                    this.PronomRelatif = true;
                }
                case Prefixe -> {
                    this.Prefixe = true;
                }
                case Suffixe -> {
                    this.Suffixe = true;
                }
                default -> {
                    throw new RuntimeException("Unmanaged enum value : " + identifier.toString());
                }
            }
        }
    }


}
