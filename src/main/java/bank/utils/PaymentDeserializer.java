package bank.utils;

import bank.Payment;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializer für Payment Objekte
 * @author Nikita
 * @implements {@link JsonDeserializer}
 */
public class PaymentDeserializer implements JsonDeserializer<Payment> {
    /**
     * Gibgt ein JsonElement des Typen Payment als Payment Objekt zurück
     * @param jsonElement Das JsonElement das als Payment zurückgegeben werden soll
     * @param type Der Typ des Json Elements
     * @param jsonDeserializationContext
     * @return Das Payment Objekt
     * @throws JsonParseException
     */
    @Override
    public Payment deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject transactionInstance = jsonElement.getAsJsonObject();
        return new Payment(transactionInstance.get("date").getAsString(), transactionInstance.get("amount").getAsDouble(), transactionInstance.get("descripton").getAsString(), transactionInstance.get("incomingInterest").getAsDouble(), transactionInstance.get("outgoingInterest").getAsDouble());

    }
}
