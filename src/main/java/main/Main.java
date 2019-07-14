package main;

import exceptions.BuildError;
import exceptions.ImageCreatingError;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class Main {
    public static void main(String[] args) throws IOException, BuildError {
        if (args.length < 1)
            throw new NoSuchFileException("Run with the path to the model as argument");

        String path = args[0];
        try {
            new Diagrams().create(path);
        } catch (ImageCreatingError e) {
            System.out.println("Error converting .puml to image.");
            System.out.println("Please, compile the .puml by yourself (for example, in www.planttext.com). Sorry!");
        }

        System.out.println("Saved into " + System.getProperty("user.dir"));
    }

}