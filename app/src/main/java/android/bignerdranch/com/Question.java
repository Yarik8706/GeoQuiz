package android.bignerdranch.com;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean isCorrectAnswer;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public boolean getCorrectAnswer(){
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean isCorrect){
        isCorrectAnswer = isCorrect;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
