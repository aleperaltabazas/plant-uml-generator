package parsing;

import exceptions.BuildError;
import exceptions.NoClassDefinitionException;
import exceptions.NoSuchClassException;
import klass.Klass;
import klass.KlassBuilder;
import klass.objekt.Objekt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static parsing.RegexRepository.*;

public class KlassReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassReader.class);

    public List<Klass> createKlasses(List<KlassBuilder> builders) {
        List<Klass> klasses = new ArrayList<>();
        builders.forEach(builder -> {
            Klass newKlass = createKlass(builder, builders, klasses);
            klasses.add(newKlass);

            if (!klasses.contains(newKlass)) {
                klasses.add(newKlass);
            }
        });


        ArrayList<Klass> finalList = new ArrayList<>(new LinkedHashSet<>(klasses));

        return finalList.stream().filter(klass -> !klass.getName().equalsIgnoreCase("klass")).collect(Collectors.toList());
    }

    public Klass createKlass(KlassBuilder klassBuilder, List<KlassBuilder> others, List<Klass> klasses) {
        if (klassBuilder.hasParent()) {
            Klass superClass;

            try {
                if (klasses.stream().anyMatch(klass -> klass.getName().equalsIgnoreCase(klassBuilder.getSuperClass()))) {
                    superClass =
                            klasses.stream().filter(klass -> klass.getName().equals(klassBuilder.getSuperClass()))
                                    .findFirst().orElseThrow(() -> new NoSuchClassException(klassBuilder.getSuperClass()));
                } else {
                    KlassBuilder other =
                            others.stream().filter(otherBuilder -> otherBuilder.getName().equalsIgnoreCase(klassBuilder.getSuperClass()))
                                    .findFirst().orElseGet(() -> new KlassBuilder());
                    superClass = createKlass(other, others, klasses);
                    if (!klasses.contains(superClass)) klasses.add(superClass);
                }
            } catch (NoClassDefinitionException e) {
                LOGGER.info("Error finding superclass, defaulting to Object", e);
                superClass = Objekt.getInstance();
            }
            return klassBuilder.setSuperKlass(superClass).buildWithSuperclass();
        }

        return klassBuilder.buildWithObjectSuperclass();
    }

    public List<KlassBuilder> getBuilders(List<String> classes) {
        List<KlassBuilder> builders = new ArrayList<>();
        classes.forEach(klass -> {
            try {
                builders.add(getKlassBuilder(klass));
            } catch (NoClassDefinitionException e) {
                LOGGER.error("No class was found", e);
            }
        });

        return builders;
    }

    public List<Klass> parseClasses(List<String> classes) throws BuildError {
        List<Klass> klasses = new ArrayList<>();
        classes.forEach(klass -> {
            try {
                klasses.add(readKlass(klass));
            } catch (NoClassDefinitionException e) {
                LOGGER.error("No class was found.", e);
            }
        });

        return klasses;
    }

    public Klass readKlass(String text) throws BuildError {
        KlassBuilder kb = getKlassBuilder(text);

        return kb.build();
    }

    private KlassBuilder getKlassBuilder(String text) {
        text = filterImports(text);
        text = filterPackage(text);

        List<String> klassAnnotations = getAnnotations(text);
        String body = getBody(text);
        String header = getHeader(text);

        KlassBuilder kb = new KlassBuilder();
        kb.addClassDefinition(header, body);
        kb.addClassBody(body);
        kb.addAnnotations(klassAnnotations);
        return kb;
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