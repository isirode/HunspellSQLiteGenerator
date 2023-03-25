import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerbeIdentifier {// TODO : test this class

    public enum Groupe {
        AvoirOuEtre(0),
        PremierGroupe(1),
        SecondGroupe(2),
        TroisiemeGroupe(3);

        Groupe(int value) {
            this.value = value;
        }

        private int value;

        public int getValue() {
            return value;
        }
    }

    public Groupe groupe;
    public boolean avoir = false;
    public boolean etre = false;
    public boolean intransitif = false;
    public boolean transitifDirect = false;
    public boolean transitifIndirect = false;
    public boolean verbePronominal = false;
    public boolean verbeImpersonnel = false;
    public boolean avecVerbeAuxiliaireEtre = false;
    public boolean avecVerbeAuxiliaireAvoir = false;

    public VerbeIdentifier(String identifier) {

        String[] parts = identifier.split("");
        if (parts.length != 10) {
            throw new RuntimeException("verbe identifier has wrong size " + identifier + " : " + parts.length);
        }

        final String grp = parts[1];
        switch(grp) {
            case "0" -> {
                this.groupe = Groupe.AvoirOuEtre;
            }
            case "1" -> {
                this.groupe = Groupe.PremierGroupe;
            }
            case "2" -> {
                this.groupe = Groupe.SecondGroupe;
            }
            case "3" -> {
                this.groupe = Groupe.TroisiemeGroupe;
            }
            default -> {
                throw new RuntimeException("unknown groupe : " + grp);
            }
        }

        final String avoirEtre = parts[2];
        switch(avoirEtre) {
            case "a" -> {
                this.avoir = true;
            }
            case "e" -> {
                this.etre = true;
            }
            case "_" -> {
                // pass
            }
            default -> {
                throw new RuntimeException("unknown third place : " + avoirEtre);
            }
        }

        // x : pourrait être les cas intransitif ET transitif ; voir direct ET indirect (aboyer je crois par exemple)
        // intransitif ?
        final String trn = parts[3];
        switch(trn) {
            case "x":
            case "i":
                this.intransitif = true;
                break;
            case "_":
                // pass
                break;
            default:
                throw new RuntimeException("unknown 4th place : " + trn);
        }

        // transitif direct ?
        final String trnDirect = parts[4];
        switch(trnDirect) {
            case "x":
            case "t":
                this.transitifDirect = true;
                break;
            case "_":
                // pass
                break;
            default:
                throw new RuntimeException("unknown 5th place : " + trnDirect);
        }

        // transitif indirect ?
        final String trnIndirect = parts[5];
        switch(trnIndirect) {
            case "x":
            case "n":
                this.transitifIndirect = true;
                break;
            case "_":
                // pass
                break;
            default:
                throw new RuntimeException("unknown 6th place : " + trnIndirect);
        }

        // verbe pronominal ?
        final String verbePronominal = parts[6];
        switch(verbePronominal) {// FIXME : je ne suis pas sur de ce que 'p' signifie
            case "p":
            case "q":
            case "r":
            case "e":
            case "x":
                this.verbePronominal = true;
                break;
            case "_":
                // pass
                break;
            default:
                throw new RuntimeException("unknown 7th place : " + verbePronominal);
        }

        // verbe impersonnel ?
        final String verbeImpersonnel = parts[7];
        switch(verbeImpersonnel) {
            case "m":
            case "x":
                this.verbeImpersonnel = true;
                break;
            case "_":
                // pass
                break;
            default:
                throw new RuntimeException("unknown 8th place : " + verbePronominal);
        }

        // avecVerbeAuxiliaireEtre ?
        final String avecVerbeAuxiliaireEtre = parts[8];
        switch(avecVerbeAuxiliaireEtre) {// FIXME : pas sur que ca fonctionne comme cela, ne sais pas ce que "x" et "z" signifie
            case "e":
            case "z":
            case "x":
                this.avecVerbeAuxiliaireEtre = true;
                break;
            case "_":
                // pass
                break;
            default:
                throw new RuntimeException("unknown 9th place : " + avecVerbeAuxiliaireEtre);
        }

        // avecVerbeAuxiliaireAvoir ?
        final String avecVerbeAuxiliaireAvoir = parts[9];
        switch(avecVerbeAuxiliaireAvoir) {// FIXME : pas sur que ca fonctionne comme cela, ne sais pas ce que "x" et "z" signifie
            case "a":
            case "z":
            case "x":
                this.avecVerbeAuxiliaireAvoir = true;
                break;
            case "_":
                // pass
                break;
            default:
                throw new RuntimeException("unknown 10th place : " + avecVerbeAuxiliaireAvoir);
        }
    }

}
