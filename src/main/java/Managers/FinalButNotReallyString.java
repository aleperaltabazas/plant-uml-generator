package Managers;

public class FinalButNotReallyString {
    private String text = "";

    public void concat(String otherString) {
        this.text = text.concat(otherString);
    }

    public String getText() {
        return text;
    }
}
