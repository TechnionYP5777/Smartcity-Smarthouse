package il.ac.technion.cs.smarthouse.networking.messages;

import org.junit.Assert;
import org.junit.Test;

/** @author Sharon
 * @since 14.12.16 */
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
