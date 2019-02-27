package parsing;

import klass.Attribute;
import klass.Klass;
import klass.Method;

import java.util.List;

public class UMLMaker {
    public String makeClassUml(Klass klass) {
        StringBuilder sb = new StringBuilder();

        sb.append(appendHeader(klass));
        sb.append(" {");
        sb.append(appendAttributes(klass));
        sb.append(appendMethods(klass));
        sb.append("}");
        sb.append(appendReferences(klass));

        return sb.toString();
    }

    private String appendReferences(Klass klass) {
        StringBuilder sb = new StringBuilder();
        klass.getAttributes().stream().filter(attr -> !attr.isPrimitive()).forEach(attr -> sb.append(klass.getName()).append(" --> ").append(attr.getKlass()));

        return sb.toString();
    }

    private String appendAttributes(Klass klass) {
        List<Attribute> attributes = klass.getAttributes();
        StringBuilder sb = new StringBuilder();

        attributes.stream().filter(attr -> attr.isVisible() || klass.hasGetterFor(attr.getName())).forEach(attr -> sb.append(attr.getName()).append(": ").append(attr.getKlass()));

        return sb.toString();
    }

    private String appendMethods(Klass klass) {
        List<Method> methods = klass.getMethods();
        StringBuilder sb = new StringBuilder();

        methods.stream().filter(Method::isVisible).forEach(met -> {
            sb.append(met.getName()).append("(");
            met.getArguments().forEach(arg -> sb.append(arg.getName()).append(": ").append(arg.getKlass()));
            sb.append("): ").append(met.getReturnType());
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
            klass.getInterfaces().forEach(i -> {
                sb.append(", ").append(i);
            });
        }

        return sb.toString();
    }
}