package parsing;

public class RegexConstants {
    public static final String KLASS_DEFINITION = "(public )?((abstract )?class|interface|enum) \\w+(<.*>)?( extends \\w+(<.*>)?)?( implements \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";
    //basically: public? class <className>( extends <parent>)?( implements <interface>, <interface>...)? {
    public static final String CLASS = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?class \\w+( extends \\w+)?( implements \\w+\\s?(,\\s?\\w+\\s?)*)?\\s?[{]";
    //basically: public? abstract class <className>( extends <parent>)?( implements <interface>, <interface>...)? {
    public static final String ABSTRACT = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?abstract class \\w+(<.*>)? (extends \\w+(<.*>)?)?( implements \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";
    //basically: public? interface <interfaceName>( extends <parentInterface>)? {
    public static final String INTERFACE = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?interface \\w+(<.*>)?( extends \\w+(<.*>)?\\s?(,\\s?\\w+(<.*>)?\\s?)*)?\\s?[{]";
    //basically: public? enum <enumName>( implements <interface>, <interface>...)?
    public static final String ENUM = "(\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?)*(public )?enum \\w+( implements \\w+\\s?(,\\s?\\w+\\s?)*)?\\s?[{]";
    public static final String ANNOTATION = "\\s*@\\w+([(].*[)])?(\\s|\n)?\\s?";
    public static final String METHOD = "\\s*(public |private |protected )?(static )?(\\w|[.]|<|>|,)+ \\w+\\s?[(].*[)]\\s?([{]?|;)\\s?";
    public static final String ATTRIBUTE = "\\s*(@\\w+([(].*[)])?\\s?\n?)*\\s*(public |protected |private )?(static )?(final )?\\w(\\w|[.]|<|>|,)*\\s\\w+( ?);";
    public static final String ENUM_CONSTANT = "(" + ANNOTATION + ")?" + "\\s*\\w+,?;?";
    public static final String ENUM_CONSTANT_WITH_BEHAVIOR = "(" + ANNOTATION + ")?" + "\\s*\\w+(\\s?)([(].*[)])?\\s?[{]?;?\n?";
}