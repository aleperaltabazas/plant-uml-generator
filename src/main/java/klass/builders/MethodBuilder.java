package klass.builders;

import exceptions.BuildError;
import io.vavr.collection.List;
import javafx.util.Pair;
import klass.Argument;
import klass.Method;

public class MethodBuilder extends AbstractBuilder {
    private String name;
    private List<Pair<String, String>> argumentsMap;
    private List<Argument> arguments;

    public void addDefinition(String definition) {
        parseAnnotations(definition);
        definition = removeAnnotations(definition);
        parseArguments(definition);
        parseVisibility(definition);
        parseType(definition);
        parseName(definition);
    }

    private void parseArguments(String definition) {
        int first = definition.indexOf('(') + 1;
        int last = definition.indexOf(')');

        String arguments = definition.substring(first, last);

        fillArgumentMap(arguments);
        this.arguments = List.empty();

        for (Pair<String, String> a : this.argumentsMap) {
            this.arguments = this.arguments.append(new Argument(a.getValue(), a.getKey()));
        }
    }

    private void fillArgumentMap(String arguments) {
        char[] ars = arguments.toCharArray();
        List<Character> characters = List.empty();

        for (char a : ars) {
            characters = characters.append(a);
        }

        int greaterOrLesserThan = 0;

        StringBuilder sb = new StringBuilder();

        List<String> names = List.empty();
        List<String> types = List.empty();

        for (Character character : characters) {
            if (character == '<') greaterOrLesserThan++;
            if (character == '>') greaterOrLesserThan--;

            if (character == ' ' && greaterOrLesserThan == 0) {
                String str = sb.toString();
                if (!str.isEmpty()) {
                    types = types.append(str);
                }
                sb = new StringBuilder();
                continue;
            }

            if (character == ',' && greaterOrLesserThan == 0) {
                names = names.append(sb.toString());
                sb = new StringBuilder();
                continue;
            }

            sb.append(character);
        }

        String str = sb.toString();
        if (!str.isEmpty()) {
            names = names.append(str);
        }

        argumentsMap = List.empty();

        for (String s : names) {
            this.argumentsMap = argumentsMap.append(new Pair<>(s, types.get(names.indexOf(s))));
        }
    }

    private void parseName(String definition) {
        String name = definition.split("\\s")[presentModifiers(definition) + 1];
        if (name.contains("("))
            this.name = name.substring(0, name.indexOf('('));
        else
            this.name = name;
    }

    public String getReturnType() {
        return type;
    }

    public List<Argument> getArgumentsMap() {
        return arguments;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getName() {
        return name;
    }

    public Method build() throws BuildError {
        if (name == null || type == null || arguments == null) {
            throw new BuildError("Need parameters to build. Name: " + name + ", type: " + type + ", arguments: " + arguments);
        }

        return new Method(name, type, arguments, visible, modifiers);
    }

    public void clear() {
        name = null;
        arguments = null;
        argumentsMap = null;
    }
}