package klass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MethodBuilder {
    private String returnType;
    private List<Argument> arguments;
    private boolean visible;
    private String name;

    public void addMethodDefinition(String definition) {
        String nameRegex = "(public |private |protected )?\\w([\\w+][.]?) \\w+";
        Pattern namePattern = Pattern.compile(nameRegex);

        parseVisibility(definition);
        parseTypeAndName(definition, namePattern);
        parseArguments(definition, nameRegex);
    }

    private void parseVisibility(String definition) {
        visible = definition.contains("public");
    }

    private void parseArguments(String definition, String nameRegex) {
        String arguments = definition.replace(nameRegex, "").replace('(', Character.MIN_VALUE).replace(')', Character.MIN_VALUE);
        char[] ars = arguments.toCharArray();
        List<Character> chars = new ArrayList<>();

        for (char a : ars)
            chars.add(a);

        int greaterOrLesserThan = 0;
        List<Character> name = new ArrayList<>();
        List<Character> type = new ArrayList<>();
        this.arguments = new ArrayList<>();

        boolean addingType = true;

        for (Character character : chars) {
            if (character == '<') greaterOrLesserThan++;
            if (character == '>') greaterOrLesserThan--;

            if (character == ' ') {
                addingType = false;
                continue;
            }

            if (character == ',' && greaterOrLesserThan == 0) {
                StringBuilder nameBuilder = new StringBuilder();
                name.forEach(c -> nameBuilder.append(c));

                StringBuilder typeBuilder = new StringBuilder();
                type.forEach(c -> typeBuilder.append(c));
                this.arguments.add(new Argument(typeBuilder.toString(), nameBuilder.toString()));
                addingType = true;
                continue;
            }

            if (addingType)
                type.add(character);
            else
                name.add(character);
        }
    }

    private void parseTypeAndName(String definition, Pattern namePattern) {
        if (namePattern.matcher(definition).find()) {
            String typeAndName = namePattern.matcher(definition).group(0);
            String[] typeAndNameArray = typeAndName.replace("(public|protected|private)", "").split("\\s");
            returnType = typeAndNameArray[0];
            name = typeAndNameArray[1];
        }
    }
}