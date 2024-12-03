package bank.exceptions;

public class AccountDoesNotExistException extends Exception
{
    public AccountDoesNotExistException(String name){
        super("Konto :" + name + "existiert nicht");
    }
}
