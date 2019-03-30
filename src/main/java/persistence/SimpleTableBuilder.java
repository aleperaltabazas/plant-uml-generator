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

import static utils.ObjectToEntity.camelToSnake;

public class SimpleTableBuilder implements TableBuilder {
    private Klass entity;
    private String tableName;
    private String primaryKey;
    private List<ForeignKey> foreignKeys;
    private List<TableAttribute> attributes;

    public SimpleTable build() {
        checkNull();
        return new SimpleTable(entity, primaryKey, attributes, foreignKeys);
    }

    private void checkNull() {
        if (entity == null || primaryKey == null)
            throw new BuildError("Need parameters to build. tableName: " + tableName + ", primaryKey: " + primaryKey);

        if (attributes == null) attributes = new ArrayList<>();
        if (foreignKeys == null) foreignKeys = new ArrayList<>();
    }

    public TableBuilder parse(Klass klass, List<Klass> others) {
        if (klass.isIgnorable())
            return new InheritanceTableBuilder().parse(klass, others);

        this.entity = klass;
        parseName(klass);
        parseAttributes(klass);
        return this;
    }

    public TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys) {
        if (this.foreignKeys == null) this.foreignKeys = new ArrayList<>();

        List<ForeignKey> localOriginFKs =
                foreignKeys.stream().filter(fk -> fk.getOriginTable().equals(entity.getName())).collect(Collectors.toList());

        this.foreignKeys.addAll(localOriginFKs);
        return this;
    }

    private void parseAttributes(Klass klass) {
        List<Attribute> klassAttributes = filterTransients(klass);
        List<Attribute> allAtributes = klass.allAtributes();
        parseSimpleAttributes(klassAttributes);
        parsePrimaryKey(klass, allAtributes);

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

    private FKType parseRelationType(List<String> annotations) {
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