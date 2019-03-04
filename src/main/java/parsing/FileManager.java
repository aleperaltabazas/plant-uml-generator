package parsing;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    public void writeFile(String fileName, List<String> lines) throws IOException {
        Path file = Paths.get(System.getProperty("user.dir") + "/" + fileName);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    public List<String> findAllClasses(String path) throws FileNotFoundException {
        File file = new File(path);
        List<String> all = new ArrayList<>();

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                try {
                    all.addAll(findAllClasses(f.getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    throw e;
                }
            }
        } else {
            if (file.getName().endsWith(".java"))
                all.add(fileToText(file.getAbsolutePath()));
        }

        return all;
    }

    public String fileToText(String path) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<String> lines = reader.lines().collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        lines.forEach(line -> sb.append(line).append("\n"));

        return sb.toString();
    }
}
