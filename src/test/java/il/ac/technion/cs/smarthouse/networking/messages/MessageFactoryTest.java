package il.ac.technion.cs.smarthouse.networking.messages;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage;
import il.ac.technion.cs.smarthouse.networking.messages.MessageFactory;
import il.ac.technion.cs.smarthouse.networking.messages.RegisterMessage;
import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;

@SuppressWarnings("static-method")
public class MessageFactoryTest {
    @Test public void recognizeRegistrationMessages() {
        assert MessageFactory.create("{\"type\": \"registration\"}") instanceof RegisterMessage;
    }

    @Test public void recognizeUpdateMessages() {
        assert MessageFactory.create("{\"type\": \"update\"}") instanceof UpdateMessage;
    }

    @Test public void recognizeAnswerMessages() {
        assert MessageFactory.create("{\"type\": \"answer\"}") instanceof AnswerMessage;
    }

    @Test public void recognizeUnknownMessages() {
        Assert.assertNull(MessageFactory.create("{\"type\": \":<\"}"));
    }
}
