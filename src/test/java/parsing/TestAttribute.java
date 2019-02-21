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
        builder.addAttributeDefinition(foo);
        assertEquals("foo", builder.getName());
        assertEquals("int", builder.getType());
        assertFalse(builder.isVisible());
    }

    @Test
    public void barShouldBeVisible() {
        builder.addAttributeDefinition(bar);
        assertTrue(builder.isVisible());
    }

    @Test
    public void bazShouldNotBeVisible() {
        builder.addAttributeDefinition(baz);
        assertEquals("baz", builder.getName());
        assertFalse(builder.isVisible());
    }

    @Test
    public void bizPrivateButWithGetterShouldBeVisible() {
        builder.addAttributeDefinition(biz);
        builder.determineVisibility(bizGetter);
        assertTrue(builder.isVisible());
    }
}