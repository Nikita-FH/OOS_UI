import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PrivateBankTest {
    PrivateBank volksBank;
    PrivateBank sparkasse;

    IncomingTransfer it1;
    IncomingTransfer it2;

    OutgoingTransfer ot1;
    OutgoingTransfer ot2;

    Payment p1;
    Payment p2;

    @BeforeEach
    void setUp() {
        try {
            volksBank = new PrivateBank("VolksBank",0.1,0.1,false);
            sparkasse = new PrivateBank("Sparkasse",0.1,0.1,false);
        } catch (PrivateBankAlreadyExistsException | IOException e) {
            e.printStackTrace();
        }


        it1 = new IncomingTransfer("it1",10,"it1", "Sender1","recipient1");
        it2 = new IncomingTransfer("it2",20,"it2","Sender2","recipient2");

        ot1 = new OutgoingTransfer("ot1",10,"ot1","Sender1","recipient1");
        ot2 = new OutgoingTransfer("ot2",20,"ot2","Sender2","recipient2");

        p1 = new Payment("p1",10,"p1",0.2,0.2);
        p2 = new Payment("p2",20,"p2",0.2,0.2);

        try {
            volksBank.createAccount("ACC1");
            volksBank.createAccount("ACC2");
            sparkasse.createAccount("ACC3");
            sparkasse.createAccount("ACC4");
            volksBank.addTransaction("ACC1",it1);
            volksBank.addTransaction("ACC2",ot1);
            volksBank.addTransaction("ACC2",p1);
            sparkasse.addTransaction("ACC3",it2);
            sparkasse.addTransaction("ACC3",ot2);
            sparkasse.addTransaction("ACC4",p2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @AfterEach
    void tearDown() {

        Path p =  Paths.get(".\\src\\main\\resources\\PrivateBanken\\");
        File[] files = p.toFile().listFiles();
        if(files != null) {
            for (File f : files) {
                if (f.isDirectory()){
                    File[] files2 = f.listFiles();

                    if (files2 != null){
                        for(File f2 : files2){
                            f2.delete();
                        }
                    }

                }
                f.delete();
            }
        }
    }

    @Test
    void ConstructorTest() {
        assert volksBank.getName().equals("VolksBank");
        assert volksBank.getDirectory().equals(".\\src\\main\\resources\\PrivateBanken\\VolksBank");
        assert volksBank.getIncomingInterest() == 0.1;
        assert volksBank.getOutgoingInterest() == 0.1;

        try {
            sparkasse = new PrivateBank(volksBank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert sparkasse.getName().equals("VolksBank-Copy");
        assert sparkasse.getDirectory().equals(".\\src\\main\\resources\\PrivateBanken\\VolksBank-Copy");
        assert sparkasse.getIncomingInterest() == 0.1;
        assert sparkasse.getOutgoingInterest() == 0.1;
        Assertions.assertDoesNotThrow(() ->  sparkasse.getTransactions("ACC1").size() == 3);
        Assertions.assertDoesNotThrow(() ->  sparkasse.getTransactions("ACC1").equals(volksBank.getTransactions("ACC1")));

        Assertions.assertDoesNotThrow( () -> new PrivateBank("VolksBank",0.1,0.1,true));
        try {
            PrivateBank v2 = new PrivateBank("VolksBank",0.1,0.1,true);
            assert v2.equals(volksBank);
        } catch (PrivateBankAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void exceptionTest() {
        Assertions.assertThrows(PrivateBankAlreadyExistsException.class, () -> new PrivateBank("VolksBank",0.1,0.1,false));
        Assertions.assertThrows(TransactionAttributeException.class, () -> volksBank.setOutgoingInterest(1.2));
        Assertions.assertThrows(TransactionAttributeException.class, () -> volksBank.setOutgoingInterest(-0.2));
        Assertions.assertThrows(TransactionAttributeException.class, () -> volksBank.setIncomingInterest(1.2));
        Assertions.assertThrows(TransactionAttributeException.class, () -> volksBank.setIncomingInterest(-0.2));
    }

    @Test
    void getSetTest() {
        try {
            volksBank.setName("VolksBank2");
            assert volksBank.getName().equals("VolksBank2");
            volksBank.setIncomingInterest(0.2);
            assert volksBank.getIncomingInterest() == 0.2;
            volksBank.setOutgoingInterest(0.2);
            assert volksBank.getOutgoingInterest() == 0.2;
        }
        catch(PrivateBankAlreadyExistsException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void equalsTest(){
        Object o = new Object();
        Assertions.assertNotEquals(volksBank,o);
        PrivateBank v2;

        try{
            Payment p3 = new Payment("p3",10,"p3",0.2,0.2);
            v2 = new PrivateBank(volksBank);
            v2.addTransaction("ACC1",p3);
            Assertions.assertNotEquals(v2,volksBank);
            v2 = new PrivateBank(volksBank);
            v2.setOutgoingInterest(0.5);
            Assertions.assertNotEquals(v2,volksBank);
            v2 = new PrivateBank(volksBank);
            v2.setIncomingInterest(0.5);
            Assertions.assertNotEquals(v2,volksBank);
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void toStringAndSortTest(){
        String res = "Bank: VolksBank\n" +
                "Incoming Interest: 0.1\n" +
                "Outgoing Interest: 0.1\n" +
                "Accounts: ACC1: [Datum:it1 Beschreibung:it1 Menge:10.0 Sender:Sender1 Recipient:recipient1]\n" +
                "ACC2: [Datum:ot1 Beschreibung:ot1 Menge:-10.0 Sender:Sender1 Recipient:recipient1, Datum:p1 Beschreibung:p1 Menge:9.0 IncomingInterest:0.1 OutgoingInterest:0.1]\n";
        Assertions.assertEquals(res,volksBank.toString());
    }

    @Test
    public void createAccountWithList(){
        PrivateBank local;

        try {
            local = new PrivateBank("Local",0.2,0.2,false);
        } catch (PrivateBankAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Transaction> transactionsList = new ArrayList<>();
        transactionsList.add(p1);
        transactionsList.add(it1);
        transactionsList.add(ot1);

        //Mit "guter" Liste testen
        try {
            local.createAccount("test",transactionsList);
            assert local.containsTransaction("test",p1);
            assert local.containsTransaction("test",it1);
            assert local.containsTransaction("test",ot1);
        } catch (AccountAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AccountDoesNotExistException e) {
            throw new RuntimeException(e);
        }

        //mit "schlechter" Liste
        Assertions.assertThrows(AccountAlreadyExistsException.class, ()-> local.createAccount("test"));
        transactionsList = null;
        List<Transaction> finalTransactionsList = transactionsList;
        Assertions.assertThrows(TransactionAttributeException.class, ()-> local.createAccount("test2", finalTransactionsList));
        transactionsList = new ArrayList<Transaction>();
        transactionsList.add(p1);
        transactionsList.add(p1);
        List<Transaction> finalTransactionsList1 = transactionsList;
        Assertions.assertThrows(TransactionAlreadyExistException.class,() -> local.createAccount("test3", finalTransactionsList1));
        IncomingTransfer it4 = null;
        transactionsList.remove(p1);
        transactionsList.add(it4);
        List<Transaction> finalTransactionsList2 = transactionsList;
        Assertions.assertThrows(TransactionAttributeException.class, ()-> local.createAccount("test4", finalTransactionsList2));

    }

    @Test
    public void addTransactionErrorTest(){
        Payment p3 = new Payment("p3",10,"p3",0.2,0.2);
        Payment p4 = null;
        Assertions.assertThrows(AccountDoesNotExistException.class, ()->volksBank.addTransaction("GibtsNicht",p3));
        Assertions.assertThrows(TransactionAttributeException.class, ()->volksBank.addTransaction("ACC1",p4));
    }

    @Test
    public void removeTransactionTest(){
        Assertions.assertDoesNotThrow(()->volksBank.removeTransaction("ACC1",it1));
        Assertions.assertDoesNotThrow(()->{
            assert volksBank.getTransactions("ACC1").isEmpty();
        }
        );
    }

    @Test
    public void removeTransactionErrorTest(){
        Assertions.assertThrows(AccountDoesNotExistException.class, ()->volksBank.removeTransaction("GibtsNicht",p1));
        Assertions.assertThrows(TransactionDoesNotExistException.class, ()->volksBank.removeTransaction("ACC1",p1));
    }

    @Test
    public void containsTransactionErrorTest(){
        Assertions.assertThrows(AccountDoesNotExistException.class, ()->volksBank.containsTransaction("GibtsNicht",p1));
    }

    @Test
    public void getAccountBalanceTest(){
        Assertions.assertDoesNotThrow(()->{
                   assert volksBank.getAccountBalance("ACC1") == 10.0;
                   assert volksBank.getAccountBalance("ACC2") == -1;
                }
        );
    }

    @Test
    public void getAccountBalanceErrorTest(){
        Assertions.assertThrows(AccountDoesNotExistException.class, ()->volksBank.getAccountBalance("GibtsNicht"));
    }

    @Test
    public void getTransactionsErrorTest(){
        Assertions.assertThrows(AccountDoesNotExistException.class, ()->volksBank.getTransactions("GibtsNicht"));
    }

    @Test
    public void getTransactionsSortedTest(){
        Payment big = new Payment("Big",100,"Big",0.2,0.2);
        OutgoingTransfer small = new OutgoingTransfer("Small",1,"small","x","y");

        Assertions.assertDoesNotThrow(()->{
            volksBank.addTransaction("ACC1",big);
            volksBank.addTransaction("ACC1",small);
            List<Transaction> test = new ArrayList<>();
            test.add(big);
            test.add(it1);
            test.add(small);
            assert test.equals(volksBank.getTransactionsSorted("ACC1",true));
            test = new ArrayList<>();
            test.add(small);
            test.add(it1);
            test.add(big);
            assert test.equals(volksBank.getTransactionsSorted("ACC1",false));
        });
    }

    @Test
    public void getTransactionsSortedErrorTest(){
        Assertions.assertThrows(AccountDoesNotExistException.class, ()->volksBank.getTransactionsSorted("GibtsNichts",true));
    }

    @Test
    public void getTransactionsByTypeTest(){
        Assertions.assertDoesNotThrow(()->{
            List<Transaction> test = new ArrayList<>();
            test.add(p1);
            assert test.equals(volksBank.getTransactionsByType("ACC2",true));
            test.remove(p1);
            test.add(ot1);
            assert test.equals(volksBank.getTransactionsByType("ACC2",false));

        });
    }

    @Test
    public void getTransactionsByTypeErrorTest(){
        Assertions.assertThrows(AccountDoesNotExistException.class, ()->volksBank.getTransactionsByType("GibtsNichts",true));
    }
}
