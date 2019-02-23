import klass.Klass;

public class Main {
    public static void main(String[] args) {
        JavaParser parser = new JavaParser();
        try {
            Klass klass = parser.readKlass("/home/alejandroperalta/workspace/my-repos/plant-uml-generator/src/main/java/klass/Argument.java");
            System.out.println(klass.getName());
            System.out.println(klass.getParent());
            klass.getAttributes().forEach(attr -> System.out.println(attr.getName()));
            klass.getMethods().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
