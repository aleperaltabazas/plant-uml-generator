import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    public List<String> findAllClasses(String path) {
        File file = new File(path);
        List<File> content = Arrays.asList(file.listFiles());

        List<String> classes = new ArrayList<>();

        content.forEach(f -> {
            if (f.isDirectory())
                findAllClasses(f.getAbsolutePath());
            else if (f.getName().endsWith(".java")) {
                try {
                    classes.add(fileToText(f.getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        return classes;
    }

    public String fileToText(String path) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<String> lines = reader.lines().collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        lines.forEach(line -> sb.append(line).append("\n"));

        return sb.toString();
    }
}
