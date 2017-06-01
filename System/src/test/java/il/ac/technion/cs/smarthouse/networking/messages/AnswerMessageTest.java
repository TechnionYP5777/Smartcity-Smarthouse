package il.ac.technion.cs.smarthouse.networking.messages;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage.Answer;

/** @author Sharon
 * @since 29.12.16 */
@SuppressWarnings("static-method")
public class AnswerMessageTest {
    @Test public void basicAnswerMessageTest() {
        final JsonParser parser = new JsonParser();
        final AnswerMessage message = new AnswerMessage(Answer.FAILURE);

        Assert.assertEquals(parser.parse(message.toJson()), parser.parse(new Gson().toJson(message)));
    }

    @Test public void messageTypeIsAnswer() {
        Assert.assertEquals(MessageType.ANSWER, new AnswerMessage(Answer.FAILURE).getType());
        Assert.assertEquals(MessageType.ANSWER, new AnswerMessage(Answer.SUCCESS).getType());
    }

    @Test public void getAnswerWorksAsExpected() {
        Assert.assertEquals(Answer.FAILURE, new AnswerMessage(Answer.FAILURE).getAnswer());
        Assert.assertEquals(Answer.SUCCESS, new AnswerMessage(Answer.SUCCESS).getAnswer());
    }

    @Test public void changeAnswerToSuccessWorks() {
        final AnswerMessage message = new AnswerMessage(Answer.FAILURE);
        message.setAnswer(Answer.SUCCESS);

        Assert.assertEquals(Answer.SUCCESS, message.getAnswer());
    }

    @Test public void changeAnswerToFailureWorks() {
        final AnswerMessage message = new AnswerMessage(Answer.SUCCESS);
        message.setAnswer(Answer.FAILURE);

        Assert.assertEquals(Answer.FAILURE, message.getAnswer());
    }
}
