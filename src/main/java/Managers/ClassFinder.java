package Managers;

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
    public void loadClassesInDirectory(String directory, String packageName) {
        File dir = new File(directory);

        System.out.println(dir.getName());

        String _package;
        if (packageName == null) {
            if (dir.getName().equalsIgnoreCase("classes"))
                _package = null;
            else
                _package = dir.getName();
        } else {
            _package = packageName + "." + dir.getName();
        }


        List<File> content = Arrays.asList(dir.listFiles());

        content.forEach(f -> {

            if (f.isDirectory())
                loadClassesInDirectory(f.getAbsolutePath(), _package);

            if (f.getName().endsWith(".class")) {

                try {
                    URL url = f.toURI().toURL();
                    URL[] urls = new URL[]{url};

                    ClassLoader loader = new URLClassLoader(urls);
                    //loader.loadClass(_package.replace("classes\\.", "").replace("\\.class", "") + "." + f.getName());
                    System.out.println(url.getFile());
                    loader.loadClass("Managers.ClassFinder");
                } catch (MalformedURLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
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