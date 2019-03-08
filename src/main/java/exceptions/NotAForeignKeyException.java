package exceptions;

import klass.Attribute;

public class NotAForeignKeyException extends RuntimeException {
    public NotAForeignKeyException(Attribute attribute) {
        super(attribute.getName() + " is not a Foreign Key.");
    }
}
