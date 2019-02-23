import exceptions.NoClassDefinitionException;
import klass.Klass;
import klass.KlassBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class JavaParser {
    private List<String> parseLines(String path) {
        File file = new File(path);
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line.replaceAll("\\s+", " "));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public Klass readKlass(String path) throws Exception {
        List<String> lines = filterUnnecessary(path);

        String classDefinition = getClassHeader(lines);
        String body = getBody(lines);

        KlassBuilder kb = new KlassBuilder();
        kb.addClassDefinition(classDefinition);
        kb.addClassBody(body);

        return kb.build();
    }

    private String getBody(List<String> lines) {
        StringBuilder sb = new StringBuilder();

        int curlyBraces = 0;
        for (String line : lines) {
            if (line.contains("{")) curlyBraces++;
            if (line.contains("}")) curlyBraces--;

            if (curlyBraces > 0) sb.append(line + "\n");
        }

        return sb.toString();
    }

    private List<String> filterUnnecessary(String path) {
        return parseLines(path).stream().filter(line -> {
            String regex = "(\\w|\\.|\\*)*;";
            return !(line.matches("import( static|) " + regex) || line.matches("package " + regex) || line.isEmpty());
        }).collect(Collectors.toList());
    }

    public String getClassHeader(List<String> lines) {
        try {
            return lines.stream().filter(line -> line.matches("" +
                    "(public )?(class|interface|abstract class) \\w+" +
                    "( extends \\w+)?" +
                    "( implements \\w+(\\s?,\\s?\\w+)*)?\\s[{]")).
                    findFirst().get();
        } catch (NoSuchElementException e) {
            throw new NoClassDefinitionException();
        }
    }
}
