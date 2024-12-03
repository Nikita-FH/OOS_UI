package bank.utils;

import bank.*;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializer Klasse für OutgoingTransfer Objekt
 * @author Nikita
 * @implements {@link JsonDeserializer}
 */
public class OutgoingTransferDeserializer implements JsonDeserializer<OutgoingTransfer> {
    /**
     * Bekommt ein Json Element und gibt ein OutgoingTransfer Objekt zurück
     * @param jsonElement Das Element das zurückgegeben werden soll
     * @param type Der Typ des Elements
     * @param jsonDeserializationContext
     * @return Das OutgoingTransfer Objekt das entstanden ist
     * @throws JsonParseException
     */
    @Override
    public OutgoingTransfer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject transactionInstance = jsonElement.getAsJsonObject();

        return new OutgoingTransfer(transactionInstance.get("date").getAsString(), transactionInstance.get("amount").getAsDouble(), transactionInstance.get("descripton").getAsString(), transactionInstance.get("sender").getAsString(), transactionInstance.get("recipient").getAsString());
    }
}
