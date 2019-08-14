package klass.builders;

import exceptions.BuildError;
import exceptions.NoClassDefinitionException;
import io.vavr.collection.List;
import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.Modifier;
import klass.classtype.*;

import java.util.NoSuchElementException;

import static parsing.RegexConstants.*;

public class KlassBuilder {
    private ClassType classType;
    private String name;
    private String parent = "";
    private List<String> interfaces = List.empty();
    private List<Attribute> attributes = List.empty();
    private List<Method> methods = List.empty();
    private List<Modifier> modifiers = List.empty();
    private List<String> annotations = List.empty();
    private Klass superKlass;
    private List<MethodBuilder> methodBuilders = List.empty();
    private List<AttributeBuilder> attributeBuilders = List.empty();

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

        List<String> words = List.of(classDefinition.split("\\s"));
        parseModifiers(words);

        try {
            this.name =
                    words.get(words.indexOf(words.filter(w -> classType(w))
                            .headOption()
                            .getOrElseThrow(() -> new NoClassDefinitionException(classDefinition))) + 1)
                            .replaceAll("[{]", "");
        } catch (NoSuchElementException e) {
            throw new NoClassDefinitionException(classDefinition);
        }

        if (classDefinition.contains("extends")) {
            parent =
                    words.get(words.indexOf(words.filter(w -> w.equalsIgnoreCase("extends"))
                            .headOption().getOrElseThrow(() -> new NoClassDefinitionException(classDefinition))) + 1)
                            .replaceAll("[{]", "");
        } else {
            parent = null;
        }

        interfaces = List.empty();

        if (classDefinition.contains("implements")) {
            for (String str : words.filter(w -> words.indexOf(w) > words.indexOf("implements") && !w.equals("{"))
                    .distinct()) {
                interfaces = interfaces.append(str.replaceAll("(\\s+|,)", ""));
            }
        }
    }

    private boolean classType(String word) {
        String lowerCase = word.toLowerCase();

        return lowerCase.equals("class") || lowerCase.equals("abstract class") || lowerCase.equals("interface")
                || lowerCase.equals("enum");
    }

    private List<String> parseEnumConstants(String body) {
        List<String> lines = List.of(body.split("\n"));
        List<String> constants = List.empty();

        for (String line : lines) {
            String constant = "";
            if (line.matches(ENUM_CONSTANT)) {
                constant = line.replaceAll("(\\s+|,|;)", "");
            } else if (line.matches(ENUM_CONSTANT_WITH_BEHAVIOR)) {
                constant = line.substring(0, line.indexOf(";")).replaceAll("\\s+", "");
            }

            if (!constant.isEmpty()) {
                constants = constants.append(constant);
            }
        }

        return constants;
    }

    private void parseModifiers(List<String> words) {
        if (words.get(0).equals("public"))
            modifiers = modifiers.append(Modifier.PUBLIC);
        else
            modifiers.append(Modifier.PACKAGE_PRIVATE);

        for (String word : words) {
            if (word.equals("abstract"))
                modifiers.append(Modifier.ABSTRACT);
            if (word.equals("final"))
                modifiers.append(Modifier.FINAL);
            if (word.matches("\\w+<.*>"))
                modifiers.append(Modifier.GENERIC);
        }
    }

    public void addClassBody(String body) {
        List<String> lines = List.of(body.split("\n"));

        parseBody(lines);
    }

    private void parseBody(List<String> lines) {
        String constructorRegex = "\\s*(public |protected |private )?" + name + "\\s?[(].*[)]\\s?([{]|[;])?";

        MethodBuilder mb = new MethodBuilder();
        AttributeBuilder ab = new AttributeBuilder();

        methods = List.empty();
        attributes = List.empty();
        List<String> annotations = List.empty();

        for (String line : lines) {
            String spaceless = line.replaceAll("\\s+", " ");
            if (spaceless.startsWith(" "))
                spaceless = spaceless.substring(1, spaceless.length() - 1);

            if (line.matches(constructorRegex))
                continue;

            if (spaceless.matches(ANNOTATION)) {
                annotations = annotations.append(spaceless);
            }

            if (line.matches(METHOD)) {
                mb.addDefinition(spaceless);
                mb.addAnotations(annotations);
                methods = methods.append(mb.build());

                methodBuilders = methodBuilders.append(mb);
                mb = new MethodBuilder();
                annotations = List.empty();
            } else if (line.matches(ATTRIBUTE)) {
                ab.addDefinition(spaceless);
                ab.addAnotations(annotations);
                attributes = attributes.append(ab.build());
                attributeBuilders = attributeBuilders.append(ab);
                ab = new AttributeBuilder();
                annotations = List.empty();
            }
        }
    }

    public KlassBuilder addAnnotations(List<String> annotations) {
        if (this.annotations == null)
            this.annotations = List.empty();

        this.annotations = this.annotations.appendAll(annotations);
        return this;
    }

    public KlassBuilder setSuperKlass(Klass superKlass) {
        this.superKlass = superKlass;
        return this;
    }

    public ClassType getClassType() {
        return classType;
    }

    public KlassBuilder setClassType(ClassType classType) {
        this.classType = classType;
        return this;
    }

    public String getName() {
        return name;
    }

    public KlassBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getSuperClass() {
        return parent;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public KlassBuilder setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public KlassBuilder setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public KlassBuilder setMethods(List<Method> methods) {
        this.methods = methods;
        return this;
    }

    public KlassBuilder setParent(String parent) {
        this.parent = parent;
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
