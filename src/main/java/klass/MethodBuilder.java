package klass;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MethodBuilder extends AbstractBuilder {
    private Boolean visible;
    private String name;
    private List<Pair<String, String>> argumentsMap;
    private List<Argument> arguments;

    public void addDefinition(String definition) {
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
        this.arguments = new ArrayList<>();

        this.argumentsMap.forEach(a -> this.arguments.add(new Argument(a.getValue(), a.getKey())));
    }

    private void fillArgumentMap(String arguments) {
        char[] ars = arguments.toCharArray();
        List<Character> characters = new ArrayList<>();

        for (char a : ars)
            characters.add(a);

        int greaterOrLesserThan = 0;

        StringBuilder sb = new StringBuilder();

        List<String> names = new ArrayList<>();
        List<String> types = new ArrayList<>();

        for (Character character : characters) {
            if (character == '<') greaterOrLesserThan++;
            if (character == '>') greaterOrLesserThan--;

            if (character == ' ' && greaterOrLesserThan == 0) {
                String str = sb.toString();
                if (!str.isEmpty())
                    types.add(str);
                sb = new StringBuilder();
                continue;
            }

            if (character == ',' && greaterOrLesserThan == 0) {
                names.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }

            sb.append(character);
        }

        String str = sb.toString();
        if (!str.isEmpty())
            names.add(str);

        argumentsMap = new ArrayList<>();

        names.forEach(name -> argumentsMap.add(new Pair<>(name, types.get(names.indexOf(name)))));
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

    public Boolean isVisible() {
        return visible;
    }

    public String getName() {
        return name;
    }

    public Method build() {
        return new Method(name, type, arguments, visible);
    }

    public void clear() {
        name = null;
        arguments = null;
        visible = null;
        argumentsMap = null;
    }
}