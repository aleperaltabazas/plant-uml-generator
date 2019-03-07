package exceptions;

import persistence.ForeignKey;

public class NoFKTypeException extends RuntimeException {
    private ForeignKey fk;

    public NoFKTypeException(ForeignKey fk) {
        super("Foreign Key " + fk.getName() + " had no type asocciated.");
        this.fk = fk;
    }
}
