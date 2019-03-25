package main;

import exceptions.BuildError;
import klass.Klass;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.TableReader;
import parsing.UMLMaker;
import persistence.ForeignKey;
import persistence.Table;

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
                Process proc = Runtime.getRuntime().exec("java -jar /home/alejandroperalta/Downloads/plantuml.jar " +
                        "classes.uml");
            }

            if (file.isDirectory()) {
                List<String> klassesText = manager.findAllClasses(file.getAbsolutePath());
                List<Klass> klasses = reader.parseClasses(klassesText);

                writeClassDiagram(manager, maker, fileName, klasses);
                writeERD(klasses, maker, manager);

            } else {
                String text = manager.fileToText(file.getAbsolutePath());
                Klass klass = reader.readKlass(text);
                List<String> umlLines = maker.writeClassDiagram(klass);

                manager.writeFile(fileName, umlLines);
            }
            System.out.println("Saved into " + System.getProperty("user.dir"));

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchFileException("Include either the directory from which to find the classes or a single " +
                    "java file to parse");
        }

    }

    private static void writeERD(List<Klass> klasses, UMLMaker maker, FileManager manager) throws IOException {
        TableReader tr = new TableReader();

        List<ForeignKey> foreignKeys = tr.readAllFks(klasses);
        List<Table> tables = tr.readAllTables(klasses, foreignKeys);

        StringBuilder tableSB = new StringBuilder();
        tableSB.append("@startuml\n");
        tableSB.append("hide circle\n");
        tableSB.append("hide empty members\n");

        tables.forEach(table -> {
            maker.writeERD(table, foreignKeys).forEach(line -> tableSB.append(line).append("\n"));

        });

        tableSB.append(maker.appendRelations(foreignKeys)).append("\n");

        tableSB.append("@enduml");

        manager.writeFile("erd.uml", Arrays.asList(tableSB.toString()));
    }

    private static void writeClassDiagram(FileManager manager, UMLMaker maker, String fileName, List<Klass> klasses) throws IOException {
        StringBuilder classSB = new StringBuilder();
        classSB.append("@startuml\n");

        klasses.stream().filter(klass -> !klass.isIgnorable()).forEach(klass -> maker.writeClassDiagram(klass).
                forEach(line -> classSB.append(line).append("\n")));

        classSB.append("@enduml");
        manager.writeFile(fileName, Arrays.asList(classSB.toString()));
    }
}