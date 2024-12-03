package bank.exceptions;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String account){
        super ("Account '" + account + "' existiert bereits");
    }
}
