package bank.exceptions;

public class TransactionDoesNotExistException extends Exception{
    public TransactionDoesNotExistException(String transaction){
        super("Transaktion: " +transaction + " existiert nichts");
    }
}
