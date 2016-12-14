package il.ac.technion.cs.eldery.networking.messages;

import org.junit.Assert;
import org.junit.Test;

public class MessageFactoryTest {
    @Test public void recognizeRegistrationMessages() {
        Assert.assertTrue(MessageFactory.create("{\"type\": \"registration\"}") instanceof RegisterMessage);
    }

    @Test public void recognizeUpdateMessages() {
        Assert.assertTrue(MessageFactory.create("{\"type\": \"update\"}") instanceof UpdateMessage);
    }

    @Test public void recognizeUnknownMessages() {
        Assert.assertNull(MessageFactory.create("{\"type\": \":<\"}"));
    }
}
