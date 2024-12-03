import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.Transaction;
import bank.utils.IncomingTransferDeserializer;
import bank.utils.OutgoingTransferDeserializer;
import bank.utils.PaymentDeserializer;
import bank.utils.TransactionSerializer;
import com.google.gson.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrivateBankUtilsTest {
    @Test
    public void serializeDesrialize() {
        IncomingTransfer i = new IncomingTransfer("i",10,"i","is","ir");
        OutgoingTransfer o = new OutgoingTransfer("o",20,"o","os","or");
        Payment p = new Payment("p",10,"p",0.1,0.1);

        Path paths =  Paths.get(".\\src\\test\\testFiles");
        paths.toFile().mkdirs();

        JsonSerializer<Transaction> serializer = new TransactionSerializer();
        GsonBuilder gsonBuilderS = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionSerializer())
                .setPrettyPrinting();

        Gson gsonWriter = gsonBuilderS.create();
        JsonElement iString = gsonWriter.toJsonTree(i,Transaction.class);
        JsonElement oString = gsonWriter.toJsonTree(o,Transaction.class);
        JsonElement pString = gsonWriter.toJsonTree(p,Transaction.class);

        File fi = new File(paths.toFile() + "\\fi.json");
        File fo = new File(paths.toFile() + "\\fo.json");
        File fp = new File(paths.toFile() + "\\fp.json");

        Assertions.assertDoesNotThrow(() -> {
            FileWriter fri = new FileWriter(fi);
            FileWriter fro = new FileWriter(fo);
            FileWriter frp = new FileWriter(fp);

            fri.write(gsonWriter.toJson(iString));
            fri.close();
            fro.write(gsonWriter.toJson(oString));
            fro.close();
            frp.write(gsonWriter.toJson(pString));
            frp.close();
        });
        JsonDeserializer<IncomingTransfer> itd = new IncomingTransferDeserializer();
        JsonDeserializer<OutgoingTransfer> otd = new OutgoingTransferDeserializer();
        JsonDeserializer<Payment> pd = new PaymentDeserializer();

        GsonBuilder gsonBuilderitD = new GsonBuilder()
                .registerTypeAdapter(IncomingTransfer.class, itd);
        GsonBuilder gsonBuilderioD = new GsonBuilder()
                .registerTypeAdapter(OutgoingTransfer.class, otd);
        GsonBuilder gsonBuilderpD = new GsonBuilder()
                .registerTypeAdapter(Payment.class, pd);

        Gson gsoniD = gsonBuilderitD.create();
        Gson gsonoD = gsonBuilderioD.create();
        Gson gsonpD = gsonBuilderpD.create();

        Assertions.assertDoesNotThrow(() -> {
            FileReader fri = new FileReader(fi);
            FileReader fro = new FileReader(fo);
            FileReader frp = new FileReader(fp);

            JsonElement ji = gsoniD.fromJson(fri, JsonElement.class);
            JsonElement jo = gsonoD.fromJson(fro, JsonElement.class);
            JsonElement pi = gsonpD.fromJson(frp, JsonElement.class);

            IncomingTransfer itNew = gsoniD.fromJson(ji.getAsJsonObject().get("INSTANCE"), IncomingTransfer.class);
            OutgoingTransfer otNew = gsonoD.fromJson(jo.getAsJsonObject().get("INSTANCE"), OutgoingTransfer.class);
            Payment pNew = gsonpD.fromJson(pi.getAsJsonObject().get("INSTANCE"), Payment.class);

            fri.close();
            fro.close();
            frp.close();

            assert itNew.equals(i);
            assert otNew.equals(o);
            assert pNew.equals(p);
        });

        paths =  Paths.get(".\\src\\test\\testFiles");
        File[] files = paths.toFile().listFiles();
        if(files != null) {
            for (File file : files) {
                file.delete();
            }
            paths.toFile().delete();
        }
    }


}
