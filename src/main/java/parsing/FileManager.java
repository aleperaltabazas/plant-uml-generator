package parsing;

import exceptions.NoSuchFileException;
import exceptions.WriteError;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    public Path writeFile(String fileName, List<String> lines) {
        Path file = Paths.get(System.getProperty("user.dir") + "/" + fileName);
        try {
            return Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new WriteError("Error writing file");
        }
    }

    public List<String> findAllClasses(String path) {
        File file = new File(path);
        List<String> all = new ArrayList<>();

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                all.addAll(findAllClasses(f.getAbsolutePath()));
            }
        } else {
            if (file.getName().endsWith(".java")) {
                try {
                    all.add(fileToText(file.getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    throw new NoSuchFileException(file.getAbsolutePath());
                }
            }
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
