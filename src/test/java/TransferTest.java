import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Transaction;
import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferTest {
    IncomingTransfer it1;
    IncomingTransfer it2;

    OutgoingTransfer ot1;
    OutgoingTransfer ot2;
    @BeforeEach
    public void setUp() {
        it1 = new IncomingTransfer("Heute",10,"Beschreibung1");
        it2 = new IncomingTransfer("Heute2",20,"Beschreibung2","Sender2","recipient2");

        ot1 = new OutgoingTransfer("Heute",10,"Beschreibung1");
        ot2 = new OutgoingTransfer("Heute2",20,"Beschreibung2","Sender2","recipient2");
    }

    @Test
    public void ConstructorTest(){
        // IncomingTransfer
        assert it1.getDate().equals("Heute");
        assert it1.getAmount() == 10;
        assert it1.getDescripton().equals("Beschreibung1");
        assert it2.getSender().equals("Sender2");
        assert it2.getRecipient().equals("recipient2");

        // OutgoingTransfer
        assert ot1.getDate().equals("Heute");
        assert ot1.getAmount() == 10;
        assert ot1.getDescripton().equals("Beschreibung1");
        assert ot2.getSender().equals("Sender2");
        assert ot2.getRecipient().equals("recipient2");

        //Copy
        Transfer it3 = new Transfer(it2);
        assert it3.equals(it2);
        it2.setSender("Sender3");
        Assertions.assertNotEquals(it3,it2);
        it3 = new Transfer(it2);
        it2.setRecipient("recipient3");
        Assertions.assertNotEquals(it3,it2);
        it3 = new Transfer(it2);
        it2.setAmount(30);
        Assertions.assertNotEquals(it3,it2);

    }

    @Test
    public void getSetTest() {
        it1.setDate("Morgen");
        it1.setAmount(20);
        it1.setDescripton("Beschreibung2");
        it1.setSender("Sender1");
        it1.setRecipient("recipient1");

        assert it1.getDate().equals("Morgen");
        assert it1.getAmount() == 20;
        assert it1.getDescripton().equals("Beschreibung2");
        assert it1.getSender().equals("Sender1");
        assert it1.getRecipient().equals("recipient1");

        ot1.setDate("Morgen");
        ot1.setAmount(20);
        ot1.setDescripton("Beschreibung2");
        ot1.setSender("Sender1");
        ot1.setRecipient("recipient1");

        assert ot1.getDate().equals("Morgen");
        assert ot1.getAmount() == 20;
        assert ot1.getDescripton().equals("Beschreibung2");
        assert ot1.getSender().equals("Sender1");
        assert ot1.getRecipient().equals("recipient1");
    }

    @Test
    public void setExceptionTest() {
        Assertions.assertThrows(TransactionAttributeException.class, () -> it1.setAmount(-10));
    }

    @Test
    public void calculateBillTest(){
        assert it1.calculateBill() == 10;
        assert it2.calculateBill() == 20;
        assert ot1.calculateBill() == -10;
        assert ot2.calculateBill() == -20;
    }

    @Test
    public void equalsTest(){
        Transfer it3 = new Transfer(it2);
        assert it3.equals(it2);
        it2.setSender("Sender3");
        Assertions.assertNotEquals(it3,it2);
        it3 = new Transfer(it2);
        it2.setRecipient("recipient3");
        Assertions.assertNotEquals(it3,it2);
        it3 = new Transfer(it2);
        it2.setAmount(30);
        Assertions.assertNotEquals(it3,it2);
        it3 = new Transfer(it2);
        it2.setDescripton("Beschreibung3");
        Assertions.assertNotEquals(it3,it2);

        Object o = new Object();
        Assertions.assertNotEquals(it2,o);
    }

    @Test
    public void toStringTest(){
        assert it1.toString().equals("Datum:Heute Beschreibung:Beschreibung1 Menge:10.0 Sender:null Recipient:null");
        assert it2.toString().equals("Datum:Heute2 Beschreibung:Beschreibung2 Menge:20.0 Sender:Sender2 Recipient:recipient2");
    }


}
