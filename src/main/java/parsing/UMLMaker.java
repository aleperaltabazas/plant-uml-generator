package parsing;

import klass.Attribute;
import klass.Klass;
import klass.Method;

import java.util.Arrays;
import java.util.List;

public class UMLMaker {
    public List<String> makeClassUml(Klass klass) {
        StringBuilder sb = new StringBuilder();

        sb.append(appendHeader(klass));
        sb.append(" {" + "\n");
        sb.append(appendAttributes(klass));
        sb.append(appendMethods(klass));
        sb.append("}" + "\n");
        sb.append(appendReferences(klass));

        return Arrays.asList(sb.toString().split("\\r?\\n"));
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

        methods.stream().filter(method -> method.isVisible() && !method.isBoilerPlate()).forEach(met -> {
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
            klass.getInterfaces().forEach(i -> System.out.println(klass.getName() + " implements " + i));
            klass.getInterfaces().forEach(i -> {
                if (klass.getInterfaces().indexOf(i) > 0)
                    sb.append(", ").append(i);
            });
        }

        return sb.toString();
    }
}