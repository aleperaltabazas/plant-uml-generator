package klass;

import exceptions.NoClassDefinitionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
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
            parent = null;
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
        attributes = parseAttributes(effectiveLines, body);
    }

    private List<Attribute> parseAttributes(List<String> lines, String body) {
        String attributeRegex = "\\s?(public |private |protected )?\\w(\\w+.?)* \\w+\\s?(=\\s?.*)?;";
        Pattern attributePattern = Pattern.compile(attributeRegex);

        List<Attribute> attributes = new ArrayList<>();
        AttributeBuilder attributeBuilder = new AttributeBuilder();

        int curlyCount = 0;

        for (String line : lines) {
            if (line.contains("{")) curlyCount++;
            if (line.contains("}")) curlyCount--;
            if (attributePattern.matcher(line).matches()) {
                if (curlyCount == 1) {
                    attributeBuilder.addAttributeDefinition(line);
                    attributeBuilder.determineVisibility(body);
                    Attribute attr = attributeBuilder.build();
                    attributes.add(attr);
                }
            }
        }

        return attributes;
    }

    private List<Method> parseMethods(List<String> lines) {
        String methodRegex = "\\s?(public |private |protected )?\\w(\\w+.?)* \\w+\\s?[(](\\w+ \\w+(, \\w+ \\w+)*)?[)]\\s?([{]?|;)\\s?";
        Pattern methodPattern = Pattern.compile(methodRegex);

        List<Method> methods = new ArrayList<>();
        MethodBuilder methodBuilder = new MethodBuilder();

        int curlyCount = 1;
        List<String> methodLines = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("{")) curlyCount++;
            if (lines.contains("}")) curlyCount--;

            if (methodPattern.matcher(line).matches()) {
                if (curlyCount == 1) {
                    methodBuilder.addMethodDefinition(methodLines.get(0));
                    methods.add(methodBuilder.build());
                    continue;
                }

                methodLines.add(line);
            }
        }

        return methods;
    }

    private List<String> filterConstructor(String body) {
        String constructorRegex = "\\s?(public |private |protected )?" + this.name + "[(].*[)]\\s?[{](.|\n)*[}]";
        Pattern constructorPattern = Pattern.compile(constructorRegex);

        return Arrays.asList(body.replaceAll(constructorRegex, "").split("\n"));
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
}