package main;

import exceptions.BuildError;
import klass.Klass;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.UMLMaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, BuildError {
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
                sb.append("@startuml\n");

                klasses.forEach(klass -> maker.makeClassUml(klass).forEach(line -> sb.append(line).append("\n")));

                sb.append("@enduml");
                manager.writeFile(fileName, Arrays.asList(sb.toString()));
                System.out.println("Saved into " + System.getProperty("user.dir"));
            } else {
                String text = manager.fileToText(file.getAbsolutePath());
                Klass klass = reader.readKlass(text);
                List<String> umlLines = maker.makeClassUml(klass);

                manager.writeFile(fileName, umlLines);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchFileException("Include either the directory from which to find the classes or a single java file to parse");
        }

    }
}