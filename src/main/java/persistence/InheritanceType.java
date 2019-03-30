package persistence;

import exceptions.NoSuchStrategyException;

public enum InheritanceType {
    SINGLE, JOINED, PERCLASS;

    public static InheritanceType parse(String inheritanceStrategy) {
        if (!inheritanceStrategy.contains("(")) return SINGLE;

        String strategy = inheritanceStrategy.substring(inheritanceStrategy.indexOf('('),
                inheritanceStrategy.lastIndexOf(')')).replaceAll("strategy\\s?=\\s?(InheritanceType.)?", "");

        if (strategy.matches("SINGLE_TABLE"))
            return SINGLE;
        else if (strategy.matches("JOINED"))
            return JOINED;
        else if (strategy.matches("TABLE_PER_CLASS"))
            return PERCLASS;

        throw new NoSuchStrategyException(strategy);
    }
}