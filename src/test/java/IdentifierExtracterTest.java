import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IdentifierExtracterTest {

    @Test
    public void ItShouldMapIdentifierToEnumIdentifier() {
        List<IdentifierExtracter.Identifier> identifierList = new ArrayList<IdentifierExtracter.Identifier>(Arrays.asList(IdentifierExtracter.Identifier.values()));

        for (var identifier : identifierList) {
            final String id = identifier.getIdentifier();
            final IdentifierExtracter.Identifier newIdentifier = IdentifierExtracter.Identifier.fromString(id);

            assertEquals(identifier, newIdentifier);
        }

    }

    @Test
    public void ItShouldNotMapIdentifierToUnknownIdentifier() {
        final String unknownIdentifier = "test";
        final IdentifierExtracter.Identifier identifier = IdentifierExtracter.Identifier.fromString(unknownIdentifier);

        assertNull(identifier);
    }

    @Test
    public void ItShouldExtractAllIdentifiers() {
        // init
        final String identifier = "# po:nom is:epi is:inv";
        final List<IdentifierExtracter.Identifier> expectedIdentifiers = List.of(
                IdentifierExtracter.Identifier.Nom,
                IdentifierExtracter.Identifier.Epicene,
                IdentifierExtracter.Identifier.Invariable
        );

        // obtain
        final List<IdentifierExtracter.Identifier> identifiers = IdentifierExtracter.extractIdentifiers(identifier);

        // test
        assertThat(identifiers).hasSize(3).hasSameElementsAs(expectedIdentifiers);
    }

    @Test
    public void ItShouldExtractAllIdentifiers2() {
        // init
        final String identifier = "# po:prn is:fem is:inv";// po:prn is:fem is:inv
        final List<IdentifierExtracter.Identifier> expectedIdentifiers = List.of(
                IdentifierExtracter.Identifier.Prenom,
                IdentifierExtracter.Identifier.Feminin,
                IdentifierExtracter.Identifier.Invariable
        );

        // obtain
        final List<IdentifierExtracter.Identifier> identifiers = IdentifierExtracter.extractIdentifiers(identifier);

        // test
        assertThat(identifiers).hasSize(3).hasSameElementsAs(expectedIdentifiers);
    }

    @Test
    public void ItShouldThrowAnExceptionIfFormatIsIncorect() {
        assertThatThrownBy(() -> {
            // init
            final String identifier = "# po: is:epi is:inv";

            // obtain
            final List<IdentifierExtracter.Identifier> identifiers = IdentifierExtracter.extractIdentifiers(identifier);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("unknown identifier format");

        assertThatThrownBy(() -> {
            // init
            final String identifier = "# po:test is:epi is:inv";

            // obtain
            final List<IdentifierExtracter.Identifier> identifiers = IdentifierExtracter.extractIdentifiers(identifier);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("unknown identifier : test");

        assertThatThrownBy(() -> {
            // init
            final String identifier = "# test:test is:epi is:inv";

            // obtain
            final List<IdentifierExtracter.Identifier> identifiers = IdentifierExtracter.extractIdentifiers(identifier);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("unknown starting part : test:test");
    }

}
