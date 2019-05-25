package vector.exception;

public class VecFileException extends RuntimeException {
    public VecFileException(String message) {
        super(message);
    }

    public VecFileException() {
        super();
    }
}
