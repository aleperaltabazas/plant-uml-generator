package parsing;

import io.vavr.collection.List;
import klass.Argument;
import klass.builders.MethodBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMethod {
    String foo = "public void foo();";
    String bar = "public void bar(int arg0, int arg1);";
    String baz = "private void baz(Map<String, Integer> map) {";
    String biz = "protected org.apache.Foo biz(Map<String, Map<Integer, String>> map);";
    String var = "Integer foo();";
    MethodBuilder builder;

    @Before
    public void initialize() {
        builder = new MethodBuilder();
    }

    @Test
    public void fooShouldHaveVoidReturnType() {
        builder.addDefinition(foo);
        assertEquals("void", builder.getReturnType());
    }

    @Test
    public void fooShouldHaveFooAsName() {
        builder.addDefinition(foo);
        assertEquals("foo", builder.getName());
    }

    @Test
    public void fooShouldHaveNoArguments() {
        builder.addDefinition(foo);
        assertTrue(builder.getArgumentsMap().isEmpty());
    }

    @Test
    public void barShouldHaveTwoArguments() {
        builder.addDefinition(bar);
        List<Argument> args = builder.getArgumentsMap();
        assertEquals(2, args.size());
        assertEquals("int", args.get(0).getKlass());
        assertEquals("arg0", args.get(0).getName());
    }

    @Test
    public void bazShouldHaveGenerics() {
        builder.addDefinition(baz);
        List<Argument> args = builder.getArgumentsMap();
        assertEquals(1, args.size());
        assertEquals("Map<String, Integer>", args.get(0).getKlass());
        assertFalse(builder.isVisible());
    }

    @Test
    public void bizShouldHaveGenerics() {
        builder.addDefinition(biz);
        List<Argument> args = builder.getArgumentsMap();
        assertEquals(1, args.size());
        assertEquals("Map<String, Map<Integer, String>>", args.get(0).getKlass());
        assertEquals("org.apache.Foo", builder.getReturnType());
        assertFalse(builder.isVisible());
    }

    @Test
    public void varShouldNotBeVisible() {
        builder.addDefinition(var);
        assertFalse(builder.isVisible());
    }
}