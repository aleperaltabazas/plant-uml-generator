package main;

import exceptions.NoSuchDirectoryException;
import klass.Klass;
import klass.builders.KlassBuilder;
import net.sourceforge.plantuml.SourceFileReader;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.TableReader;
import parsing.UMLMaker;
import persistence.attributes.ForeignKey;
import persistence.tables.Table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Diagrams {
    public void create(String path) {
        File file = new File(path);
        String fileName = file.getName() + ".puml";

        if (!file.isDirectory()) {
            throw new NoSuchDirectoryException("arg0 should be directory");
        }

        FileManager manager = new FileManager();
        KlassReader reader = new KlassReader();
        UMLMaker maker = new UMLMaker();

        List<String> klassesText = manager.findAllClasses(file.getAbsolutePath());
        List<KlassBuilder> builders = reader.getBuilders(klassesText);
        List<Klass> klasses = reader.createKlasses(builders);

        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");

        klasses.stream().filter(klass -> !klass.isIgnorable()).forEach(klass ->
                maker.writeClassDiagram(klass).forEach(line -> sb.append(line).append("\n")));

        sb.append("@enduml");
        String text = sb.toString();


        Path pathToFile = manager.writeFile(fileName, Arrays.asList(text));
        File sourceFile = pathToFile.toFile();
        try {
            SourceFileReader sourceReader = new SourceFileReader(sourceFile);
            sourceReader.getGeneratedImages();
            writeERD(klasses, maker, manager);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeERD(List<Klass> klasses, UMLMaker maker, FileManager manager) {
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