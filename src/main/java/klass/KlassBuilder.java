package klass;

import exceptions.NoClassDefinitionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KlassBuilder {
    private ClassType classType;
    private String name = "Klass";
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
        List<String> effectiveLines = filterConstructor(body);
        methods = parseMethods(effectiveLines);
        attributes = parseAttributes(effectiveLines);
    }

    private List<Attribute> parseAttributes(List<String> lines) {

        return null;
    }

    private List<Method> parseMethods(List<String> lines) {
        String methodRegex = "(public |private |protected )?\\w([\\w+][.]?)* \\w+\\s?[(](\\w+ \\w+(, \\w+ \\w+)*)?[)]\\s?[{]?\\s?";
        Pattern methodPattern = Pattern.compile(methodRegex);

        int curlyCount = 0;
        for (String line : lines) {
            if (methodPattern.matcher(line).matches()) {
                if (line.contains("{")) curlyCount++;
            }
        }

        return null;
    }

    private List<String> filterConstructor(String body) {
        String constructorRegex = "(public |private |protected )?" + this.name + "[(].*[)]\\s?[{]";
        Pattern constructorPattern = Pattern.compile(constructorRegex);
        Matcher matcher;

        List<String> lines = Arrays.asList(body.split("\n"));
        boolean skip = true;
        List<String> constructor = new ArrayList<>();

        int curlyCount = 0;

        for (String line : lines) {
            if (constructorPattern.matcher(line).matches()) {
                skip = false;
            }

            if (!skip) {
                if (line.contains("{")) curlyCount++;
                if (line.contains("}")) curlyCount--;

                constructor.add(line);

                if (curlyCount == 0) skip = true;
            }
        }

        return lines.stream().filter(line -> constructor.stream().anyMatch(l -> l.equalsIgnoreCase(line))).collect(Collectors.toList());
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