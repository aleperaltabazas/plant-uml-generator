import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Reader reader = new Reader();
        reader.read(TestClass.class);
    }
}
