package bank.exceptions;

public class TransactionAlreadyExistException extends RuntimeException{
    public TransactionAlreadyExistException(String transaktion){
        super("Transaktion: "+transaktion+ "existiert bereits");
    }
}
