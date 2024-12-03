package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Klasse die eine Ein- bzw Auszahlung realisiert
 * @author Nikita Diser
 * @extends {@link Transaction}
 */
public class Payment extends Transaction{
    /**
     * Zinsen der Einzahlung, zwischen 0% und 100% als double 0-1
     */
    private double incomingInterest;
    /**
     * Zinsen der Auszahlung, zwischen 0% und 100% als double 0-1
     */
    private double outgoingInterest;

    /**
     * Gibt {@link Payment#incomingInterest} der Zahlung wieder
     * @return Double Zinsen für Einzahlungen
     */
    public double getIncomingInterest() {return this.incomingInterest;}

    /**
     * GIbt {@link Payment#outgoingInterest} der Zahlung wieder
     * @return Double Zinsen der Auszahung
     */
    public double getOutgoingInterest() {return this.outgoingInterest;}

    /**
     * Setzt {@link Payment#incomingInterest} der Einzahlung
     * @param incomingInterest double Zinsen für Einzahlungen, zwischen 0-1 (0%-100%)
     * @throws bank.exceptions.TransactionAlreadyExistException wenn Interest nicht zwischen 0-1 liegt
     */
    public void setIncomingInterest(double incomingInterest)  throws TransactionAttributeException{
        if (incomingInterest > 0 && incomingInterest <= 1)
            this.incomingInterest = incomingInterest;
        else
            throw new TransactionAttributeException("Interest nicht 0 <= X <=1");
    }

    /**
     * Setzt {@link Payment#outgoingInterest} der Auszahlung
     * @param outgoingInterest double Zinsen für Auszahlungen, zwischen 0-1 (0%-100%)
     * @throws bank.exceptions.TransactionAlreadyExistException wenn Interest nicht zwischen 0-1 liegt
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest > 0 && outgoingInterest <= 1)
            this.outgoingInterest = outgoingInterest;
        else
            throw new TransactionAttributeException("Interest nicht 0 <= X <=1");
    }

    /**
     * Constructor der Klasse {@link Payment}, ohne die Zinsen festzulegen
     * @param date String Datum der Zahlung
     * @param amount double Menge der Zahlung
     * @param descripton String Beschreibung der Zahlung
     */
    public Payment(String date, double amount, String descripton) {
        super(date, amount, descripton);
    }

    /**
     * Constructor der Klasse {@link Payment}
     * @param date String datum der Zahlung
     * @param amount double menge der Zahlung
     * @param descripton String beschreibung der Zahlung
     * @param incomingInterest double höhe der Zinsen für Einzahlunge
     * @param outgoingInterest double höhe der Zinsen für Auszahlungen
     */
    public Payment(String date, double amount, String descripton, double incomingInterest, double outgoingInterest) {
        this(date, amount, descripton);
        this.setIncomingInterest(incomingInterest);
        this.setOutgoingInterest(outgoingInterest);
    }

    /**
     * Copy Constructor der Klasse {@link Payment}
     * @param payment Payment Objekt, dass kopiert werden soll
     */
    public Payment(Payment payment) {
         this (payment.date, payment.amount, payment.descripton, payment.incomingInterest, payment.outgoingInterest);
    }

    /**
     * Gibt {@link Payment} als String wieder
     * @return String gibt {@link Payment} in Reihenfolge: {@link Transaction#date Datum};{@link Transaction#descripton Beschreibung};
     * {@link Payment#calculateBill() Rechnung};{@link Payment#incomingInterest Zinsen Einzahlung}; {@link Payment#outgoingInterest}
     */
    @Override
    public String toString(){
        return (super.toString() + " IncomingInterest:" + this.incomingInterest + " OutgoingInterest:" + this.outgoingInterest);
    }

    /**
     * Berechnet die Rechnung zur verzinsung der Ein- bzw Auszahlung
     * <p>Einzahlung wenn {@link Payment#amount} > 0</p>
     * <p>Auszahlung wenn {@link Payment#amount} <= 0</p>
     * @return double bei
     * <p>Einzahlung: Menge * (1-Zinsen)</p>
     * <p>Auszahlung: Menge * (1+Zinsen)</p>
     */
    @Override
    public double calculateBill(){
        if(this.amount > 0){
            return (this.amount * (1 - this.incomingInterest));
        }
        else {
            return (this.amount * (1 + this.outgoingInterest));
        }
    }

    /**
     * Überprüft ob Objekte der Klasse {@link Payment} auf gleichheit
     * @param t Payment das Objekt das auf gleichheit zu this überprüft
     * @return boolean true wenn Objekt mit this übereinstimmt, sonst false
     */
    @Override
    public boolean equals(Object t){
        if(t instanceof Payment){
            if(!super.equals(t)) return false;
            if(this.incomingInterest != ((Payment)t).incomingInterest) return false;
            if(this.outgoingInterest != ((Payment)t).outgoingInterest) return false;
        }
        else return false;

        return true;
    }

}
