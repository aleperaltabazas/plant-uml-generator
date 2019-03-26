package parsing;

import klass.Klass;
import klass.KlassBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class KlassReaderTest {
    KlassReader reader = new KlassReader();
    KlassBuilder b1, b2, b3, b4, b5;
    List<KlassBuilder> builders;

    @Before
    public void initialize() {
        b1 = new KlassBuilder();
        b2 = new KlassBuilder();
        b3 = new KlassBuilder();
        b4 = new KlassBuilder();
        b5 = new KlassBuilder();

        b1.setName("A").setParent("C");
        b2.setName("B").setParent("C");
        b3.setName("C");
        b4.setName("D").setParent("A");
        b5.setName("E");

        builders = Arrays.asList(b1, b2, b3, b4, b5);
    }

    @Test
    public void saraza() {
        List<Klass> klasses = reader.createKlasses(builders);
        System.out.println("saraza");
    }
}