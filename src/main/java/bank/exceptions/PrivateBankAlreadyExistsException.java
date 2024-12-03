package bank.exceptions;

public class PrivateBankAlreadyExistsException extends Exception{
    public PrivateBankAlreadyExistsException(){
        super("Bank already exists");
    }
}
