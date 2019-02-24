package klass;

import exceptions.NoClassDefinitionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KlassBuilder {
    private ClassType classType;
    private String name = "Klass";
    private String parent;
    private List<String> interfaces;
    private List<Attribute> attributes;
    private List<Method> methods;

    public Klass build() throws Exception {
        if (classType == null || name == null) {
            throw new Exception("Need parameters to build");
        }

        return new Klass(attributes, methods, name, classType, parent);
    }

    public void addClassDefinition(String classDefinition) {
        if (classDefinition.contains(" abstract class "))
            classType = ClassType.Abstract;
        else if (classDefinition.contains(" interface "))
            classType = ClassType.Interface;
        else if (classDefinition.contains(" class "))
            classType = ClassType.Concrete;
        else
            throw new NoClassDefinitionException();

        List<String> words = Arrays.asList(classDefinition.split("\\s"));
        this.name = words.get(words.indexOf(words.stream().filter(w -> w.replace("\\s", "").equalsIgnoreCase("class")).findFirst().get()) + 1);

        if (classDefinition.contains("extends")) {
            parent = words.get(words.indexOf(words.stream().filter(w -> w.equalsIgnoreCase("extends")).findFirst().get()) + 1);
        } else {
            parent = null;
        }

        if (classDefinition.contains("implements")) {
            StringBuilder parentBuilder = new StringBuilder();
            words.stream().filter(w -> words.indexOf(w) > words.indexOf("implements")).collect(Collectors.toList()).forEach(parentBuilder::append);
            interfaces = Arrays.asList(parentBuilder.toString().replaceAll("\\s", "").split(","));
        } else {
            interfaces = new ArrayList<>();
        }


    }

    public void addClassBody(String body) {
        List<String> lines = Arrays.asList(body.split("\n"));

        parseBody(lines);
    }

    private void parseBody(List<String> lines) {
        String methodRegex = "\\s*(public |private |protected )?(static )?(\\w|[.])+ \\w+\\s?[(](\\w+ \\w+(, \\w+ \\w+)*)?[)]\\s?([{]?|;)\\s?";
        String attributeRegex = "\\s*(public |protected |private )?(static )?(final )?(\\w|[.])* \\w+\\s?;";

        MethodBuilder mb = new MethodBuilder();
        AttributeBuilder ab = new AttributeBuilder();

        methods = new ArrayList<>();
        attributes = new ArrayList<>();

        for (String line : lines) {
            String spaceless = line.replaceAll("\\s+", " ");
            if (spaceless.startsWith(" "))
                spaceless = spaceless.substring(1, spaceless.length() - 1);


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