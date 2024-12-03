package bank.utils;

import bank.Payment;
import bank.Transaction;
import bank.Transfer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Klassie die sich um Serializiren von Transaktionen kümmert
 * @author Nikita
 * @implements {@link JsonSerializer}
 */
public class TransactionSerializer implements JsonSerializer<Transaction> {
    /**
     * Ist dazu da im Transaktionen in eine Json Datei zu schreiben
     * @param transaction die Transaktion die geschrieben werden soll
     * @param type der Typ der Datei
     * @param jsonSerializationContext
     * @return JsonElement die Transaktion als JsonElement
     */
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject transactionJson = new JsonObject();
        transactionJson.addProperty("CLASSNAME", transaction.getClass().getSimpleName());

        JsonObject transactionInstance = new JsonObject();
        if (transaction instanceof Transfer) {
            transactionInstance.addProperty("sender", ((Transfer) transaction).getSender());
            transactionInstance.addProperty("recipient", ((Transfer) transaction).getRecipient());
        } else {
            transactionInstance.addProperty("incomingInterest", ((Payment) transaction).getIncomingInterest());
            transactionInstance.addProperty("outgoingInterest", ((Payment) transaction).getOutgoingInterest());
        }
        transactionInstance.addProperty("date", transaction.getDate());
        transactionInstance.addProperty("amount",transaction.getAmount());
        transactionInstance.addProperty("descripton", transaction.getDescripton());
        //transactionJson.addProperty("INSTANCE", transactionInstance.toString());
        transactionJson.add("INSTANCE", transactionInstance);

        return transactionJson;
    }
}
