package parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    public List<String> findAllClasses(String path) throws FileNotFoundException {
        File file = new File(path);
        List<String> all = new ArrayList<>();

        if (file.isDirectory()) {
            Arrays.asList(file.listFiles()).forEach(f -> {
                try {
                    all.addAll(findAllClasses(f.getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } else {
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
