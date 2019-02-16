import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class Reader {
    public void read(Class klazz) {
        final FinalButNotReallyString classDescription = new FinalButNotReallyString();
        final FinalButNotReallyString classReferences = new FinalButNotReallyString();

        if (klazz.isInterface()) {
            classDescription.concat("interface ");
        } else if (Modifier.isAbstract(klazz.getModifiers())) {
            classDescription.concat("abstract class ");
        } else {
            classDescription.concat("class ");
        }

        classDescription.concat(klazz.getSimpleName() + "{\n");

        List<Field> fields = Arrays.asList(klazz.getDeclaredFields());

        fields.forEach(f -> {
            classDescription.concat(f.getName() + ": " + f.getType().getSimpleName() + "\n");
            if (!(f.getType().isPrimitive() || isQuasiPrimitive(f.getType())))
                classReferences.concat((klazz.getSimpleName() + " --> " + f.getType() + "\n").replace("class", ""));
        });

        List<Method> methods = Arrays.asList(klazz.getDeclaredMethods());

        methods.forEach(m -> {
            if (Modifier.isPublic(m.getModifiers())) {
                classDescription.concat(m.getName() + "(");

                List<Parameter> parameters = Arrays.asList(m.getParameters());
                parameters.forEach(p -> {
                    classDescription.concat(p.getName() + ": " + p.getType());
                    if (parameters.indexOf(p) != (parameters.size() - 1))
                        classDescription.concat(", ");
                });

                classDescription.concat("): " + m.getReturnType().getSimpleName() + "\n");
            }
        });

        classDescription.concat("}");
        System.out.println(classDescription.getText());
        System.out.println(classReferences.getText());
    }

    private boolean shouldIgnoreMethod(Method m) {
        return false;
    }

    private boolean isQuasiPrimitive(Class<?> type) {
        List<Class> quasiPrimitives = Arrays.asList(Long.class, Integer.class, Float.class, String.class, Double.class);
        return quasiPrimitives.contains(type);
    }
}
