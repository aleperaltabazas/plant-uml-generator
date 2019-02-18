import some.Baz;
import some.more.Foo;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ClassFinder finder = new ClassFinder();
        ClassReader reader = new ClassReader();

        Set<Class<?>> classes = finder.findClasses(Baz.class.getPackage());
        FinalButNotReallyString uml = new FinalButNotReallyString();
        classes.forEach(c -> uml.concat(reader.read(c)));

        System.out.println(uml.getText());
    }
}
