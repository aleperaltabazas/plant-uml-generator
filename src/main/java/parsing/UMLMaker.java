package parsing;

import exceptions.MultiplePrimaryKeyError;
import exceptions.NoPrimaryKeyError;
import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.Modifier;
import klass.classtype.EnumKlass;
import klass.classtype.Interfase;
import persistence.Table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UMLMaker {
    public Table writeERD(Klass klass) {
        String pk = getPK(klass);

        return null;
    }

    private String getPK(Klass klass) {
        List<Attribute> posiblePks = klass.getAttributes().stream().filter(a -> a.getAnnotations().stream().anyMatch(an -> an.equalsIgnoreCase("entity"))).collect(Collectors.toList());

        if (posiblePks.size() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("There should only exist one Primary Key within class ").append(klass.getName()).append(". Found following PKs: ");
            sb.append(posiblePks.get(0));

            posiblePks.stream().skip(1).forEach(pk -> sb.append(", ").append(pk));

            throw new MultiplePrimaryKeyError(sb.toString());
        }

        if (posiblePks.isEmpty()) {
            throw new NoPrimaryKeyError(klass.getName());
        }

        return posiblePks.get(0).getName();
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
                sb.append(klass.getName()).append(" --> \"*\" ").append(removeListWrapper(attr.getKlass())).append("\n");
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