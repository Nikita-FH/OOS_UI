import bank.*;
import bank.exceptions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            PrivateBank bank = new PrivateBank("Volksbank",0.5,0.5,true);
            bank.toString();
        } catch (PrivateBankAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /*public static void main(String[] args) throws AccountDoesNotExistException {
        PrivateBank b_alt = new PrivateBank("die alte Bank", 0.2, 0.2);
        PrivateBank b_neu = new PrivateBank(b_alt);

        //Testen equals
        {
            PrivateBank test =  new PrivateBank("die alte Bank", 0.2, 0.2);
            System.out.println(test.equals(b_alt));
        }
        //Testen Copy Construtor
        System.out.println(b_alt.equals(b_neu));

        //getter und setter testen
        try{
            b_neu.setIncomingInterest(2);
        }
        catch (TransactionAttributeException e){
            System.out.println(e.getMessage());
        }
        try{
            b_neu.setOutgoingInterest(2);
        }
        catch (TransactionAttributeException e){
            System.out.println(e.getMessage());
        }
        b_neu.setIncomingInterest(0.00);
        b_neu.setOutgoingInterest(0.00);
        b_neu.setName("Die neue Bank");

        System.out.println(b_neu.getName().equals("Die neue Bank"));
        System.out.println(b_neu.getIncomingInterest() == 0.00);
        System.out.println(b_neu.getOutgoingInterest() == 0.00);

        //CreateAccount(String)
        try {
            b_neu.createAccount("Nikita");
            b_neu.createAccount("Maria");
            b_neu.createAccount("Kai");
            //AccountAlreadyExistsException
            b_neu.createAccount("Nikita");
        }
        catch (AccountAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        //CreateAccount(String, List<Transaction>)
        Payment p1 = new Payment("p1",10,"p1",0.2,0.2);
        Payment p2 = new Payment("p2",10,"p2",0.2,0.2);

        List<Payment> PL = new ArrayList<Payment>();
        PL.add(p1);
        PL.add(p2);
        try{
            b_neu.createAccount("Sven", PL );
            //AccountAlreadyExistsException
            b_neu.createAccount("Sven");
        }
        catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }
        try{
            //TransactionAlreadyExistException
            b_neu.createAccount("Nina",PL);
        }
        catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }
        try{
            PL = null;
            //TransactionAttributeException
            b_neu.createAccount("Max",PL);
        }
        catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }

        IncomingTransfer tIn1 = new IncomingTransfer("a",10,"a","Nikita","Sven");
        IncomingTransfer tIn2 = new IncomingTransfer("b",10,"b","Max","Sven");
        OutgoingTransfer tOut1 = new OutgoingTransfer("c",10,"c","Sven","Nikita");
        OutgoingTransfer tOut2 = new OutgoingTransfer("d",10,"d","Sven","Max");
        Transfer t1 = new Transfer("e",10,"e","Maria","Kai");
        Transfer t2 = new Transfer("f",10,"f","Sven","Kai");
        Transfer t3 = new Transfer("g",10,"g","Nikita","Sven");

        //AddTransaction fuer spaetere BankAlt Klasse
        try{
            b_alt.createAccount("Maria");
            b_alt.createAccount("Kai");
            b_alt.createAccount("Nikita");
            b_alt.createAccount("Sven");

            b_alt.addTransaction("Maria",t1);
            b_alt.addTransaction("Sven",t2);
            b_alt.addTransaction("Nikita",t3);
            b_alt.addTransaction("Kai",t1);
            b_alt.addTransaction("Kai",t2);
            b_alt.addTransaction("Sven",t3);
        }
        catch (AccountDoesNotExistException | TransactionAlreadyExistException | TransactionAttributeException | AccountAlreadyExistsException e) {
            System.out.println("Diese Fehlermeldung darf nicht erscheinen");
        }

        //AddTransaction(String, Transaction)
        try{
            b_neu.addTransaction("Nikita",tIn1);
            b_neu.addTransaction("Max",tIn2);
            b_neu.addTransaction("Sven",tOut1);
            b_neu.addTransaction("Sven",tOut2);
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }
        try{
            IncomingTransfer tIn3 = new IncomingTransfer("h",10,"h","Josie","Sven");
            //AccountDoesNotExistException
            b_neu.addTransaction("Josie",tIn3);
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }
        try{
            //TransactionAlreadyExistException
            b_neu.addTransaction("Nikita",tIn1);
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }
        try{
            IncomingTransfer tIn3 = null;
            //TransactionAttributeException
            b_neu.addTransaction("Nikita",tIn3);
        } catch (AccountDoesNotExistException | TransactionAlreadyExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }

        //removeTransaction testen
        try{
            //AccountDoesNotExistException
            b_neu.removeTransaction("Dieter",t1);
        }
        catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        try{
            IncomingTransfer tIn3 = new IncomingTransfer("h",10,"h","Josie","Sven");
            //TransactionDoesNotExistException
            b_neu.removeTransaction("Nikita",tIn3);
        }
        catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        try{
            b_neu.removeTransaction("Nikita",tIn1);
        }
        catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
            System.out.println("Dieser Fehler darf nicht angezeigt werden");
        }

        //containsTransaction testen
        try{
            b_neu.containsTransaction("Dieter",tIn1);
        } catch (AccountDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        try{
            b_neu.containsTransaction("Nikita",tIn1);
        } catch (AccountDoesNotExistException e) {
            System.out.println("Dieser Fehler darf nicht angezegit werden");
        }

        //toString testen
        System.out.println(b_neu.toString());
        System.out.println(b_alt.toString());

        //getTransactionSorted testen
        try {

            System.out.println(b_neu.getTransactionsSorted("Sven", false).toString());
            System.out.println(b_neu.getTransactionsSorted("Sven", true).toString());
        }
        catch (AccountDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        try {
            //AccoutnDoesNotExistsException
            System.out.println(b_neu.getTransactionsSorted("Dieter", true).toString());
        }
        catch (AccountDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        //getTransactions testen
        try{
            System.out.println(b_neu.getTransactions("Sven").toString());
        } catch (AccountDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        try{
            //AccountDoesNotExistException
            System.out.println(b_neu.getTransactions("Dieter").toString());
        } catch (AccountDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        //getTransactionsByType testen
        try{
            System.out.println(b_neu.getTransactionsByType("Sven", true).toString());
            OutgoingTransfer tOut = new OutgoingTransfer("a",100,"a","Sven","Maria");
            b_neu.addTransaction("Sven",tOut);
            System.out.println(b_neu.getTransactionsByType("Sven", false).toString());
        }
        catch(AccountDoesNotExistException e){
            System.out.println(e.getMessage());
        }
        try{
            //AcountDoesNotExistException
            System.out.println(b_neu.getTransactionsByType("Dieter", true).toString());
        }
        catch(AccountDoesNotExistException e){
            System.out.println(e.getMessage());
        }
        //getAccountBalance
        try{
            System.out.println(b_neu.getAccountBalance("Sven"));
            System.out.println(b_alt.getAccountBalance("Sven"));
        }
        catch(AccountDoesNotExistException | TransactionAttributeException e){
            System.out.println(e.getMessage());
        }

    }*/
}