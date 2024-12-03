package bank.exceptions;

public class TransactionAttributeException extends RuntimeException{
    public TransactionAttributeException(String message) {
        super("Typ Fehler:" + message);
    }
}
