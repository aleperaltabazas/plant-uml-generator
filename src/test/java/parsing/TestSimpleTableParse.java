package parsing;

import klass.Attribute;
import klass.Klass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import persistence.attributes.ForeignKey;
import persistence.attributes.ForeignKeyFactory;
import persistence.tables.SimpleTable;
import persistence.tables.builders.SimpleTableBuilder;
import utils.AttributeFactory;
import utils.KlassFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestSimpleTableParse {
    Klass klass;
    private Attribute oneToOne;
    private Attribute oneToMany;
    private Attribute manyToOne;

    @Before
    public void initialize() {
        Attribute pk = AttributeFactory.pk("id", "Long");
        oneToOne = AttributeFactory.oneToOne("classType", "ClassType");
        oneToMany = AttributeFactory.oneToMany("attributes", "Attribute");
        manyToOne = AttributeFactory.manyToOne("parent", "Klass");
        Attribute some = AttributeFactory.simple("some", "int");
        Attribute other = AttributeFactory.simple("other", "float");
        klass = KlassFactory.withAttributes("Entity", Arrays.asList(pk, oneToOne, oneToMany, manyToOne, some, other));
    }

    @Test
    public void tableOfKlassWithoutParsingForeignKeys() {
        SimpleTableBuilder tb = new SimpleTableBuilder();
        tb.parse(klass);
        SimpleTable simpleTable = tb.build();

        assertEquals("id (PK)", simpleTable.getPk());
        assertEquals(2, simpleTable.getAttributes().size());
        assertEquals(0, simpleTable.getFks().size());
    }

    @Test
    public void tableOfKlassWhileParsingForeignKeys() {
        List<ForeignKey> foreignKeys = ForeignKeyFactory.foreignKeysOf(klass);

        SimpleTableBuilder tb = new SimpleTableBuilder();
        tb.parse(klass);
        tb.takeForeignKeys(foreignKeys);
        SimpleTable simpleTable = tb.build();

        assertEquals(2, simpleTable.getFks().size());
        assertTrue(simpleTable.getFks().contains(ForeignKey.of(klass.getName(), oneToOne)));
        assertTrue(simpleTable.getFks().contains(ForeignKey.of(klass.getName(), manyToOne)));
    }

    @Test
    @Ignore
    // This test was done when the method takeForeignKeys() had a different approach, thus, it's no longer that useful
    // and should be changed to something according to the changes
    public void takingForeignKeysShouldNotReduceListSize() {
        List<ForeignKey> foreignKeys = ForeignKeyFactory.foreignKeysOf(klass);
        int initialSize = foreignKeys.size();

        SimpleTableBuilder tb = new SimpleTableBuilder();
        tb.parse(klass);
        tb.takeForeignKeys(foreignKeys);

        SimpleTable simpleTable = tb.build();

        int finalSize = foreignKeys.size();

        assertEquals(3, initialSize);
        assertEquals(1, finalSize);
        assertTrue(simpleTable.getFks().contains(ForeignKey.of(klass.getName(), oneToOne)));
        assertFalse(simpleTable.getFks().contains(ForeignKey.of(klass.getName(), oneToMany)));
    }
}