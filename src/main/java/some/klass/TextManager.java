package some.klass;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<String> lines = parseLines(path).stream().filter(line -> {
            String regex = "(\\w|\\.|\\*)*;";
            return !line.matches("import( static|) " + regex) || !line.matches("package " + regex);
        }).collect(Collectors.toList());

        String classDefinition = lines.stream().filter(line -> line.matches("" +
                "(public )?(class|interface|abstract class) \\w+" +
                "( extends \\w+)?" +
                "( implements \\w+(\\s?,\\s?\\w+)*)?")).
                findFirst().get();

        return null;
    }
}
