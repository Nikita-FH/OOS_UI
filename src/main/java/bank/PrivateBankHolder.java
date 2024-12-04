package bank;

import bank.exceptions.PrivateBankAlreadyExistsException;

import java.io.IOException;

public final class PrivateBankHolder {
        
    private PrivateBank bank;
    private final static PrivateBankHolder INSTANCE = new PrivateBankHolder();
    
    private PrivateBankHolder(){};
    
    public static PrivateBankHolder getInstance(){
        return INSTANCE;
    }

    public PrivateBank getBank(){
        return bank;
    }
    public void setBank(PrivateBank bank){
        this.bank = bank;
    }
    
}
