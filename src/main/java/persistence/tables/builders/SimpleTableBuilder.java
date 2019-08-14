package persistence.tables.builders;

import exceptions.BuildError;
import exceptions.MultiplePrimaryKeyError;
import exceptions.NoFkFoundException;
import exceptions.NoPrimaryKeyError;
import io.vavr.collection.List;
import klass.Attribute;
import klass.Klass;
import persistence.attributes.FKType;
import persistence.attributes.ForeignKey;
import persistence.attributes.TableAttribute;
import persistence.tables.RegularTable;

import static utils.ObjectToEntity.camelToSnake;

public class SimpleTableBuilder implements TableBuilder {
    private Klass entity;
    private String tableName;
    private String primaryKey;
    private List<ForeignKey> foreignKeys;
    private List<TableAttribute> attributes;

    public RegularTable build() {
        checkNull();
        return new RegularTable(entity, primaryKey, attributes, foreignKeys);
    }

    private void checkNull() {
        if (entity == null || primaryKey == null)
            throw new BuildError("Need parameters to build. tableName: " + tableName + ", primaryKey: " + primaryKey);

        if (attributes == null) attributes = List.empty();
        if (foreignKeys == null) foreignKeys = List.empty();
    }

    public TableBuilder parse(Klass klass, List<Klass> others) {
        if (klass.isIgnorable()) {
            return new InheritanceTableBuilder().parse(klass, others);
        }

        this.entity = klass;
        parseName(klass);
        parseAttributes(klass);
        return this;
    }

    public TableBuilder takeForeignKeys(List<ForeignKey> foreignKeys) {
        if (this.foreignKeys == null) this.foreignKeys = List.empty();

        List<ForeignKey> localOriginFKs =
                foreignKeys.filter(fk -> fk.getOriginTable().equalsIgnoreCase(entity.getName()));

        this.foreignKeys = this.foreignKeys.appendAll(localOriginFKs);
        return this;
    }

    private void parseAttributes(Klass klass) {
        List<Attribute> klassAttributes = filterTransients(klass);
        List<Attribute> allAtributes = klass.allAtributes();
        parseSimpleAttributes(klassAttributes);
        parsePrimaryKey(klass, allAtributes);
    }

    private List<Attribute> filterTransients(Klass klass) {
        return klass.getAttributes().filter(attr -> !attr.isTransient());
    }

    private void parsePrimaryKey(Klass klass, List<Attribute> klassAttributes) {
        List<Attribute> possiblePks =
                klassAttributes.filter(attribute -> attribute.isPrimaryKey());

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
        this.attributes = klassAttributes
                .filter(a -> !(a.isPrimaryKey() || a.isForeignKey()))
                .map(a -> new TableAttribute(a.getName(), a.getKlass()));
    }

    private void parseName(Klass klass) {
        this.tableName = camelToSnake(klass.getName());
    }

    private FKType parseRelationType(List<String> annotations) {
        if (annotations.contains("@OneToOne"))
            return FKType.ONE_TO_ONE;
        if (annotations.contains("@OneToMany"))
            return FKType.ONE_TO_MANY;
        if (annotations.contains("@ManyToOne"))
            return FKType.MANY_TO_ONE;
        if (annotations.contains("@ManyToMany"))
            return FKType.MANY_TO_MANY;

        throw new NoFkFoundException();
    }
}