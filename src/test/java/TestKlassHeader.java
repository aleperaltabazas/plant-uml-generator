import klass.ClassType;
import klass.KlassBuilder;
import klass.Objekt;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestKlassHeader {
    String foo = "public class Foo";
    String fooExtendsBar = "public class Foo extends Bar";
    String baz = "public interface Baz";
    String fooImplementsBaz = "public class Foo implements Baz";

    KlassBuilder builder;

    @Before
    public void initialize() {
        builder = new KlassBuilder();
    }

    @After
    public void clear() {
        builder.clear();
    }

    @Test
    public void fooShouldBeAConcreteClass() {
        builder.addClassDefinition(foo);
        assertEquals(ClassType.Concrete, builder.getClassType());
    }

    @Test
    public void foosNameShouldBeFoo() {
        builder.addClassDefinition(foo);
        assertEquals("Foo", builder.getName());
    }

    @Test
    public void foosSuperClassShouldBeObjekt() {
        builder.addClassDefinition(foo);
        assertEquals(Objekt.getInstance().getName(), builder.getSuperClass());
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
}