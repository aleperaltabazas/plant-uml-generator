package parsing;

import klass.AttributeBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAttribute {
    String foo = "private int foo;";
    String bar = "public Integer bar;";
    String baz = "double baz = 2d";
    String biz = "private int biz;";
    String bizGetter = "public int getBiz() {";
    AttributeBuilder builder;

    @Before
    public void initialize() {
        builder = new AttributeBuilder();
    }

    @Test
    public void fooShouldNoteBeVisible() {
        builder.addDefinition(foo);
        assertEquals("foo", builder.getName());
        assertEquals("int", builder.getType());
        assertFalse(builder.isVisible());
    }

    @Test
    public void barShouldBeVisible() {
        builder.addDefinition(bar);
        assertTrue(builder.isVisible());
    }

    @Test
    public void bazShouldNotBeVisible() {
        builder.addDefinition(baz);
        assertEquals("baz", builder.getName());
        assertFalse(builder.isVisible());
    }

}