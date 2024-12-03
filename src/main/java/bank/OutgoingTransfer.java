package bank;

public class OutgoingTransfer extends Transfer {
    /**
     * Konstruktor fuer die KLasse {@link OutgoingTransfer}
     * @param date String Datum der des Transfers
     * @param amount double Menge des Transfers
     * @param descripton String Beschreibung des Transfers
     */
    public OutgoingTransfer(String date, double amount, String descripton) {
        super(date, amount, descripton);
    }

    /**
     * Konstruktor fuer die KLasse {@link OutgoingTransfer}
     * @param date String Datum der des Transfers
     * @param amount double Menge des Transfers
     * @param descripton String Beschreibung des Transfers
     * @param sender String der Sender des Transfers
     * @param recipient String Empfaenger des Transfers
     */
    public OutgoingTransfer(String date, double amount, String descripton, String sender, String recipient) {
        super(date, amount, descripton, sender, recipient);
    }

    /**
     * Gibt zurueck was man ueberweisen hat
     * @return double ueberwiesennes Geld
     */
    @Override
    public double calculateBill(){
        return -this.amount;
    }
}
