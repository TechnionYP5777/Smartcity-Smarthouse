package il.ac.technion.cs.eldery.networking.messages;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("static-method")
public class MessageFactoryTest {
    @Test public void recognizeRegistrationMessages() {
        assert MessageFactory.create("{\"type\": \"registration\"}") instanceof RegisterMessage;
    }

    @Test public void recognizeUpdateMessages() {
        assert MessageFactory.create("{\"type\": \"update\"}") instanceof UpdateMessage;
    }

    @Test public void recognizeUnknownMessages() {
        Assert.assertNull(MessageFactory.create("{\"type\": \":<\"}"));
    }
}
