package il.ac.technion.cs.eldery.networking.messages;

public class AnswerMessage extends Message {
    private Answer answer;

    public AnswerMessage(final Answer answer) {
        super(MessageType.ANSWER);

        this.answer = answer;
    }

    /** @return answer this message contains */
    public Answer getAnswer() {
        return answer;
    }

    /** Sets a new answer for this message
     * @param ¢ new answer */
    public void setAnswer(final Answer ¢) {
        answer = ¢;
    }

    public enum Answer {
        SUCCESS,
        FAILURE
    }
}
