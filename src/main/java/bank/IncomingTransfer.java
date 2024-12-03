package bank;

public class IncomingTransfer extends Transfer{
    /**
     * Konstruktor fuer die KLasse {@link IncomingTransfer}
     * @param date String Datum der des Transfers
     * @param amount double Menge des Transfers
     * @param descripton String Beschreibung des Transfers
     */
    public IncomingTransfer(String date, double amount, String descripton) {
        super(date, amount, descripton);
    }

    /**
     * Konstruktor fuer die KLasse {@link IncomingTransfer}
     * @param date String Datum der des Transfers
     * @param amount double Menge des Transfers
     * @param descripton String Beschreibung des Transfers
     * @param sender String der Sender des Transfers
     * @param recipient String Empfaenger des Transfers
     */
    public IncomingTransfer(String date, double amount, String descripton, String sender, String recipient) {
        super(date, amount, descripton, sender, recipient);
    }
}
