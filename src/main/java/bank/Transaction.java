package bank;
/**
Abstrakte Klasse die wichtigsten Attribute und Methoden für alle Arten von Transaktionen
zur verfügung stellt
@author Nikita Diser
 @implements {@link CalculateBill}
 */
abstract public class Transaction implements CalculateBill, Comparable<Transaction> {
    /**date String ist Datum der Transaktion*/
    protected String date;
    /**amount double ist die Menge der Transaktion in Double*/
    protected double amount;
    /** descripton String ist die Beschreibung zur Transaktion*/
    protected String descripton;

    //getter
    /**
     * Gibt {@link Transaction#date} zurück
     * @return String vom Datum der Transaktion
     */
    public String getDate() {return this.date;}

    /**
     * Gibt {@link Transaction#amount } zurück
     * @return double Wert der Transaktion
     */
    public double getAmount() {return this.amount;}

    /**
     * Gibt {@link Transaction#descripton} zurück
     * @return String Beschreibung der Transaktion
     */
    public String getDescripton() {return this.descripton;}

    /**
     * Setzt {@link Transaction#date} der Transaktion
     * @param date String zum Datum der Transaltion
     */
    public void setDate(String date){this.date = date;}

    /**
     * Setzt {@link Transaction#descripton} der Transaktion
     * @param descripton String beschreibung der Transaktion
     */
    public void setDescripton(String descripton){this.descripton = descripton;}

    /**
     * Setzt {@link Transaction#amount} der Transaktion
     * @param amount double ist die Menge der Transaktion
     */
    public void setAmount(double amount) {this.amount = amount;}

    /**
     * Consturctor der Klasse {@link Transaction}
     * @param date String der das Datum der Transaktion setzt
     * @param amount Double Menge der Transaktion
     * @param descripton String die Beschreibung der Transaktion
     */
    Transaction(String date, double amount, String descripton){
        this.date = date;
        this.amount = amount;
        this.descripton = descripton;
    }

    /**
     * Copy Constructor der Klasse {@link Transaction}
     * @param t Transaction die kopiert werden soll
     */
    public Transaction(Transaction t){
        this(t.date = t.date, t.amount, t.descripton);
    }

    /**
     * Gibt {@link Transaction} als String zurück
     * @return String gibt {@link Payment} in Reihenfolge: {@link Transaction#date Datum};{@link Transaction#descripton Beschreibung};
     * {@link Transaction#calculateBill() Rechnung}
     */
    @Override
    public String toString(){
        return ("Datum:" + this.date + " Beschreibung:" + this.descripton +" Menge:" + this.calculateBill());
    }

    /**
     * Berechnet die Rechnung der Transaktion
     * @return double ausgerechnete Rechnung
     */
    @Override
    public double calculateBill(){
        return this.amount;
    }

    /**
     * Überprüft ob die Objekte der Klasse {@link Transaction} gleich sind
     * @param t  {@link Transaction} Objekt das das mit this verglichen werden soll
     * @return boolean true wenn Objekt und this gleich sind
     */
    @Override
    public boolean equals(Object t){
        if (t instanceof Transaction){
            if(!this.date.equals(((Transaction)t).date)) return false;
            if(!this.descripton.equals(((Transaction)t).descripton)) return false;
            if(this.amount != ((Transaction)t).amount) return false;
        }
        else return false;

        return true;
    }
    @Override
    public int compareTo(Transaction t){
        return Double.compare(this.amount, t.amount);
    }
}
