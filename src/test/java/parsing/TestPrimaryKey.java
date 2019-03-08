package parsing;

import exceptions.MultiplePrimaryKeyError;
import exceptions.NoPrimaryKeyError;
import org.junit.Before;
import org.junit.Test;
import persistence.Table;
import persistence.TableBuilder;
import utils.AttributeFactory;
import utils.KlassFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestPrimaryKey {
    private TableBuilder tb;

    @Before
    public void initialize() {
        tb = new TableBuilder();
    }

    @Test
    public void simpleTable() {
        tb.parse(KlassFactory.withAttributes("EntityKlass", Arrays.asList(AttributeFactory.pk("id", "Long"))));
        Table table = tb.build();
        assertEquals("id (PK)", table.getPk());
        assertEquals("entity_klass", table.getName());
    }

    @Test
    public void withNoPrimaryKey() {
        assertThrows(NoPrimaryKeyError.class, () -> tb.parse(KlassFactory.emptyClass("Class")));
    }

    @Test
    public void withTwoPrimaryKeys() {
        assertThrows(MultiplePrimaryKeyError.class,
                () -> tb.parse(KlassFactory.withAttributes("Class", Arrays.asList(AttributeFactory.pk("id", "Long"),
                        AttributeFactory.pk("pk", "Integer")))));
    }
}