package utils;

public class StringEditor {
    private static final String genericRegex = "\\w+<.*>";

    public static String removeListWrapper(String str) {
        int lastIndexOf = str.lastIndexOf('>');
        StringBuilder sb = new StringBuilder(str);
        sb.replace(lastIndexOf, lastIndexOf + 1, "");

        String unWrapped = sb.toString().replaceFirst("<", "").replaceFirst("(List|Set|Map)", "");
        if (unWrapped.matches(genericRegex))
            return removeListWrapper(unWrapped);

        return unWrapped;
    }
}
