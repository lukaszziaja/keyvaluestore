package exceptions;

public class NamespaceDoesNotExistException extends RuntimeException {

    public NamespaceDoesNotExistException() {
        super("Namespace with that name doesn't exist");
    }
}
