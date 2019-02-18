import some.Baz;
import some.more.Foo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ClassFinder finder = new ClassFinder();
        ClassReader reader = new ClassReader();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Set<Class<?>> classes = finder.findClasses(Baz.class.getPackage());
        FinalButNotReallyString uml = new FinalButNotReallyString();
        classes.forEach(c -> uml.concat(reader.read(c)));

        try {
            Constructor<Package> packageConstructor = Package.class.getDeclaredConstructor(String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class, ClassLoader.class);
            packageConstructor.setAccessible(true);
            Package instance = packageConstructor.newInstance("some", null, null, null, null, null, null, null, Thread.currentThread().getContextClassLoader());
            System.out.println(instance.getName());
            System.out.println(Baz.class.getPackage().getName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }


        System.out.println(uml.getText());
    }
}
