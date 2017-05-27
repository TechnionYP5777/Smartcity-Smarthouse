package il.ac.technion.cs.smarthouse.networking.messages;

import org.junit.Test;

import org.junit.Assert;

/**
 * 
 * @author Inbal Zukerman
 * @since 29.12.16
 */

public class MessageTypeTest {

    @Test
    public void testMessageType() {
        Assert.assertEquals("answer", MessageType.ANSWER.toString());
        Assert.assertEquals("failure", MessageType.FAILURE.toString());
        Assert.assertEquals("registration", MessageType.REGISTRATION.toString());
        Assert.assertEquals("success", MessageType.SUCCESS.toString());
        Assert.assertEquals("update", MessageType.UPDATE.toString());

    }
}
