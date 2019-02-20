package some.klass;

import exceptions.InvalidRegex;
import exceptions.NoClassDefinitionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class TextManager {
    private List<String> parseLines(String path) {
        File file = new File(path);
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line.replace("\\s+", " "));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public Klass readKlass(String path) {
        List<String> lines = filterUnnecessary(path);

        String classDefinition = getClassHeader(lines);

        return null;
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
                    "( implements \\w+(\\s?,\\s?\\w+)*)?")).
                    findFirst().get();
        } catch (NoSuchElementException e) {
            throw new NoClassDefinitionException();
        }
    }
}
