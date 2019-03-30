package exceptions;

import klass.Klass;

public class NoSuchStrategyException extends RuntimeException {
    public NoSuchStrategyException(Klass klass) {
        super(klass.getName() + " doesn't implement @Inheritance strategy.");
    }

    public NoSuchStrategyException(String strategy) {
        super(strategy + " is not a valid strategy for mapping.");
    }
}
