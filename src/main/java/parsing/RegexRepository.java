package parsing;

public class RegexRepository {
    public static final String klassDefinitionRegex = "(public )?((abstract )?class|interface|enum) \\w+(<.*>)?( extends \\w+(<.*>)?)?( implements \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";//basically: (public )?class <className>( extends <parent>)?( implements <interface>, <interface>...)? {
    public static final String classRegex = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?class \\w+( extends \\w+)?( implements \\w+\\s?(,\\s?\\w+\\s?)*)?\\s?[{]";//basically: (public )?abstract class <className>( extends <parent>)?( implements <interface>, <interface>...)? {
    public static final String abstractRegex = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?abstract class \\w+(<.*>)? (extends \\w+(<.*>)?)?( implements \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";//basically: (public )?interface <interfaceName>( extends <parentInterface>)? {
    public static final String interfaceRegex = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?interface \\w+(<.*>)?( extends \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";//basically: (public )?enum <enumName>( implements <interface>, <interface>...)?
    public static final String enumRegex = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?enum \\w+( implements \\w+\\s?(,\\s?\\w+\\s?)*)?\\s?[{]";
    public static final String annotationRegex = "\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?";
    public static final String methodRegex = "\\s*(public |private |protected )?(static )?(\\w|[.]|<|>|,)+ \\w+\\s?[(].*[)]\\s?([{]?|;)\\s?";
    public static final String attributeRegex = "\\s*(@\\w+([(].*[)])?\\s?\n?)*\\s*(public |protected |private )?(static )?(final )?\\w(\\w|[.]|<|>|,)*\\s\\w+( ?);";
    public static final String enumConstantRegex = "(" + annotationRegex + ")?" + "\\s*\\w+,?;?";
    public static final String enumConstantWithBehaviorRegex = "(" + annotationRegex + ")?" + "\\s*\\w+(\\s?)([(].*[)])?\\s?[{]?;?\n?";
}