package il.ac.technion.cs.eldery.networking.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public enum MessageFactory {
    ;

    public static Message craete(String json) {
        JsonParser parser = new JsonParser();
        JsonObject element = parser.parse(json).getAsJsonObject();

        String type = element.get("type").getAsString().toLowerCase();

        Gson gson = new Gson();

        switch (type) {
            case "registration":
                return gson.fromJson(json, RegisterMessage.class);
            case "update":
                return gson.fromJson(json, UpdateMessage.class);
            default:
                break;
        }

        return null;
    }
}
