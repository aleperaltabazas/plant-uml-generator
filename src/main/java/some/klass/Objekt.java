package some.klass;

import java.util.Arrays;
import java.util.List;

public class Objekt extends Klass {
    private static Objekt instance = new Objekt();

    private Objekt() {
        super(Arrays.asList(), Arrays.asList(), "Object", ClassType.Concrete, null);
    }

    public static Objekt getInstance() {
        return instance;
    }
}
