package parsing;

import exceptions.BuildError;
import exceptions.NoClassDefinitionException;
import klass.Klass;
import klass.KlassBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static parsing.RegexRepository.*;

public class KlassReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassReader.class);

    public List<Klass> parseClasses(List<String> classes) throws BuildError {
        List<Klass> klasses = new ArrayList<>();
        classes.forEach(klass -> {
            try {
                klasses.add(readKlass(klass));
            } catch (NoClassDefinitionException e) {
                LOGGER.info("Error when reading klass: ", e);
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
        kb.addClassDefinition(header, body);
        kb.addClassBody(body);
        kb.addAnnotations(klassAnnotations);

        return kb.build();
    }

    private List<String> getAnnotations(String text) {
        List<String> annotations = new ArrayList<>();
        String definition = "";

        for (String line : text.split("\n")) {
            if (isClassDefinition(line)) {
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

    private boolean isClassDefinition(String line) {
        return line.matches(classRegex) || line.matches(abstractRegex) || line.matches(interfaceRegex) || line.matches(enumRegex);
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
            if (isClassDefinition(line))
                header = line;
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

            inClassBody = line.matches(RegexRepository.klassDefinitionRegex);
        }

        return sb.toString();
    }
}