package klass.objekt;

import klass.Klass;
import klass.Modifier;
import klass.classtype.ConcreteKlass;

import java.util.ArrayList;
import java.util.Arrays;

public class Objekt extends Klass {
    private static volatile Objekt instance;

    public Objekt() {
        super(new ArrayList<>(), new ArrayList<>(), "Object", new ConcreteKlass(), null, new ArrayList<>(),
                Arrays.asList(Modifier.Public), new ArrayList<>());
    }

    public static synchronized Objekt getInstance() {
        if (instance == null) instance = new Objekt();

        return instance;
    }

    @Override
    public boolean isIgnorable() {
        return false;
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