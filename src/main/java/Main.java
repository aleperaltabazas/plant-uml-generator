import klass.Klass;

public class Main {
    public static void main(String[] args) throws Exception {
        FileManager manager = new FileManager();
        String argument = manager.findAllClasses("/home/alejandroperalta/workspace/my-repos/plant-uml-generator/src/main/java/klass/Argument.java").get(0);
        KlassReader reader = new KlassReader();
        Klass klass = reader.readKlass(argument);
        System.out.println(klass.getName());
        klass.getMethods().forEach(m -> {
            System.out.println(m.getName() + ": " + m.getReturnType());
        });
        klass.getAttributes().forEach(a -> {
            System.out.println(a.getName() + ": " + a.getKlass());
        });
    }
}
