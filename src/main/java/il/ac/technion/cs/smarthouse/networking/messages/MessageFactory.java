package il.ac.technion.cs.smarthouse.networking.messages;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public enum MessageFactory {
    ;

    /** Creates a returns a message given its JSon encoding.
     * @param json JSon encoding of the message
     * @return message encoded extracted from the JSon string */
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
