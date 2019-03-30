package exceptions;

import klass.Klass;

public class NotInheritableTable extends RuntimeException {
    public NotInheritableTable(Klass klass) {
        super(klass.getName() + " does not declare it's inheritance mapping.");
    }
}
