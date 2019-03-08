package persistence;

import exceptions.BuildError;
import exceptions.MultiplePrimaryKeyError;
import exceptions.NoFkFoundException;
import exceptions.NoPrimaryKeyError;
import klass.Attribute;
import klass.Klass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.SnakeCaser.camelToSnake;

public class TableBuilder {
    private Klass entity;
    private String tableName;
    private String primaryKey;
    private List<ForeignKey> foreignKeys;
    private List<TableAttribute> attributes;

    public Table build() {
        checkNull();
        return new Table(tableName, primaryKey, attributes, foreignKeys);
    }

    private void checkNull() {
        if (tableName == null || primaryKey == null)
            throw new BuildError("Need parameters to build. tableName: " + tableName + ", primaryKey: " + primaryKey);
    }

    public void parse(Klass klass) {
        this.entity = klass;
        parseName(klass);
        parseAttributes(klass);
    }

    public void takeForeignKeys(List<ForeignKey> foreignKeys) {
        if (this.foreignKeys == null) this.foreignKeys = new ArrayList<>();

        List<ForeignKey> localOriginFKs =
                foreignKeys.stream().filter(fk -> fk.getOriginTable().equals(entity.getInterfaces())).collect(Collectors.toList());

        this.foreignKeys.addAll(localOriginFKs);
    }

    private void parseAttributes(Klass klass) {
        List<Attribute> klassAttributes = filterTransients(klass);
        parseSimpleAttributes(klassAttributes);
        parsePrimaryKey(klass, klassAttributes);

    }

    private List<Attribute> filterTransients(Klass klass) {
        return klass.getAttributes().stream().filter(attr -> !attr.isTransient()).collect(Collectors.toList());
    }

    private void parsePrimaryKey(Klass klass, List<Attribute> klassAttributes) {
        List<Attribute> possiblePks =
                klassAttributes.stream().filter(attribute -> attribute.isPrimaryKey()).collect(Collectors.toList());

        if (possiblePks.size() > 1) {
            StringBuilder sb = new StringBuilder();
            possiblePks.forEach(pk -> sb.append(pk.getName() + "\n"));

            throw new MultiplePrimaryKeyError("Multiple PKs found for Class " + klass.getName() + ": " + sb.toString());
        }

        if (possiblePks.isEmpty()) {
            throw new NoPrimaryKeyError(klass.getName());
        }

        primaryKey = camelToSnake(possiblePks.get(0).getName());
    }

    private void parseSimpleAttributes(List<Attribute> klassAttributes) {
        this.attributes = new ArrayList<>();

        klassAttributes.stream().filter(attribute -> !(attribute.isPrimaryKey() || attribute.isForeignKey()))
                .forEach(attribute -> this.attributes.add(new TableAttribute(camelToSnake(attribute.getName()),
                        attribute.getKlass())));
    }

    private void parseName(Klass klass) {
        this.tableName = camelToSnake(klass.getName());
    }

    private FKType parseType(List<String> annotations) {
        if (annotations.contains("@OneToOne"))
            return FKType.OneToOne;
        if (annotations.contains("@OneToMany"))
            return FKType.OneToMany;
        if (annotations.contains("@ManyToOne"))
            return FKType.ManyToOne;
        if (annotations.contains("@ManyToMany"))
            return FKType.ManyToMany;

        throw new NoFkFoundException();
    }
}