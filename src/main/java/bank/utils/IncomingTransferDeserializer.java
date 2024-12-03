package bank.utils;

import bank.IncomingTransfer;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserializer Klasse für IncomingTransfer Objekt
 * @author Nikita
 * @implements {@link JsonDeserializer}
 */
public class IncomingTransferDeserializer implements JsonDeserializer<IncomingTransfer> {
    /**
     * Bekommt ein JsonElement und gibt das dazugehörige IncomingTransfer Objekt aus
     * @param jsonElement das umgewandelt werden soll
     * @param type Type des Objekt
     * @param jsonDeserializationContext
     * @return das JsonElement als IncomingTransfer Objekt
     * @throws JsonParseException
     */
    @Override
    public IncomingTransfer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject transactionInstance = jsonElement.getAsJsonObject();

       return new IncomingTransfer(transactionInstance.get("date").getAsString(), transactionInstance.get("amount").getAsDouble(), transactionInstance.get("descripton").getAsString(), transactionInstance.get("sender").getAsString(), transactionInstance.get("recipient").getAsString());
    }
}
