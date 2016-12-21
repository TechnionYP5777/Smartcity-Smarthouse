package il.ac.technion.cs.eldery.networking.messages;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public enum MessageFactory {
    ;

    public static Message create(final String json) {
        final String type = new JsonParser().parse(json).getAsJsonObject().get("type").getAsString().toLowerCase();

        final Gson $ = new Gson();

        switch (type) {
            case "registration":
                return $.fromJson(json, RegisterMessage.class);
            case "update":
                return $.fromJson(json, UpdateMessage.class);
            case "answer":
                return $.fromJson(json, AnswerMessage.class);
            default:
                return null;
        }

    }
}
