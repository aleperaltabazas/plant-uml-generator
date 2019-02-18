package some.klass;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KlassFactory {
    public static Klass makeClassFromText(String text) {
        KlassFactory factory = new KlassFactory();
        String klassHeader = factory.klassDefinition(text);


        Pattern pattern = Pattern.compile("public (abstract|) class(\\s*)\\w* ");

        return null;
    }

    public String klassDefinition(String text) {
        Pattern pattern = Pattern.compile("public( abstract|) class(\\s*)\\w*(\\s*)(extends \\w*|)(\\s*)(implements \\w*((\\s*),(\\s*)\\w*)*|)(\\s*)(\\{|\n)");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new RuntimeException("no class found");
        }

        return matcher.group(0).replace('{', ' ');
    }

    public static Klass simpleClass(String name) {
        return new Klass(Collections.emptyList(), Collections.emptyList(), name, ClassType.Concrete, Objekt.getInstance());
    }

    public static Klass simpleAbstractClass(String name) {
        return new Klass(Collections.emptyList(), Collections.emptyList(), name, ClassType.Abstract, Objekt.getInstance());
    }
}
