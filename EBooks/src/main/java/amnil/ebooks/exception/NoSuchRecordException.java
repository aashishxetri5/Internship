package amnil.ebooks.exception;

public class NoSuchRecordException extends RuntimeException {
    public NoSuchRecordException(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}
