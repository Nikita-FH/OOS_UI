package bank;

public final class PrivateBankModel {
        
    private PrivateBank bank;
    private final static PrivateBankModel INSTANCE = new PrivateBankModel();
    
    private PrivateBankModel(){};
    
    public static PrivateBankModel getInstance(){
        return INSTANCE;
    }

    public PrivateBank getBank(){
        return bank;
    }
    public void setBank(PrivateBank bank){
        this.bank = bank;
    }
    
}
