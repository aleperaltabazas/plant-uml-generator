import klass.Klass;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.UMLMaker;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        FileManager manager = new FileManager();
        KlassReader reader = new KlassReader();

        String text = manager.fileToText("/home/alejandroperalta/workspace/my-repos/plant-uml-generator/src/main/java/klass/Klass.java");
        Klass klass = reader.readKlass(text);

        UMLMaker maker = new UMLMaker();
        List<String> lines = maker.makeClassUml(klass);
        Path file = Paths.get("/home/alejandroperalta/workspace/my-repos/" + klass.getName() + ".uml");
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
}
