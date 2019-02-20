package klass;

import exceptions.NoClassDefinitionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KlassBuilder {
    private ClassType classType;
    private String name;
    private String parent;
    private List<String> interfaces;
    private List<Attribute> attributes;
    private List<Method> methods;

    public void addClassDefinition(String classDefinition) {
        if (classDefinition.contains("abstract class"))
            classType = ClassType.Abstract;
        else if (classDefinition.contains("interface"))
            classType = ClassType.Interface;
        else if (classDefinition.contains("class"))
            classType = ClassType.Concrete;
        else
            throw new NoClassDefinitionException();

        List<String> words = Arrays.asList(classDefinition.split(" "));
        String name = words.get(words.indexOf(words.stream().filter(w -> w.replace("\\s", "").equalsIgnoreCase("class")).findFirst().get()) + 1);
        StringBuilder sb = new StringBuilder(name);
        this.name = sb.toString();

        if (classDefinition.contains("extends")) {
            parent = words.get(words.indexOf(words.stream().filter(w -> w.equalsIgnoreCase("extends")).findFirst().get()) + 1);
        } else {
            parent = Objekt.getInstance().getName();
        }

        if (classDefinition.contains("implements")) {
            StringBuilder parentBUilder = new StringBuilder();
            words.stream().filter(w -> words.indexOf(w) > words.indexOf("implements")).collect(Collectors.toList()).forEach(w -> parentBUilder.append(w));
            interfaces = Arrays.asList(parentBUilder.toString().split(","));
        } else {
            interfaces = new ArrayList<>();
        }
    }

    public void addClassBody(String body) {
        List<String> lines = Arrays.asList(body.split("\n"));
        lines.forEach(line -> parse(line));
    }

    private void parse(String line) {
        String methodRegex = "(public |private |protected )?\\w+ \\w+\\s?[(](\\w+ \\w+(, \\w+ \\w+)*)?[)]\\s?[{]?\\s?";
        Pattern methodPattern = Pattern.compile(methodRegex);
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

    public void clear() {
        classType = null;
        name = null;
        parent = null;
        interfaces = null;
    }
}
