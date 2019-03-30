package parsing;

import exceptions.NotAForeignKeyException;
import klass.Attribute;
import org.junit.Test;
import persistence.attributes.FKType;
import persistence.attributes.ForeignKey;
import utils.AttributeFactory;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TestForeignKey {
    Attribute attribute;
    ForeignKey fk;

    @Test
    public void oneToOne() {
        attribute = AttributeFactory.oneToOne("foo", "Bar");
        fk = ForeignKey.of("Foo", attribute);

        assertEquals("Foo", fk.getOriginTable());
        assertEquals(FKType.OneToOne, fk.getType());
        assertEquals("Bar", fk.getDestinationTable());
    }

    @Test
    public void oneToMany() {
        attribute = AttributeFactory.oneToMany("foo", "Bar");
        fk = ForeignKey.of("Foo", attribute);

        assertEquals("Bar", fk.getOriginTable());
        assertEquals(FKType.ManyToOne, fk.getType());
        assertEquals("Foo", fk.getDestinationTable());
    }

    @Test
    public void manyToOne() {
        attribute = AttributeFactory.manyToOne("foo", "Bar");
        fk = ForeignKey.of("Foo", attribute);

        assertEquals("Foo", fk.getOriginTable());
        assertEquals(FKType.ManyToOne, fk.getType());
        assertEquals("Bar", fk.getDestinationTable());
    }

    @Test
    public void manyToMany() {
        attribute = AttributeFactory.manyToMany("foo", "Bar");
        fk = ForeignKey.of("Foo", attribute);

        assertEquals("Foo", fk.getOriginTable());
        assertEquals(FKType.ManyToMany, fk.getType());
        assertEquals("Bar", fk.getDestinationTable());
    }

    @Test
    public void noAnnotation() {
        attribute = AttributeFactory.simple("foo", "int");
        assertThrows(NotAForeignKeyException.class, () -> fk = ForeignKey.of("Foo", attribute));
    }

    @Test
    public void equalForeignKeys() {
        ForeignKey fk1 = ForeignKey.of("Foo", AttributeFactory.oneToOne("foo", "int"));
        ForeignKey fk2 = ForeignKey.of("Foo", AttributeFactory.oneToOne("foo", "int"));

        assertEquals(fk1, fk2);
        assertTrue(Arrays.asList(fk1).contains(fk2));
    }
}