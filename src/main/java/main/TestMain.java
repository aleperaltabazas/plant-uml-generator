package main;

import exceptions.NoSuchDirectoryException;
import klass.Klass;
import klass.KlassBuilder;
import klass.objekt.Objekt;
import parsing.FileManager;
import parsing.KlassReader;
import parsing.UMLMaker;

import java.io.File;
import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        String path = args[0];
        File file = new File(path);
        String fileName = file.getName() + ".puml";

        if (!file.isDirectory()) {
            throw new NoSuchDirectoryException("arg0 should be directory");
        }

        FileManager manager = new FileManager();
        KlassReader reader = new KlassReader();
        UMLMaker maker = new UMLMaker();

        List<String> klassesText = manager.findAllClasses(file.getAbsolutePath());
        List<KlassBuilder> builder = reader.getBuilders(klassesText);
        List<Klass> ks = reader.createKlasses(builder);

        System.out.println(ks.size());
        ks.forEach(k -> show(k, ""));
    }

    private static void show(Klass k, String s) {
        if (k == Objekt.getInstance())
            System.out.println(s + Objekt.getInstance().getName());
        else {
            System.out.println(s + k.getName());
            show(k.getSuperKlass(), s + "   ");
        }
    }
}