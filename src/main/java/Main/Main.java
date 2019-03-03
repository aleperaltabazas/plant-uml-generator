package Main;

import Managers.ClassFinder;
import Managers.ClassReader;
import Managers.FinalButNotReallyString;
import Managers.PackageFinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String directory = args[0];
        String diagramName;

        File dir = new File(directory);

        try {
            diagramName = args[1];
        } catch (Exception e) {
            diagramName = "class-diagram.txt";
        }

        ClassFinder classFinder = new ClassFinder();
        PackageFinder packageFinder = new PackageFinder();
        ClassReader reader = new ClassReader();

        classFinder.loadClassesInDirectory(directory, null);
        List<Package> packages = packageFinder.packagesFrom(directory);
        Set<Class<?>> classes = new HashSet<>();
        packages.forEach(p -> classes.addAll(classFinder.findClasses(p)));

        FinalButNotReallyString uml = new FinalButNotReallyString();
        classes.forEach(c -> uml.concat(reader.read(c) + "\n"));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(diagramName));
            writer.write(uml.getText());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
