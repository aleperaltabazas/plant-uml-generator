import some.Reader;

import java.util.Arrays;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Reader reader = new Reader();
        //reader.read(TestClass.class);

        Set<Class<?>> classes = new ClassFinder().findClasses(reader.getClass().getPackage());
        classes.forEach(c -> {
            System.out.println("Class name: " + c.getSimpleName());
            System.out.println("Class package: " + c.getPackage());
        });
    }
}
