package some.klass;

import exceptions.NoClassDefinitionException;

import java.util.List;

public class KlassFactory {
    private ClassType classType;
    private String name;

    public void addClassDefinition(String classDefinition) {
        if (classDefinition.contains("abstract class"))
            classType = ClassType.Abstract;
        else if (classDefinition.contains("interface"))
            classType = ClassType.Interface;
        else if (classDefinition.contains("class"))
            classType = ClassType.Concrete;
        else
            throw new NoClassDefinitionException();

        int nameStarting = classDefinition.lastIndexOf(classType.javaDefinition()) + 2;

        while (classDefinition.charAt(nameStarting) != ' ') {
            if (name == null) name = "";
            name += classDefinition.charAt(nameStarting);
            nameStarting++;
        }
    }
}
