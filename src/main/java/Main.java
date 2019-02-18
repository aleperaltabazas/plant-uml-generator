import some.Reader;
import some.klass.KlassFactory;

public class Main {
    public static void main(String[] args) {
        String klass = "import some.Reader;\nimport some.TestClass;\nimport some.klass.KlassFactorypublic class Foo extends Bar implements Baz, Biz {   private int foo;}";

        Reader reader = new Reader();
        //reader.read(TestClass.class);
        System.out.println(new KlassFactory().klassDefinition(klass));
    }
}
