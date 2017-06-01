package il.ac.technion.cs.smarthouse.networking.messages;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;



/**
 * @author Inbal Zukerman
 * @since 30.12.16
 */

public class MessageTest {

    @Test
    public void messagesTest() {
        final String m1 = Message.createMessage(MessageType.UPDATE, "Stove" + PathBuilder.DELIMITER + "temp", 100, "11:12");
        Assert.assertEquals("update.stove.temp.sensorid-11:12=100", m1);
        
        assert !Message.isFailureMessage(m1);
        assert !Message.isSuccessMessage(m1);
        assert Message.isInMessage(m1, "sensorid-11:12");
        
        
    }

    @Test
    public void testMessage() {
        String m1 = Message.createMessage(MessageType.REGISTRATION, "", "", "11");
        Assert.assertEquals("registration.sensorid-11", m1);

        m1 = Message.createMessage(MessageType.ANSWER, MessageType.SUCCESS);
        Assert.assertEquals("answer.success", m1);

        Assert.assertNull(Message.send(m1, null, null));

        // TODO: inbal test more sending options

        assert Message.isInMessage(m1, MessageType.ANSWER.toString());
        assert !Message.isFailureMessage(m1);
    }

}
