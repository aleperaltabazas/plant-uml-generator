package persistence;

import exceptions.MultiplePrimaryKeyError;
import exceptions.NoPrimaryKeyError;
import klass.Attribute;
import klass.Klass;

import java.util.List;
import java.util.stream.Collectors;

public class TableFactory {
    public Table tableOf(Klass klass) {
        return null;
    }

    private String getPK(Klass klass) {
        List<Attribute> posiblePks =
                klass.getAttributes().stream().filter(a -> a.getAnnotations().stream().anyMatch(an -> an.equalsIgnoreCase("entity"))).collect(Collectors.toList());

        if (posiblePks.size() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("There should only exist one Primary Key within class ").append(klass.getName()).append(". " +
                    "Found following PKs: ");
            sb.append(posiblePks.get(0));

            posiblePks.stream().skip(1).forEach(pk -> sb.append(", ").append(pk));

            throw new MultiplePrimaryKeyError(sb.toString());
        }

        if (posiblePks.isEmpty()) {
            throw new NoPrimaryKeyError(klass.getName());
        }

        return posiblePks.get(0).getName();
    }
}