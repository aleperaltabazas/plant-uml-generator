package main;

import exceptions.BuildError;
import klass.Klass;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.UMLMaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
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

            String fileName = getFileName(args);

            if (file.isDirectory()) {
                makeClassDiagram(manager, reader, maker, file, fileName);
                System.out.println("Saved into " + System.getProperty("user.dir"));
            } else {
                throw new NotDirectoryException(fileName + " is not a directory.");
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchFileException("Need a directory to create class diagram.");
        }

    }

    private static void makeClassDiagram(FileManager manager, KlassReader reader, UMLMaker maker, File file,
                                         String fileName) throws IOException {
        List<String> klassesText = manager.findAllClasses(file.getAbsolutePath());
        List<Klass> klasses = reader.parseClasses(klassesText);

        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");

        klasses.stream().filter(klass -> !klass.isIgnorable()).forEach(klass -> maker.writeClassDiagram(klass).forEach(line -> sb.append(line).append("\n")));

        sb.append("@enduml");
        manager.writeFile(fileName, Arrays.asList(sb.toString()));
    }

    private static String getFileName(String[] args) {
        String fileName;

        try {
            fileName = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            fileName = "classes.uml";
        }
        return fileName;
    }
}