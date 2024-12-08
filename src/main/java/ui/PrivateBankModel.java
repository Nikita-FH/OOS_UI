package ui;

import bank.PrivateBank;

/**
 * Model das die Bank Methode hält
 */
public final class PrivateBankModel {
    /**
     * BDie bank die gespeichert werden soll
     */
    private PrivateBank bank;
    /**
     * Instance der Bank
     */
    private final static PrivateBankModel INSTANCE = new PrivateBankModel();

    /**
     * Private Constructor damit der nichts genutzt werden kann
     */
    private PrivateBankModel(){};

    /**
     * Gibt die Instance der Bank zurück die wir speichern
     * @return Instance der Bank
     */
    public static PrivateBankModel getInstance(){
        return INSTANCE;
    }

    /**
     * Gibt die Bank zurück
     * @return
     */
    public PrivateBank getBank(){
        return bank;
    }

    /**
     * Setzt die bank die gespeichert
     * @param bank
     */
    public void setBank(PrivateBank bank){
        this.bank = bank;
    }
    
}
