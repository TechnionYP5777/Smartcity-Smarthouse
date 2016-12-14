package il.ac.technion.cs.eldery.networking.messages;

import org.junit.Assert;
import org.junit.Test;

public class MessageFactoryTest {
    @Test public void recognizeRegistrationMessages() {
        Assert.assertTrue(MessageFactory.craete("{\"type\": \"registration\"}") instanceof RegisterMessage);
    }

    @Test public void recognizeUpdateMessages() {
        Assert.assertTrue(MessageFactory.craete("{\"type\": \"update\"}") instanceof UpdateMessage);
    }

    @Test public void recognizeUnknownMessages() {
        Assert.assertNull(MessageFactory.craete("{\"type\": \":<\"}"));
    }
}
