import klass.Klass;
import klass.KlassBuilder;

public class KlassReader {
    private final String klassDefinitionRegex = "(public )?class \\w+( extends \\w+)?( implements \\w+\\s?(,\\s?\\w+\\s?)*)?\\s?[{]";

    public Klass readKlass(String text) throws Exception {
        text = filterImports(text);
        text = filterPackage(text);

        String body = getBody(text);
        String header = getHeader(text);

        KlassBuilder kb = new KlassBuilder();
        kb.addClassDefinition(header);
        kb.addClassBody(body);

        return kb.build();
    }

    private String filterPackage(String text) {
        return text.replaceAll("package .*;", "");
    }

    private String filterImports(String text) {
        return text.replaceAll("import .*;", "");
    }

    private String getHeader(String text) {
        String header = null;

        for (String line : text.split("\n")) {
            if (line.matches(klassDefinitionRegex)) header = line;
        }

        return header;
    }

    private String getBody(String text) {
        String[] lines = text.split("\n");
        StringBuilder sb = new StringBuilder();

        int curlyBraces = 0;
        boolean inClassBody = false;

        for (String line : lines) {
            if (inClassBody) {

                if (curlyBraces == 0) {
                    String effectiveLine = line.replaceAll("([{]|[}]|;)", "");

                    if (!effectiveLine.isEmpty())
                        sb.append(effectiveLine).append(";\n");
                }

                if (line.contains("}")) curlyBraces--;
                if (line.contains("{")) curlyBraces++;

                continue;
            }

            inClassBody = line.matches(klassDefinitionRegex);
        }

        return sb.toString();
    }
}