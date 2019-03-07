package persistence;

import exceptions.MultiplePrimaryKeyError;
import exceptions.NoFkFoundException;
import exceptions.NoPrimaryKeyError;
import klass.Attribute;
import klass.Klass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableManager {
    public Table tableOf(Klass klass, List<ForeignKey> fks) {
        String tableName = klass.getName();
        String pk = getPk(klass);
        List<TableAttribute> attributes = getAttributes(klass);
        List<ForeignKey> tableFks = getFks(klass, fks);

        return new Table(tableName, pk, attributes, tableFks);
    }

    private List<TableAttribute> getAttributes(Klass klass) {
        List<TableAttribute> attributes = getAttributes(klass);

        klass.getAttributes().forEach(attr -> attributes.add(new TableAttribute(attr.getName(), attr.getKlass())));

        return attributes;
    }

    private List<ForeignKey> getFks(Klass klass, List<ForeignKey> fks) {
        return fks.stream().filter(fk -> fk.getOriginTable().equalsIgnoreCase(klass.getName())).collect(Collectors.toList());
    }

    private String getPk(Klass klass) {
        return klass.getAttributes().stream().filter(attr -> attr.getAnnotations().contains("Id")).findFirst().get().getName();
    }

    public List<ForeignKey> readFKs(Klass klass) {
        List<ForeignKey> fks = new ArrayList<>();

        klass.getAttributes().stream().filter(attr -> {
            List<String> annotaions = attr.getAnnotations();
            return annotaions.contains("OneToOne") || annotaions.contains("OneToMany") || annotaions.contains(
                    "ManyToOne") || annotaions.contains("ManyToMany");
        }).forEach(attr -> fks.add(new ForeignKey(attr.getName(), parseType(attr.getAnnotations()), klass.getName(),
                attr.getKlass())));

        return fks;
    }

    private FKType parseType(List<String> annotations) {
        if (annotations.contains("OneToOne"))
            return FKType.OneToOne;
        if (annotations.contains("OneToMany"))
            return FKType.OneToMany;
        if (annotations.contains("ManyToOne"))
            return FKType.ManyToOne;
        if (annotations.contains("ManyToMany"))
            return FKType.ManyToMany;

        throw new NoFkFoundException();
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