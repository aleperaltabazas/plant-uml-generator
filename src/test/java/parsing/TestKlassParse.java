package parsing;

import klass.Klass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestKlassParse {
    Klass klassArgument;

    @Before
    public void initialize() throws Exception {
        FileManager manager = new FileManager();
        String classText = manager.fileToText("/home/alejandroperalta/workspace/my-repos/plant-uml-generator/src/main/java/klass/Argument.java");

        KlassReader reader = new KlassReader();
        klassArgument = reader.readKlass(classText);
    }

    @Test
    public void klassNameShouldBeArgument() {
        assertEquals("Argument", klassArgument.getName());
    }

    @Test
    public void klassSuperShouldBeNone() {
        assertFalse(klassArgument.getParent().isPresent());
    }

    @Test
    public void klassShouldHaveTwoAttributes() {
        assertEquals(2, klassArgument.getAttributes().size());
        assertEquals("klass", klassArgument.getAttributes().get(0).getName());
        assertEquals("name", klassArgument.getAttributes().get(1).getName());
    }

    @Test
    public void klassShouldHaveTwoMethods() {
        assertEquals(2, klassArgument.getMethods().size());
        assertEquals("getKlass", klassArgument.getMethods().get(0).getName());
        assertEquals("getName", klassArgument.getMethods().get(1).getName());
    }

    @Test
    public void klassShouldHaveGetterForName() {
        assertTrue(klassArgument.hasGetterFor("name"));
    }
}
