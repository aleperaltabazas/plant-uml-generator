package klass;

import java.util.ArrayList;
import java.util.List;

public class MethodBuilder {
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
        List<Character> name = new ArrayList<>();
        List<Character> type = new ArrayList<>();
        this.arguments = new ArrayList<>();

        String word = "";
        List<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean addingType = true;

        int wordCount = 0;

        List<String> names = new ArrayList<>();
        List<String> types = new ArrayList<>();

        boolean skip = false;

        for (Character character : chars) {
            if (skip) {
                skip = false;
                continue;
            }

            if (character == '<') greaterOrLesserThan++;
            if (character == '>') greaterOrLesserThan--;

            if (character == ',' && greaterOrLesserThan == 0) {
                skip = true;
            }

            if (character == ' ' && greaterOrLesserThan == 0) {
                words.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }

            sb.append(character);
        }

        words.forEach(w -> {
            if (words.indexOf(w) % 2 == 0) {
                this.arguments.add(new Argument(w, words.get(words.indexOf(w) + 1)));
            }
        });
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
        } else {
            name = words[1];
        }

        this.name = name.substring(0, words[2].indexOf('('));
    }

    private boolean declaresVisibility(String definition) {
        String starting = definition.substring(0, "protected".length());
        return starting.contains("protected") || starting.contains("private") || starting.contains("public");
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