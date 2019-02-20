package klass;

import exceptions.NoClassDefinitionException;
import utils.FinalButNotReallyString;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Arrays;
import java.util.List;

public class KlassBuilder {
    private ClassType classType;
    private FinalButNotReallyString name = new FinalButNotReallyString();
    private Klass parent;
    private List<String> interfaces;

    public void addClassDefinition(String classDefinition) {
        if (classDefinition.contains("abstract class"))
            classType = ClassType.Abstract;
        else if (classDefinition.contains("interface"))
            classType = ClassType.Interface;
        else if (classDefinition.contains("class"))
            classType = ClassType.Concrete;
        else
            throw new NoClassDefinitionException();

        int nameStarting = classDefinition.indexOf(classType.javaDefinition()) + classType.javaDefinition().length() + 1;

        while (classDefinition.charAt(nameStarting) != ' ') {
            if (name == null) name = new FinalButNotReallyString();
            name.concat("" + classDefinition.charAt(nameStarting));
            nameStarting++;

            if (nameStarting == classDefinition.length()) break;

        }

        if (classDefinition.contains("extends")) {
            int superStarting = classDefinition.lastIndexOf("extends") + 2;

            String parentName = "";
            while (classDefinition.charAt(superStarting) != ' ') {
                parentName += classDefinition.charAt(superStarting);
                superStarting++;
            }

            parent = buildSimpleKlass(parentName);
        } else {
            parent = Objekt.getInstance();
        }
    }

    public static Klass buildSimpleKlass(String name) {
        return new Klass(Arrays.asList(), Arrays.asList(), name, ClassType.Concrete, Objekt.getInstance());
    }

    public ClassType getClassType() {
        return classType;
    }

    public String getName() {
        return name.getText();
    }

    public Klass getParent() {
        return parent;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }
}
