package main;

import exceptions.BuildError;
import exceptions.ImageCreatingError;
import klass.Klass;
import parsing.FileManager;
import parsing.TableReader;
import parsing.UMLMaker;
import persistence.ForeignKey;
import persistence.Table;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, BuildError {
        if (args.length < 1)
            throw new NoSuchFileException("Run with the path to the model as argument");

        String path = args[0];
        try {
            new ClassDiagram().create(path);
        } catch (ImageCreatingError e) {
            System.out.println("Error converting .puml to image.");
            System.out.println("Please, compile the .puml by yourself (for example, in www.planttext.com). Sorry!");
        }

        System.out.println("Saved into " + System.getProperty("user.dir"));
    }

    public static void writeERD(List<Klass> klasses, UMLMaker maker, FileManager manager) {
        TableReader tr = new TableReader();

        List<ForeignKey> foreignKeys = tr.readAllFks(klasses);
        List<Table> simpleTables = tr.readAllTables(klasses, foreignKeys);

        StringBuilder tableSB = new StringBuilder();
        tableSB.append("@startuml\n");
        tableSB.append("hide circle\n");
        tableSB.append("hide empty members\n");

        simpleTables.forEach(table -> {
            maker.writeERD(table, foreignKeys).forEach(line -> tableSB.append(line).append("\n"));

        });

        tableSB.append(maker.appendRelations(foreignKeys)).append("\n");

        tableSB.append("@enduml");

        manager.writeFile("erd.uml", Arrays.asList(tableSB.toString()));
    }

}