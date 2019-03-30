package persistence.tables.inheritance;

import klass.Klass;
import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;

import java.util.List;

import static utils.ObjectToEntity.camelToSnake;

public class SingleTable implements InheritanceType {
    public String write(Klass parent, List<Klass> children, List<ForeignKey> fks) {
        StringBuilder sb = new StringBuilder();
        sb.append(name(parent));
        sb.append("{\n");
        sb.append(attributes(parent, children));
        sb.append("}\n");
        sb.append(foreignKeys(fks));

        return sb.toString();
    }

    private String foreignKeys(List<ForeignKey> fks) {
        StringBuilder sb = new StringBuilder();

        String nullable = "{FROM} }o--o{ {TO}";
        String notNull = "{FROM} }|--|{ {TO}";

        fks.forEach(fk -> {
            if (fk.isNullable())
                sb.append(nullable.replace("{FROM}", fk.getOriginTable()).replace("{TO}", fk.getDestinationTable()));
            else
                sb.append(notNull.replace("{FROM}", fk.getOriginTable()).replace("{TO}", fk.getDestinationTable()));
        });

        return null;
    }

    private String name(Klass parent) {
        String entity = "entity {ENTITY} {\n";
        return entity.replace("{ENTITY}", camelToSnake(parent.getName()));
    }

    private String attributes(Klass parent, List<Klass> children) {
        StringBuilder sb = new StringBuilder();

        //TODO: remove transients
        parent.getAttributes().forEach(a -> sb.append(new TableAttribute(camelToSnake(a.getName()), a.getKlass())).append("\n"));
        children.forEach(c -> c.getAttributes().forEach(a -> {
            sb.append(new TableAttribute(camelToSnake(a.getName()), a.getKlass())).append("\n");
        }));

        return sb.toString();
    }
}