package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Klasse die Transaktion zwischen zwei Partein realisiert
 * @extends {@link Transaction}
 * @author Nikita Diser
 */
public class Transfer extends Transaction{
    /**
     * sender String Auftraggeber der Transaktion
     */
    private String sender;
    /**
     * recipient String Empfänger der Transaktion
     */
    private String recipient;

    /**
     * gibt {@link Transfer#sender} des Transfers zurück
     * @return String Auftraggeber des Transfers
     */
    public String getSender(){return this.sender;}

    /**
     * gibt {@link Transfer#recipient} des Transfers zurück
     * @return String Empfänger des Transfers
     */
    public String getRecipient(){return this.recipient;}

    /**
     * Setzt {@link Transfer#sender} des Transfers
     * @param sender String der Auftraggeber des Transfers
     */
    public void setSender(String sender){this.sender = sender;}

    /**
     * setzt {@link Transfer#recipient} des Transfers
     * @param recipient String Empfänger des Transfers
     */
    public void setRecipient(String recipient){this.recipient = recipient;}

    /**
     * setzt {@link Transfer#amount}des Transfers
     * @param amount double ist die Menge der Transaktion, muss größer 0 sein
     */
    @Override
    public void setAmount(double amount) throws TransactionAttributeException {
        if (amount > 0) {
            this.amount = amount;
        } else {
            throw new TransactionAttributeException("Amount must be greater than 0");
        }
    }

    /**
     * Constructor der Klasse {@link Transfer}, ohne Auftraggeber und Empfänger
     * @param date String Datum des Transfers
     * @param amount double Menge des Transfers
     * @param descripton String Beschreibung des Transfers
     */
    public Transfer(String date, double amount, String descripton) {
        super(date, amount, descripton);
    }

    /**
     * Constructor der Klasse {@link Transfer}
     * @param date String Datum des Transfers
     * @param amount double Menge des Transfers
     * @param descripton String Beschreibung des Transfers
     * @param sender String Auftraggeber der Transaktion
     * @param recipient String Empfänger der Transaktion
     */
    public Transfer(String date, double amount,String descripton, String sender, String recipient) {
        this (date, amount, descripton);
        this.recipient = recipient;
        this.sender = sender;
    }

    /**
     * Copy Constructor der Klasse {@link Transfer}
     * @param transfer Transfer das Objekt das kopiert werden soll
     */
    public Transfer(Transfer transfer) {
        this (transfer.date, transfer.amount, transfer.descripton, transfer.sender, transfer.recipient);
    }

    /**
     * Gibt {@link Transfer} als String wieder
     * @return String gibt {@link Payment} in Reihenfolge: {@link Transaction#date Datum};{@link Transaction#descripton Beschreibung};
     * {@link Transfer#calculateBill() Rechnung};{@link Transfer#sender Auftraggeber}; {@link Transfer#recipient Empfänger}
     */
    @Override
    public String toString(){
       return (super.toString() + " Sender:" + this.sender + " Recipient:" + this.recipient);
    }

    /**
     * Überprüft ob das Objekte der Klasse {@link Transfer} gleich sind
     * @param t {@link Transfer} Objekt das auf gleich überprüft werden soll zu this
     * @return gibt true zurück wenn this und Objekt gleich sind, sonst false
     */
    public boolean equals(Object t){
        if (t instanceof Transfer) {
            if(!super.equals(t)) return false;
            if(!this.recipient.equals(((Transfer)t).recipient)) return false;
            if(!this.sender.equals(((Transfer)t).sender)) return false;
        }
        else return false;

        return true;
    }
}
