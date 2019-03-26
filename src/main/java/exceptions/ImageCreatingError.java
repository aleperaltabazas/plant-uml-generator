package exceptions;

import java.io.IOException;

public class ImageCreatingError extends RuntimeException {
    public ImageCreatingError(IOException e) {
        super(e);
    }
}
