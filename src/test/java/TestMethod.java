import klass.Argument;
import klass.MethodBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestMethod {
    String foo = "public void foo();";
    String bar = "public void bar(int arg0, int arg1);";
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
}
