package parsing;

import klass.builders.KlassBuilder;
import klass.classtype.ConcreteKlass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestKlassHeader {
    String foo = "public class Foo";
    String fooExtendsBar = "public class Foo extends Bar";
    String baz = "public interface Baz";
    String fooImplementsBaz = "public class Foo implements Baz";
    String fooImplementsBazBiz = "public class Foo implements Baz, Biz";

    KlassBuilder builder;

    @Before
    public void initialize() {
        builder = new KlassBuilder();
    }

    @Test
    public void fooShouldBeAConcreteClass() {
        builder.addClassDefinition(foo);
        assertTrue(builder.getClassType() instanceof ConcreteKlass);
    }

    @Test
    public void foosNameShouldBeFoo() {
        builder.addClassDefinition(foo);
        assertEquals("Foo", builder.getName());
    }

    @Test
    public void foosSuperClassShouldBeNull() {
        builder.addClassDefinition(foo);
        assertEquals(null, builder.getSuperClass());
    }

    @Test
    public void fooShouldHaveNoInterfaces() {
        builder.addClassDefinition(foo);
        assertTrue(builder.getInterfaces().isEmpty());
    }

    @Test
    public void fooExtendsBarShouldHaveBarAsSuper() {
        builder.addClassDefinition(fooExtendsBar);
        assertEquals("Bar", builder.getSuperClass());
    }

    @Test
    public void fooImplementsBazShouldHaveBazAsOnlyInterface() {
        builder.addClassDefinition(fooImplementsBaz);
        assertTrue(builder.getInterfaces().contains("Baz"));
        assertEquals(1, builder.getInterfaces().size());
    }

    @Test
    public void fooImplementsBazBizShouldHaveTwoInterfaces() {
        builder.addClassDefinition(fooImplementsBazBiz);
        assertTrue(builder.getInterfaces().contains("Baz") && builder.getInterfaces().contains("Biz"));
        assertEquals(2, builder.getInterfaces().size());
    }
}