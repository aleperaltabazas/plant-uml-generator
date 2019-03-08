package parsing;

import exceptions.NoFKTypeException;
import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.Modifier;
import klass.classtype.EnumKlass;
import klass.classtype.Interfase;
import persistence.FKType;
import persistence.ForeignKey;
import persistence.Table;
import utils.ObjectToEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.StringEditor.removeListWrapper;

public class UMLMaker {
    public List<String> writeERD(Table table, List<ForeignKey> fks) {
        StringBuilder sb = new StringBuilder();
        sb.append(appendTableHeader(table));
        sb.append("{\n");
        sb.append(appendTableAttributes(table));
        sb.append("}\n");

        return Arrays.asList(sb.toString().split("\\r?\\n"));
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
            case OneToOne:
                relation =
                        ObjectToEntity.camelToSnake(fk.getOriginTable()) + " |o--o| " + ObjectToEntity.camelToSnake(fk.getDestinationTable());
                break;
            case OneToMany:
                relation =
                        ObjectToEntity.camelToSnake(fk.getOriginTable()) + " |o--o{ " + ObjectToEntity.camelToSnake(fk.getDestinationTable());
                break;
            case ManyToOne:
                relation =
                        ObjectToEntity.camelToSnake(fk.getOriginTable()) + " }o--o| " + ObjectToEntity.camelToSnake(fk.getDestinationTable());
                break;
            case ManyToMany:
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

        ForeignKey originConnection = new ForeignKey("id_" + fk.getOriginTable(), FKType.ManyToOne, tableName,
                ObjectToEntity.camelToSnake(fk.getOriginTable()));
        ForeignKey destinationConnection = new ForeignKey("id_" + fk.getDestinationTable(), FKType.ManyToOne,
                tableName, ObjectToEntity.camelToSnake(fk.getDestinationTable()));

        return new Table(tableName, null, new ArrayList<>(), Arrays.asList(originConnection, destinationConnection));
    }

    public List<String> writeClassDiagram(Klass klass) {
        StringBuilder sb = new StringBuilder();
        sb.append(appendHeader(klass));
        sb.append("{\n");
        if (klass.getClassType() instanceof EnumKlass) sb.append(appendEnumConstants(klass));
        sb.append(appendAttributes(klass));
        sb.append(appendMethods(klass));
        sb.append("}\n");
        sb.append(appendReferences(klass));

        return Arrays.asList(sb.toString().split("\\r?\\n"));
    }

    private String appendEnumConstants(Klass klass) {
        return klass.getClassType().enumConstants() + "\n";
    }

    private String appendReferences(Klass klass) {
        String collectionRegex = "(List<.*>|Set<.*>|Map<.*,.*>)";

        StringBuilder sb = new StringBuilder();
        klass.getAttributes().stream().filter(attr -> !attr.isIgnored()).forEach(attr -> {
            if (attr.getKlass().matches(collectionRegex)) {
                sb.append(klass.getName()).append(" --> \"*\" ").append(removeListWrapper(attr.getKlass())).append(
                        "\n");
            } else {
                sb.append(klass.getName()).append(" --> ").append(attr.getKlass()).append("\n");
            }
        });

        return sb.toString();
    }

    private String appendAttributes(Klass klass) {
        List<Attribute> attributes = klass.getAttributes();
        StringBuilder sb = new StringBuilder();

        attributes.stream().filter(attr -> attr.isVisible() || klass.hasGetterFor(attr.getName())).forEach(attr -> sb.append(attr.getName()).append(": ").append(attr.getKlass()).append("\n"));

        return sb.toString();
    }

    private String appendMethods(Klass klass) {
        List<Method> methods = klass.getMethods();
        StringBuilder sb = new StringBuilder();

        methods.stream().filter(method -> (method.isVisible() && !method.isBoilerPlate()) || (klass.getClassType() instanceof Interfase && !method.hasModifier(Modifier.Private))).forEach(met -> {
            sb.append(met.getName()).append("(");
            met.getArguments().forEach(arg -> sb.append(arg.getName()).append(": ").append(arg.getKlass()));
            sb.append("): ").append(met.getReturnType()).append("\n");
        });

        return sb.toString();
    }

    private String appendHeader(Klass klass) {
        StringBuilder sb = new StringBuilder();

        sb.append(klass.getClassType().javaDefinition()).append(klass.getName());

        if (klass.getParent().isPresent()) {
            sb.append(" extends ").append(klass.getParent().get());
        }

        if (!klass.getInterfaces().isEmpty()) {
            sb.append(" implements ").append(klass.getInterfaces().get(0));
            klass.getInterfaces().stream().skip(1).forEach(i ->
                    sb.append(", ").append(i)
            );
        }

        return sb.toString();
    }
}