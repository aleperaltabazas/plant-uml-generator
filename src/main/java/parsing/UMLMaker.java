package parsing;

import exceptions.NoFKTypeException;
import io.vavr.collection.List;
import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.classtype.EnumKlass;
import persistence.attributes.FKType;
import persistence.attributes.ForeignKey;
import persistence.tables.MiddleTable;
import persistence.tables.Table;
import utils.ObjectToEntity;

public class UMLMaker {
    public List<String> writeERD(Table table, List<ForeignKey> fks) {
        return List.of(table.write().split("\\r?\\n"));
    }

    private String appendTableAttributes(Table table) {
        StringBuilder sb = new StringBuilder();

        sb.append(table.getPk()).append("\n");
        sb.append("--\n");
        table.getAttributes().forEach(attr -> sb.append(attr.getName()).append("\n"));
        table.getFks().forEach(fk -> sb.append(fk.getName()).append("\n"));

        return sb.toString();
    }

    private String appendTableHeader(Table table) {
        return "entity " + table.getName();
    }

    public String appendRelations(List<ForeignKey> fks) {
        StringBuilder sb = new StringBuilder();

        for (ForeignKey fk : fks) {
            String relation = getRelation(fk);

            sb.append(relation).append("\n");
        }

        return sb.toString();
    }

    private String getRelation(ForeignKey fk) {
        String relation;

        switch (fk.getType()) {
            case ONE_TO_ONE:
                relation =
                        ObjectToEntity.camelToSnake(fk.getOriginTable()) + " |o--o| " + ObjectToEntity.camelToSnake(fk.getDestinationTable());
                break;
            case ONE_TO_MANY:
                relation =
                        ObjectToEntity.camelToSnake(fk.getOriginTable()) + " |o--o{ " + ObjectToEntity.camelToSnake(fk.getDestinationTable());
                break;
            case MANY_TO_ONE:
                relation =
                        ObjectToEntity.camelToSnake(fk.getOriginTable()) + " }o--o| " + ObjectToEntity.camelToSnake(fk.getDestinationTable());
                break;
            case MANY_TO_MANY:
                Table middleTable = middleTable(fk);
                relation = appendMiddleTable(middleTable);
                break;
            default:
                throw new NoFKTypeException(fk);
        }

        return relation;
    }

    private String appendMiddleTable(Table middleTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("entity ").append(middleTable.getName()).append("{\n");
        middleTable.getFks().forEach(fk -> sb.append(fk.getName()).append("\n"));
        sb.append("--\n");
        sb.append("}\n");

        middleTable.getFks().forEach(fk -> sb.append(fk.getOriginTable()).append(" }o--o| ")
                .append(fk.getDestinationTable()).append("\n"));

        return sb.toString();
    }

    private Table middleTable(ForeignKey fk) {
        String tableName = ObjectToEntity.camelToSnake(fk.getOriginTable() + "" + fk.getDestinationTable());

        ForeignKey originConnection = new ForeignKey("id_" + fk.getOriginTable(), FKType.MANY_TO_ONE, tableName,
                ObjectToEntity.camelToSnake(fk.getOriginTable()), false);
        ForeignKey destinationConnection = new ForeignKey("id_" + fk.getDestinationTable(), FKType.MANY_TO_ONE,
                tableName, ObjectToEntity.camelToSnake(fk.getDestinationTable()), false);

        return new MiddleTable(tableName, List.of(originConnection,
                destinationConnection));
    }


    public List<String> writeClassDiagram(Klass klass) {
        StringBuilder sb = new StringBuilder();
        sb.append(klassDefinition(klass));
        sb.append(" { \n");
        if (klass.getClassType() instanceof EnumKlass) sb.append(enumConstants(klass));
        sb.append(usableKlassAttributes(klass));
        sb.append(usableKlassMethods(klass));
        sb.append("} \n");
        sb.append(klassReferences(klass));

        return List.of(sb.toString().split("\\r?\\n"));
    }

    private String enumConstants(Klass klass) {
        return klass.getClassType().enumConstants() + "\n";
    }

    private String klassReferences(Klass klass) {
        String collectionRegex = "(List<.*>|Set<.*>|Map<.*,.*>)";

        StringBuilder sb = new StringBuilder();
        klass.getAttributes().filter(attr -> !attr.isIgnored()).forEach(attr -> {
            if (attr.getKlass().matches(collectionRegex)) {
                sb.append(klass.getName()).append(" --> \"*\" ").append(removeListWrapper(attr.getKlass())).append(
                        "\n");
            } else {
                sb.append(klass.getName()).append(" --> ").append(attr.getKlass()).append("\n");
            }
        });

        return sb.toString();
    }

    private String removeListWrapper(String klass) {
        int lastIndexOf = klass.lastIndexOf('>');
        StringBuilder sb = new StringBuilder(klass);
        sb.replace(lastIndexOf, lastIndexOf + 1, "");

        return sb.toString().replaceFirst("<", "").replaceFirst("(List|Set|Map)", "");
    }

    private String usableKlassAttributes(Klass klass) {
        List<Attribute> attributes = klass.getAttributes();
        StringBuilder sb = new StringBuilder();

        attributes.filter(attr -> attr.isVisible() || klass.hasGetterFor(attr.getName())).forEach(attr ->
                sb.append(attr.getName()).append(": ").append(attr.getKlass()).append("\n"));

        return sb.toString();
    }

    private String usableKlassMethods(Klass klass) {
        List<Method> methods = klass.getMethods();
        StringBuilder sb = new StringBuilder();

        methods.filter(method -> method.isVisible() && !(method.isBoilerPlate() || klass.inherits(method))).forEach(met -> {
            sb.append(met.getName()).append("(");
            met.getArguments().forEach(arg -> {
                if (met.getArguments().indexOf(arg) > 0) {
                    sb.append(", ");
                }
                sb.append(arg.getName()).append(": ").append(arg.getKlass());
            });
            sb.append("): ").append(met.getReturnType()).append("\n");
        });

        return sb.toString();
    }

    private String klassDefinition(Klass klass) {
        StringBuilder sb = new StringBuilder();

        String withNameAndDefinition = klass.getClassType().javaDefinition() + " " + klass.getName() + " ";

        if (klass.hasParent()) {
            sb.append(withNameAndDefinition).append("extends ").append(klass.getSuperKlass().getName()).append("\n");
        }

        if (!klass.getInterfaces().isEmpty()) {
            klass.getInterfaces().forEach(i -> sb.append(withNameAndDefinition).append("implements ").append(i).append("\n"));
        }

        return sb.append(withNameAndDefinition).toString().replaceAll("[{]", "");
    }
}