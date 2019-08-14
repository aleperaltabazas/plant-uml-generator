package klass.objekt;

import io.vavr.collection.List;
import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.Modifier;
import klass.classtype.ConcreteKlass;

public class ObjectClass extends Klass {
    private static ObjectClass instance;

    public ObjectClass() {
        super(List.empty(), ObjectMethods.all(), "Object", new ConcreteKlass(), null, List.empty(),
                List.of(Modifier.PUBLIC), List.empty());
    }

    public static synchronized ObjectClass getInstance() {
        if (instance == null) instance = new ObjectClass();

        return instance;
    }

    @Override
    public boolean isIgnorable() {
        return false;
    }

    @Override
    public List<Method> getInheritableMethods() {
        return getMethods();
    }

    @Override
    public List<Attribute> getInheritableAttributes() {
        return getAttributes();
    }

    @Override
    public List<Method> inheritedMethods() {
        return getMethods();
    }

    @Override
    public List<Attribute> inheritedAttributes() {
        return getAttributes();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}