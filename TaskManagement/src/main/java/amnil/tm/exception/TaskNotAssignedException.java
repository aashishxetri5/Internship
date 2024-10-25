package amnil.tm.exception;

public class TaskNotAssignedException extends RuntimeException{
    public TaskNotAssignedException(String message) {
        super(message);
    }
}
