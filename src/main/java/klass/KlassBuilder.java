package klass;

import exceptions.BuildError;
import exceptions.NoClassDefinitionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class KlassBuilder {
    private ClassType classType;
    private String name = "Klass";
    private String parent;
    private List<String> interfaces;
    private List<Attribute> attributes;
    private List<Method> methods;
    private List<Modifier> modifiers = new ArrayList<>();

    public Klass build() throws BuildError {
        if (classType == null || name == null) {
            throw new BuildError("Need parameters to build. Name: " + name + ", type: " + classType);
        }

        return new Klass(attributes, methods, name, classType, interfaces, parent, modifiers);
    }

    public void addClassDefinition(String classDefinition) {
        if (classDefinition.contains(" abstract class ")) {
            classType = ClassType.Abstract;
        } else if (classDefinition.contains(" class ")) {
            classType = ClassType.Concrete;
        } else if (classDefinition.contains(" interface ")) {
            classType = ClassType.Interface;
        } else {
            throw new NoClassDefinitionException();
        }

        List<String> words = Arrays.asList(classDefinition.split("\\s"));
        parseModifiers(words);

        try {
            this.name = words.get(words.indexOf(words.stream().filter(w -> {
                String noSpaces = w.replaceAll("\\s+", "");
                return w.equalsIgnoreCase("class") || w.equalsIgnoreCase("abstract class") || w.equalsIgnoreCase("interface");
            }).findFirst().get()) + 1);
        } catch (NoSuchElementException e) {
            throw e;
        }

        if (classDefinition.contains("extends")) {
            parent = words.get(words.indexOf(words.stream().filter(w -> w.equalsIgnoreCase("extends")).findFirst().get()) + 1);
        } else {
            parent = null;
        }

        if (classDefinition.contains("implements")) {
            StringBuilder parentBuilder = new StringBuilder();
            words.stream().filter(w -> words.indexOf(w) > words.indexOf("implements") && !w.equals("{")).collect(Collectors.toSet()).forEach(str -> {
                parentBuilder.append(str);
            });
            interfaces = Arrays.asList(parentBuilder.toString().replaceAll("\\s", "").split(","));
        } else {
            interfaces = new ArrayList<>();
        }


    }

    private void parseModifiers(List<String> words) {
        if (words.get(0).equals("public"))
            modifiers.add(Modifier.Public);
        else
            modifiers.add(Modifier.PackagePrivate);

        for (String word : words) {
            if (word.equals("abstract"))
                modifiers.add(Modifier.Abstract);
            if (word.equals("final"))
                modifiers.add(Modifier.Final);
            if (word.matches("\\w+<.*>"))
                modifiers.add(Modifier.Generic);
        }
    }

    public void addClassBody(String body) throws BuildError {
        List<String> lines = Arrays.asList(body.split("\n"));

        parseBody(lines);
    }

    private void parseBody(List<String> lines) throws BuildError {
        String methodRegex = "\\s*(public |private |protected )?(static )?(\\w|[.]|<|>|,)+ \\w+\\s?[(].*[)]\\s?([{]?|;)\\s?";
        String attributeRegex = "\\s*(public |protected |private )?(static )?(final )?(\\w|[.]|<|>|,)* \\w+\\s?;";
        String constructorRegex = "\\s*(public |protected |private )" + name + "\\s?[(].*[)]\\s?([{]|[;])?";

        MethodBuilder mb = new MethodBuilder();
        AttributeBuilder ab = new AttributeBuilder();

        methods = new ArrayList<>();
        attributes = new ArrayList<>();

        for (String line : lines) {
            String spaceless = line.replaceAll("\\s+", " ");
            if (spaceless.startsWith(" "))
                spaceless = spaceless.substring(1, spaceless.length() - 1);

            if (line.matches(constructorRegex))
                continue;

            if (line.matches(methodRegex)) {
                mb.addDefinition(spaceless);
                methods.add(mb.build());
                mb.clear();
            } else if (line.matches(attributeRegex)) {
                ab.addDefinition(spaceless);
                attributes.add(ab.build());
                ab.clear();
            }
        }
    }

    public ClassType getClassType() {
        return classType;
    }

    public String getName() {
        return name;
    }

    public String getSuperClass() {
        return parent;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Method> getMethods() {
        return methods;
    }
}