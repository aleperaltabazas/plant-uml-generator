package parsing;

import exceptions.BuildError;
import klass.Klass;
import klass.KlassBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KlassReader {
    private final String klassDefinitionRegex = "(public )?((abstract )?class|interface) \\w+(<.*>)?( extends \\w+(<.*>)?)?( implements \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";

    //basically: (public )?class <className>( extends <parent>)?( implements <interface>, <interface>...)? {
    private final String classRegex = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?class \\w+( extends \\w+)?( implements \\w+\\s?(,\\s?\\w+\\s?)*)?\\s?[{]";

    //basically: (public )?abstract class <className>( extends <parent>)?( implements <interface>, <interface>...)? {
    private final String abstractRegex = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?abstract class \\w+(<.*>)? (extends \\w+(<.*>)?)?( implements \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";

    //basically: (public )?interface <interfaceName>(extends <parentInterface>)? {
    private final String interfaceRegex = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?interface \\w+(<.*>)?( extends \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";

    private final String annotationRegex = "\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?";

    public List<Klass> parseClasses(List<String> classes) throws BuildError {
        List<Klass> klasses = new ArrayList<>();
        classes.forEach(klass -> {
            try {
                klasses.add(readKlass(klass));
            } catch (BuildError e) {
                e.printStackTrace();
            }
        });

        return klasses;
    }

    public Klass readKlass(String text) throws BuildError {
        text = filterImports(text);
        text = filterPackage(text);

        List<String> klassAnnotations = getAnnotations(text);
        String body = getBody(text);
        String header = getHeader(text);

        KlassBuilder kb = new KlassBuilder();
        kb.addClassDefinition(header);
        kb.addClassBody(body);
        kb.addAnnotations(klassAnnotations);

        return kb.build();
    }

    private List<String> getAnnotations(String text) {
        List<String> annotations = new ArrayList<>();
        String definition = "";

        for (String line : text.split("\n")) {
            if (line.matches(classRegex) || line.matches(abstractRegex) || line.matches(interfaceRegex)) {
                definition = line;
                break;
            }

            if (line.matches(annotationRegex)) {
                annotations.add(line.replaceAll("\\s+", " "));
            }
        }

        annotations.addAll(Arrays
                .stream(definition.split("\\s"))
                .filter(word -> word.matches(annotationRegex))
                .collect(Collectors.toList()));

        return annotations;
    }

    private String filterPackage(String text) {
        return text.replaceAll("package .*;", "");
    }

    private String filterImports(String text) {
        return text.replaceAll("import .*;", "");
    }

    private String getHeader(String text) {
        String header = "";

        for (String line : text.split("\n")) {
            if (line.matches(classRegex) || line.matches(abstractRegex) || line.matches(interfaceRegex)) header = line;
        }

        return header;
    }

    private String getBody(String text) {
        String[] lines = text.split("\n");
        StringBuilder sb = new StringBuilder();

        int curlyBraces = 0;
        boolean inClassBody = false;

        for (String line : lines) {
            if (inClassBody) {

                if (curlyBraces == 0) {
                    String effectiveLine = line.replaceAll("([{]|[}]|;)", "");

                    if (!effectiveLine.isEmpty())
                        sb.append(effectiveLine).append(";\n");
                }

                if (line.contains("}")) curlyBraces--;
                if (line.contains("{")) curlyBraces++;

                continue;
            }

            inClassBody = line.matches(klassDefinitionRegex);
        }

        return sb.toString();
    }
}