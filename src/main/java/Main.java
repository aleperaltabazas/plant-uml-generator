import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ClassFinder finder = new ClassFinder();
        Reader reader = new Reader();
        Set<Class<?>> classes = new ClassFinder().findClasses(reader.getClass().getPackage());
    }
}
