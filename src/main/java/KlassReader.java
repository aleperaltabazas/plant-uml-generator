public class KlassReader {
    private final String klassDefinitionRegex = "(public )?class \\w+( extends \\w+)?( implements \\w+\\s?(,\\s?\\w+\\s?)*)?\\s?[{]";

    public void readKlass(String text) {
        text = filterImports(text);
        text = filterPackage(text);

        String body = getBody(text);
        String header = getHeader(text);

        System.out.println(body);
        System.out.println(header);
    }

    private String removeUselessSpaces(String text) {
        return text.replaceAll("\\s+", " ");
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
                        sb.append(effectiveLine + ";\n");
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