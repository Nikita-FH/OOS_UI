import bank.Payment;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentTest {
    Payment p1;
    Payment p2;

    @BeforeEach
    public void setUp() {
         p1 = new Payment("Heute",10,"Beschreibung1");
         p2 = new Payment("Heute2",20,"Beschreibung2",0.2,0.2);
    }

    @Test
    public void constructorTest(){
        assert p1.getDate().equals("Heute");
        assert p1.getAmount() == 10;
        assert p1.getDescripton().equals("Beschreibung1");

        //Test Constructor mit 3 Parametern
        assert p2.getDate().equals("Heute2");
        assert p2.getAmount() == 20;
        assert p2.getDescripton().equals("Beschreibung2");
        assert p2.getIncomingInterest() == 0.2;
        assert p2.getOutgoingInterest() == 0.2;

        //Test Copy Constructor
        p1 = new Payment(p2);
        assert p1.getDate().equals("Heute2");
        assert p1.getAmount() == 20;
        assert p1.getDescripton().equals("Beschreibung2");
        assert p1.getIncomingInterest() == 0.2;
        assert p1.getOutgoingInterest() == 0.2;

    }

    @Test
    public void getSetTest(){
        //Normales Einfuegen
        p1.setDate("Morgen");
        assert p1.getDate().equals("Morgen");
        p1.setAmount(20);
        assert p1.getAmount() == 20;
        p1.setDescripton("Neue Beschreibung");
        assert p1.getDescripton().equals("Neue Beschreibung");
        Assertions.assertDoesNotThrow(() -> p1.setIncomingInterest(0.2));
        assert p1.getIncomingInterest() == 0.2;
        Assertions.assertDoesNotThrow(() -> p1.setOutgoingInterest(0.2));
        assert p1.getOutgoingInterest() == 0.2;
    }

    @Test
    public void getSetTestException(){
        //Einfuegen von falschen Werten
        Assertions.assertThrows(TransactionAttributeException.class, () -> p1.setIncomingInterest(1.2));
        Assertions.assertThrows(TransactionAttributeException.class, () -> p1.setIncomingInterest(-0.2));
        Assertions.assertThrows(TransactionAttributeException.class, () -> p1.setOutgoingInterest(1.2));
        Assertions.assertThrows(TransactionAttributeException.class, () -> p1.setOutgoingInterest(-0.2));
    }

    @Test
    public void toStringTest(){
        assert p1.toString().equals("Datum:Heute Beschreibung:Beschreibung1 Menge:10.0 IncomingInterest:0.0 OutgoingInterest:0.0");
        assert p2.toString().equals("Datum:Heute2 Beschreibung:Beschreibung2 Menge:16.0 IncomingInterest:0.2 OutgoingInterest:0.2");
    }

    @Test
    public void calculateBillTest(){
        assert p1.calculateBill() == 10;
        assert p2.calculateBill() == 16;
        p2.setAmount(-20);
        assert p2.calculateBill() == -24;
    }

    @Test
    public void equalsTesten(){
        //negative testen
        Assertions.assertNotEquals(p1, p2);
        p1 = new Payment(p2);
        p1.setIncomingInterest(0.1);
        Assertions.assertNotEquals(p1, p2);
        p1 = new Payment(p2);
        p1.setOutgoingInterest(0.1);
        Assertions.assertNotEquals(p1, p2);

        Object o = new Object();
        Assertions.assertNotEquals(p1, o);

        //positive testen
        p1 = new Payment(p2);
        assert p1.equals(p2);
    }
}
