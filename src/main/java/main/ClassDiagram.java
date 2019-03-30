package main;

import exceptions.NoSuchDirectoryException;
import klass.Klass;
import klass.builders.KlassBuilder;
import net.sourceforge.plantuml.SourceFileReader;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.UMLMaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ClassDiagram {
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
            Main.writeERD(klasses, maker, manager);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}