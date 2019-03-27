package klass;

import exceptions.BuildError;
import exceptions.NoClassDefinitionException;
import klass.classtype.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static parsing.RegexRepository.*;

public class KlassBuilder {
    private ClassType classType;
    private String name;
    private String parent = "";
    private List<String> interfaces = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private List<Modifier> modifiers = new ArrayList<>();
    private List<String> annotations = new ArrayList<>();
    private Klass superKlass;

    private void checkNull() {
        if (classType == null || name == null) {
            throw new BuildError("Need parameters to build. Name: " + name + ", type: " + classType);
        }
    }

    public void addClassDefinition(String classDefinition) {
        addClassDefinition(classDefinition, null);
    }

    public void addClassDefinition(String classDefinition, String body) {
        if (classDefinition.contains(" abstract class ")) {
            classType = new AbstractKlass();
        } else if (classDefinition.contains(" class ")) {
            classType = new ConcreteKlass();
        } else if (classDefinition.contains(" interface ")) {
            classType = new Interfase();
        } else if (classDefinition.contains(" enum ")) {
            classType = new EnumKlass(parseEnumConstants(body));
        } else {
            throw new NoClassDefinitionException(classDefinition);
        }

        List<String> words = Arrays.asList(classDefinition.split("\\s"));
        parseModifiers(words);

        try {
            this.name =
                    words.get(words.indexOf(words.stream().filter(w -> classType(w)).
                            findFirst().orElseThrow(() -> new NoClassDefinitionException(classDefinition))) + 1).replaceAll("[{]", "");
        } catch (NoSuchElementException e) {
            throw new NoClassDefinitionException(classDefinition);
        }

        if (classDefinition.contains("extends")) {
            parent =
                    words.get(words.indexOf(words.stream().filter(w -> w.equalsIgnoreCase("extends")).
                            findFirst().orElseThrow(() -> new NoClassDefinitionException(classDefinition))) + 1).replaceAll("[{]", "");
        } else {
            parent = null;
        }

        interfaces = new ArrayList<>();

        if (classDefinition.contains("implements")) {
            words.stream().filter(w -> words.indexOf(w) > words.indexOf("implements")
                    && !w.equals("{")).collect(Collectors.toSet()).forEach(str -> interfaces.add(str.replaceAll("(\\s" +
                    "+|,)", "")));
        }
    }

    private boolean classType(String word) {
        String lowerCase = word.toLowerCase();

        return lowerCase.equals("class") || lowerCase.equals("abstract class") || lowerCase.equals("interface")
                || lowerCase.equals("enum");
    }

    private List<String> parseEnumConstants(String body) {
        List<String> lines = Arrays.asList(body.split("\n"));
        List<String> constants = new ArrayList<>();

        for (String line : lines) {
            String constant = "";
            if (line.matches(enumConstantRegex)) {
                constant = line.replaceAll("(\\s+|,|;)", "");
            } else if (line.matches(enumConstantWithBehaviorRegex)) {
                constant = line.substring(0, line.indexOf(";")).replaceAll("\\s+", "");
            }

            if (!constant.isEmpty()) {
                constants.add(constant);
            }
        }

        return constants;
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
        String constructorRegex = "\\s*(public |protected |private )" + name + "\\s?[(].*[)]\\s?([{]|[;])?";

        MethodBuilder mb = new MethodBuilder();
        AttributeBuilder ab = new AttributeBuilder();

        methods = new ArrayList<>();
        attributes = new ArrayList<>();
        List<String> annotations = new ArrayList<>();

        for (String line : lines) {
            String spaceless = line.replaceAll("\\s+", " ");
            if (spaceless.startsWith(" "))
                spaceless = spaceless.substring(1, spaceless.length() - 1);

            if (line.matches(constructorRegex))
                continue;

            if (spaceless.matches(annotationRegex)) {
                annotations.add(spaceless);
            }

            if (line.matches(methodRegex)) {
                mb.addDefinition(spaceless);
                mb.addAnotations(annotations);
                methods.add(mb.build());
                annotations.clear();
                mb.clear();
            } else if (line.matches(attributeRegex)) {
                ab.addDefinition(spaceless);
                ab.addAnotations(annotations);
                attributes.add(ab.build());
                ab.clear();
                annotations.clear();
            }
        }
    }

    public KlassBuilder addAnnotations(List<String> annotations) {
        if (this.annotations == null)
            this.annotations = new ArrayList<>();

        this.annotations.addAll(annotations);
        return this;
    }

    public KlassBuilder setSuperKlass(Klass superKlass) {
        this.superKlass = superKlass;
        return this;
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

    public KlassBuilder setClassType(ClassType classType) {
        this.classType = classType;
        return this;
    }

    public KlassBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public KlassBuilder setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public KlassBuilder setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public KlassBuilder setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public KlassBuilder setMethods(List<Method> methods) {
        this.methods = methods;
        return this;
    }

    public KlassBuilder setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public KlassBuilder setAnnotations(List<String> annotations) {
        this.annotations = annotations;
        return this;
    }

    public boolean hasParent() {
        if (parent == null) return false;

        return !parent.isEmpty();
    }

    public Klass buildWithSuperclass() {
        checkNull();
        return new Klass(attributes, methods, name, classType, superKlass, interfaces, modifiers, annotations);
    }

    public Klass buildWithObjectSuperclass() {
        checkNull();
        return new Klass(attributes, methods, name, classType, interfaces, modifiers, annotations);
    }
}