import klass.ClassType;
import klass.KlassBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestKlassHeader {
    String foo = "public class Foo";
    String fooExtendsBar = "public class Foo extends Bar";
    String baz = "public interface Baz";

    KlassBuilder builder;

    @Before
    public void initialize() {
        builder = new KlassBuilder();
    }

    @Test
    public void testClassFoo() {
        builder.addClassDefinition(foo);
        Assert.assertEquals(ClassType.Concrete, builder.getClassType());
        Assert.assertEquals("Foo", builder.getName());
    }

    @Test
    public void testClassBaz() {
        builder.addClassDefinition(baz);
        Assert.assertEquals(ClassType.Interface, builder.getClassType());
        Assert.assertEquals("Baz", builder.getName());
    }
}