import klass.Klass;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.UMLMaker;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        FileManager manager = new FileManager();
        KlassReader reader = new KlassReader();
        UMLMaker maker = new UMLMaker();

        try {
            String path = args[0];
            File file = new File(path);

            String fileName;

            try {
                fileName = args[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                fileName = "classes.uml";
            }

            if (file.isDirectory()) {
                List<String> klassesText = manager.findAllClasses(file.getAbsolutePath());
                List<Klass> klasses = reader.parseClasses(klassesText);

                StringBuilder sb = new StringBuilder();
                sb.append("@startuml");

                klasses.forEach(klass -> maker.makeClassUml(klass).forEach(line -> sb.append(line + "\n")));

                sb.append("@enduml");
            } else {
                String text = manager.fileToText(file.getAbsolutePath());
                Klass klass = reader.readKlass(text);
                List<String> umlLines = maker.makeClassUml(klass);

                manager.writeFile(fileName, umlLines);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Include either the directory from which to find the classes or a single java file to parse");
        }


    }
}