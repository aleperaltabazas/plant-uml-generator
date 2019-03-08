package parsing;

import klass.Attribute;
import klass.Klass;
import org.junit.Before;
import org.junit.Test;
import persistence.ForeignKey;
import persistence.ForeignKeyFactory;
import persistence.Table;
import persistence.TableBuilder;
import utils.AttributeFactory;
import utils.KlassFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestTableParse {
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
        TableBuilder tb = new TableBuilder();
        tb.parse(klass);
        Table table = tb.build();

        assertEquals("id (PK)", table.getPk());
        assertEquals(2, table.getAttributes().size());
        assertEquals(0, table.getFks().size());
    }

    @Test
    public void tableOfKlassWhileParsingForeignKeys() {
        List<ForeignKey> foreignKeys = ForeignKeyFactory.foreignKeysOf(klass);
        TableBuilder tb = new TableBuilder();
        tb.parse(klass);
        tb.takeForeignKeys(foreignKeys);
        Table table = tb.build();

        assertEquals(2, table.getFks().size());
        assertTrue(table.getFks().contains(ForeignKey.of(klass.getName(), oneToOne)));
        assertTrue(table.getFks().contains(ForeignKey.of(klass.getName(), manyToOne)));
    }
}