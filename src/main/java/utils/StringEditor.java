package utils;

public class StringEditor {
    public static String removeListWrapper(String str) {
        int lastIndexOf = str.lastIndexOf('>');
        StringBuilder sb = new StringBuilder(str);
        sb.replace(lastIndexOf, lastIndexOf + 1, "");

        return sb.toString().replaceFirst("<", "").replaceFirst("(List|Set|Map)", "");
    }
}
