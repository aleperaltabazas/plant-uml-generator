package klass;

import java.util.ArrayList;
import java.util.List;

public class MethodBuilder extends AbstractBuilder {
    private String returnType;
    private List<Argument> arguments;
    private boolean visible;
    private String name;

    public void addMethodDefinition(String definition) {
        parseVisibility(definition);
        parseType(definition);
        parseName(definition);
        parseArguments(definition);
    }

    private void parseVisibility(String definition) {
        visible = definition.contains("public");
    }

    private void parseArguments(String definition) {
        int first = definition.indexOf('(') + 1;
        int last = definition.indexOf(')');

        String arguments = definition.substring(first, last);

        char[] ars = arguments.toCharArray();
        List<Character> chars = new ArrayList<>();

        for (char a : ars)
            chars.add(a);

        int greaterOrLesserThan = 0;

        StringBuilder sb = new StringBuilder();

        List<String> names = new ArrayList<>();
        List<String> types = new ArrayList<>();

        for (Character character : chars) {
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

        this.arguments = new ArrayList<>();
        if (!names.isEmpty()) {
            names.forEach(n -> {
                this.arguments.add(new Argument(types.get(names.indexOf(n)), n));
            });
        }

    }

    private void parseType(String definition) {
        String[] words = definition.split("\\s");
        if (declaresVisibility(definition))
            this.returnType = words[1];
        else
            this.returnType = words[0];
    }

    private void parseName(String definition) {
        String[] words = definition.split("\\s");
        String name;

        if (declaresVisibility(definition)) {
            name = words[2];
            this.name = name.substring(0, words[2].indexOf('('));
        } else {
            name = words[1];
            this.name = name.substring(0, words[1].indexOf('('));
        }
    }

    public String getReturnType() {
        return returnType;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getName() {
        return name;
    }
}