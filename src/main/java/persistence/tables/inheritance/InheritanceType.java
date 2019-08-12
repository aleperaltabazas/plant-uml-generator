package persistence.tables.inheritance;

import exceptions.NoSuchStrategyException;
import klass.Klass;
import persistence.attributes.ForeignKey;

import java.util.List;

public interface InheritanceType {
    static InheritanceType parse(String inheritanceStrategy) {
        if (!inheritanceStrategy.contains("(")) return new SingleTable();

        String strategy = inheritanceStrategy.substring(inheritanceStrategy.indexOf('('),
                inheritanceStrategy.lastIndexOf(')')).replaceAll("strategy\\s?=\\s?(InheritanceType.)?", "");

        if (strategy.matches("SINGLE_TABLE"))
            return new SingleTable();
        else if (strategy.matches("JOINED"))
            return new JoinedTable();
        else if (strategy.matches("TABLE_PER_CLASS"))
            return new TablePerClass();

        throw new NoSuchStrategyException(strategy);
    }

    default String write(Klass parent, List<Klass> children, List<ForeignKey> foreignKeys) {
        return "";
    }
}