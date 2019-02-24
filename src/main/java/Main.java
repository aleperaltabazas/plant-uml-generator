public class Main {
    public static void main(String[] args) {
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
        reader.readKlass(foo);
    }
}
