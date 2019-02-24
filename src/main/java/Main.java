import klass.Klass;

public class Main {
    public static void main(String[] args) throws Exception {
        String foo = "public class Foo {\n" +
                "   private int bar;\n" +
                "   public int getBar() {\n" +
                "       return bar;\n" +
                "   }\n" +
                "   public void doSomething(String something) {\n" +
                "       System.out.println(\"Hello World !\");\n" +
                "   }\n" +
                "}";

        KlassReader reader = new KlassReader();
        Klass klass = reader.readKlass(foo);

        System.out.println(klass.getName());
        System.out.println(klass.getParent());
        System.out.println(klass.getClassType());

        System.out.println("\nATTRIBUTES\n");

        klass.getAttributes().forEach(at -> {
            System.out.println(at.getName());
            System.out.println(at.getKlass());
        });

        System.out.println("\nMETHODS\n");

        klass.getMethods().forEach(m -> {
            System.out.println(m.getReturnType());
            System.out.println(m.getName());
        });
    }
}
