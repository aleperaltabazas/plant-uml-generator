package parsing;

import exceptions.BuildError;
import exceptions.NoClassDefinitionException;
import exceptions.NoSuchClassException;
import io.vavr.collection.List;
import klass.Klass;
import klass.builders.KlassBuilder;
import klass.objekt.ObjectClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static parsing.RegexConstants.*;

public class KlassReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassReader.class);

    public List<Klass> createKlasses(List<KlassBuilder> builders) {
        List<Klass> klasses = List.empty();
        for (KlassBuilder builder : builders) {
            Klass newKlass = createKlass(builder, builders, klasses);

            if (!klasses.contains(newKlass)) {
                klasses = klasses.append(newKlass);
            }
        }

        return klasses.distinct().filter(klass -> !klass.getName().equalsIgnoreCase("klass"));
    }

    public Klass createKlass(KlassBuilder klassBuilder, List<KlassBuilder> others, List<Klass> klasses) {
        if (klassBuilder.hasParent()) {
            Klass superClass;

            try {
                if (klasses.exists(klass -> klass.getName().equalsIgnoreCase(klassBuilder.getSuperClass()))) {
                    superClass =
                            klasses.filter(klass -> klass.getName().equals(klassBuilder.getSuperClass()))
                                    .headOption().getOrElseThrow(() -> new NoSuchClassException(klassBuilder.getSuperClass()));
                } else {
                    KlassBuilder other =
                            others.filter(otherBuilder -> otherBuilder.getName().equalsIgnoreCase(klassBuilder.getSuperClass()))
                                    .headOption().getOrElseThrow(() -> new NoSuchClassException(klassBuilder.getSuperClass()));
                    superClass = createKlass(other, others, klasses);
                    if (!klasses.contains(superClass)) {
                        klasses = klasses.append(superClass);
                    }
                }
            } catch (NoClassDefinitionException | NoSuchClassException | BuildError e) {
                LOGGER.info("Error finding superclass, defaulting to Object", e);
                superClass = ObjectClass.getInstance();
            }

            return klassBuilder.setSuperKlass(superClass).buildWithSuperclass();
        }

        return klassBuilder.buildWithObjectSuperclass();
    }

    public List<KlassBuilder> getBuilders(List<String> classes) {
        return classes.map(c -> getKlassBuilder(c));
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
        List<String> annotations = List.empty();
        String definition = "";

        for (String line : text.split("\n")) {
            if (isClassDefinition(line)) {
                definition = line;
                break;
            }

            if (line.matches(ANNOTATION)) {
                annotations = annotations.append(line.replaceAll("\\s+", " "));
            }
        }

        annotations = annotations.appendAll(List
                .of(definition.split("\\s"))
                .filter(word -> word.matches(ANNOTATION))
                .collect(Collectors.toList()));

        return annotations;
    }

    private boolean isClassDefinition(String line) {
        return line.matches(CLASS) || line.matches(ABSTRACT) || line.matches(INTERFACE) || line.matches(ENUM);
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

            inClassBody = line.matches(RegexConstants.KLASS_DEFINITION);
        }

        return sb.toString();
    }
}