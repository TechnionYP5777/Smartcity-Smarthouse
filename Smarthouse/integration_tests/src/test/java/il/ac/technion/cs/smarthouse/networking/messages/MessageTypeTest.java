package il.ac.technion.cs.smarthouse.networking.messages;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;

/** @author Sharon
 * @since 29.12.16 */
@SuppressWarnings("static-method")
public class MessageTypeTest {
    @Test public void createRegistrationTypeFromString() {
        Assert.assertEquals(MessageType.REGISTRATION, MessageType.fromString("registration"));
        Assert.assertEquals(MessageType.REGISTRATION, MessageType.fromString("Registration"));
        Assert.assertEquals(MessageType.REGISTRATION, MessageType.fromString("registration".toUpperCase()));
        Assert.assertEquals(MessageType.REGISTRATION, MessageType.fromString("rEgIsTrAtIoN"));
    }

    @Test public void createUpdateTypeFromString() {
        Assert.assertEquals(MessageType.UPDATE, MessageType.fromString("update"));
        Assert.assertEquals(MessageType.UPDATE, MessageType.fromString("Update"));
        Assert.assertEquals(MessageType.UPDATE, MessageType.fromString("update".toUpperCase()));
        Assert.assertEquals(MessageType.UPDATE, MessageType.fromString("uPdAtE"));
    }

    @Test public void createAnswerTypeFromString() {
        Assert.assertEquals(MessageType.ANSWER, MessageType.fromString("answer"));
        Assert.assertEquals(MessageType.ANSWER, MessageType.fromString("Answer"));
        Assert.assertEquals(MessageType.ANSWER, MessageType.fromString("answer".toUpperCase()));
        Assert.assertEquals(MessageType.ANSWER, MessageType.fromString("aNsWeR"));
    }

    @Test public void unknownStringOfMessageTypeReturnsNull() {
        Assert.assertNull(MessageType.fromString("sup"));
        Assert.assertNull(MessageType.fromString("this is not a legal message type, I wonder what will be returned?"));
    }
}
