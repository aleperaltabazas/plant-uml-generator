package klass.objekt;

import klass.Klass;
import klass.Modifier;
import klass.classtype.ConcreteKlass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Objekt extends Klass {
    private static volatile Objekt instance;

    public Objekt() {
        super(new ArrayList<>(), new ArrayList<>(), "Object", new ConcreteKlass(), new ArrayList<>(), null,
                Arrays.asList(Modifier.Public), new ArrayList<>());
    }

    @Override
    public Optional<String> getParent() {
        return Optional.of(this.getName());
    }

    public static synchronized Objekt getInstance() {
        if (instance == null) instance = new Objekt();

        return instance;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = super.equals(o);
        return equals;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}