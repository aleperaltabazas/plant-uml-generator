package Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PackageFinder {
    public List<Package> packagesFrom(String path) {
        List<Package> packages = new ArrayList<>();
        List<File> directories = subDirectoriesFrom(path);

        directories.forEach(dir -> {
            Package.getPackage(dir.getName().replace('/', '.').replace('\\', '.'));
        });

        return packages;
    }

    public List<File> subDirectoriesFrom(String path) {
        File baseDirectory = new File(path);

        if (!baseDirectory.isDirectory()) {
            throw new RuntimeException("base path should be a directory: " + baseDirectory.getAbsolutePath());
        }

        List<File> content = Arrays.asList(baseDirectory.listFiles());
        List<File> subDirectories = content.stream().filter(File::isDirectory).collect(Collectors.toList());
        subDirectories.forEach(subDir -> subDirectories.addAll(subDirectoriesFrom(subDir.getAbsolutePath())));

        return subDirectories;
    }
}
