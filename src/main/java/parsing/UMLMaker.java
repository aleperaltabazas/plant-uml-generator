package parsing;

import klass.Attribute;
import klass.Klass;
import klass.Method;
import klass.Modifier;
import klass.classtype.EnumKlass;
import klass.classtype.Interfase;

import java.util.Arrays;
import java.util.List;

public class UMLMaker {
    public List<String> writeClassDiagram(Klass klass) {
        StringBuilder sb = new StringBuilder();
        sb.append(klassDefinition(klass));
        sb.append(" { \n");
        if (klass.getClassType() instanceof EnumKlass) sb.append(enumConstants(klass));
        sb.append(usableKlassAttributes(klass));
        sb.append(usableKlassMethods(klass));
        sb.append("} \n");
        sb.append(klassReferences(klass));

        return Arrays.asList(sb.toString().split("\\r?\\n"));
    }

    private String enumConstants(Klass klass) {
        return klass.getClassType().enumConstants() + "\n";
    }

    private String klassReferences(Klass klass) {
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

    private String removeListWrapper(String klass) {
        int lastIndexOf = klass.lastIndexOf('>');
        StringBuilder sb = new StringBuilder(klass);
        sb.replace(lastIndexOf, lastIndexOf + 1, "");

        return sb.toString().replaceFirst("<", "").replaceFirst("(List|Set|Map)", "");
    }

    private String usableKlassAttributes(Klass klass) {
        List<Attribute> attributes = klass.getAttributes();
        StringBuilder sb = new StringBuilder();

        attributes.stream().filter(attr -> attr.isVisible() || klass.hasGetterFor(attr.getName())).forEach(attr ->
                sb.append(attr.getName()).append(": ").append(attr.getKlass()).append("\n"));

        return sb.toString();
    }

    private String usableKlassMethods(Klass klass) {
        List<Method> methods = klass.getMethods();
        StringBuilder sb = new StringBuilder();

        methods.stream().filter(method -> (method.isVisible() && !method.isBoilerPlate()) || (klass.getClassType()
                instanceof Interfase && !method.hasModifier(Modifier.Private))).forEach(met -> {
            sb.append(met.getName()).append("(");
            met.getArguments().forEach(arg -> sb.append(arg.getName()).append(": ").append(arg.getKlass()));
            sb.append("): ").append(met.getReturnType()).append("\n");
        });

        return sb.toString();
    }

    private String klassDefinition(Klass klass) {
        StringBuilder sb = new StringBuilder();

        String withNameAndDefinition = klass.getClassType().javaDefinition() + " " + klass.getName() + " ";

        if (klass.getParent().isPresent()) {
            sb.append(withNameAndDefinition).append("extends ").append(klass.getParent().get()).append("\n");
        }

        if (!klass.getInterfaces().isEmpty()) {
            klass.getInterfaces().forEach(i -> sb.append(withNameAndDefinition).append("implements ").append(i).append("\n"));
        }

        return sb.append(withNameAndDefinition).toString().replaceAll("[{]", "");
    }
}