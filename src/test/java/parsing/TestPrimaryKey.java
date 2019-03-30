package parsing;

import exceptions.MultiplePrimaryKeyError;
import exceptions.NoPrimaryKeyError;
import org.junit.Before;
import org.junit.Test;
import persistence.tables.RegularTable;
import persistence.tables.builders.SimpleTableBuilder;
import utils.AttributeFactory;
import utils.KlassFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestPrimaryKey {
    private SimpleTableBuilder tb;

    @Before
    public void initialize() {
        tb = new SimpleTableBuilder();
    }

    @Test
    public void simpleTable() {
        tb.parse(KlassFactory.withAttributes("EntityKlass", Arrays.asList(AttributeFactory.pk("id", "Long"))));
        RegularTable regularTable = tb.build();
        assertEquals("id (PK)", regularTable.getPk());
        assertEquals("entity_klass", regularTable.getName());
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