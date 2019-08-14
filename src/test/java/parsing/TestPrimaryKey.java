package parsing;

import exceptions.MultiplePrimaryKeyError;
import exceptions.NoPrimaryKeyError;
import io.vavr.collection.List;
import org.junit.Before;
import org.junit.Test;
import persistence.tables.RegularTable;
import persistence.tables.builders.SimpleTableBuilder;
import utils.AttributeFactory;
import utils.KlassFactory;

import static org.junit.Assert.*;

public class TestPrimaryKey {
    private SimpleTableBuilder tb;

    @Before
    public void initialize() {
        tb = new SimpleTableBuilder();
    }

    @Test
    public void simpleTable() {
        tb.parse(KlassFactory.withAttributes("EntityKlass", List.of(AttributeFactory.pk("id", "Long"))),
                List.empty());
        RegularTable regularTable = tb.build();
        assertEquals("id (PK)", regularTable.getPk());
        assertEquals("entity_klass", regularTable.getName());
    }

    @Test
    public void withNoPrimaryKey() {
        assertThrows(NoPrimaryKeyError.class, () -> tb.parse(KlassFactory.emptyClass("Class"), List.empty()));
    }

    @Test
    public void withTwoPrimaryKeys() {
        assertThrows(MultiplePrimaryKeyError.class,
                () -> tb.parse(KlassFactory.withAttributes("Class", List.of(AttributeFactory.pk("id", "Long"),
                        AttributeFactory.pk("pk", "Integer"))), List.empty()));
    }
}