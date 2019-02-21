package parsing;

import klass.Argument;
import klass.MethodBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        builder.addMethodDefinition(foo);
        assertEquals("void", builder.getReturnType());
    }

    @Test
    public void fooShouldHaveFooAsName() {
        builder.addMethodDefinition(foo);
        assertEquals("foo", builder.getName());
    }

    @Test
    public void fooShouldHaveNoArguments() {
        builder.addMethodDefinition(foo);
        assertTrue(builder.getArguments().isEmpty());
    }

    @Test
    public void barShouldHaveTwoArguments() {
        builder.addMethodDefinition(bar);
        List<Argument> args = builder.getArguments();
        assertEquals(2, args.size());
        assertEquals("int", args.get(0).getKlass());
        assertEquals("arg0", args.get(0).getName());
    }

    @Test
    public void bazShouldHaveGenerics() {
        builder.addMethodDefinition(baz);
        List<Argument> args = builder.getArguments();
        assertEquals(1, args.size());
        assertEquals("Map<String, Integer>", args.get(0).getKlass());
        assertFalse(builder.isVisible());
    }

    @Test
    public void bizShouldHaveGenerics() {
        builder.addMethodDefinition(biz);
        List<Argument> args = builder.getArguments();
        assertEquals(1, args.size());
        assertEquals("Map<String, Map<Integer, String>>", args.get(0).getKlass());
        assertEquals("org.apache.Foo", builder.getReturnType());
        assertFalse(builder.isVisible());
    }

    @Test
    public void varShouldNotBeVisible() {
        builder.addMethodDefinition(var);
        assertFalse(builder.isVisible());
    }
}