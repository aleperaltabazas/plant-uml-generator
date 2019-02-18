import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ClassFinder {
    public void loadClassesInDirectory(String directory) {
        File dir = new File(directory);

        if (!dir.isDirectory()) {
            throw new RuntimeException("Should be a directory");
        }

        List<File> files = Arrays.asList(dir.listFiles());
        files.forEach(f -> {
            if (f.isDirectory())
                loadClassesInDirectory(f.getName());
        });

        try {
            URL url = dir.toURI().toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);

            cl.loadClass(dir.getName().replace('/', '.'));
        } catch (MalformedURLException | ClassNotFoundException e) {
        }
    }

    public Set<Class<?>> findClasses(Package _package) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(_package.getName()))));

        return reflections.getSubTypesOf(Object.class);
    }
}
