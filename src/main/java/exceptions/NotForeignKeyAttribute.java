package exceptions;

import klass.Attribute;

public class NotForeignKeyAttribute extends RuntimeException {
    public NotForeignKeyAttribute(Attribute attribute) {
        super(attribute.getName() + " is not a Foreign Key.");
    }
}
