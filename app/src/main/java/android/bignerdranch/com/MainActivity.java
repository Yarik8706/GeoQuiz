package android.bignerdranch.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private boolean isHideButtons;
    private TextView mQuestionTextView;

    private final Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onSaveInstanceState(Bundle savedInstateState) {
        super.onSaveInstanceState(savedInstateState);
        savedInstateState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        updateQuestion();

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswer(false);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % (mQuestionBank.length + 1);
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex - 1) % (mQuestionBank.length + 1);
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    private void updateQuestion() {
        if (mCurrentIndex == mQuestionBank.length){
            isHideButtons = true;
            mQuestionTextView.setText("Всего правильных ответов: " + getAnswersResult());
            mTrueButton.setVisibility(View.GONE);
            mFalseButton.setVisibility(View.GONE);
            return;
        }

        if (isHideButtons){
            mTrueButton.setVisibility(View.VISIBLE);
            mFalseButton.setVisibility(View.VISIBLE);
            isHideButtons = false;
        }

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private String getAnswersResult(){
        float allAnswers = mQuestionBank.length;
        float CorrectAnswers = 0;
        String output;

        for (Question question: mQuestionBank) {
            if (question.getCorrectAnswer()) {
                CorrectAnswers = CorrectAnswers + 1;
            }
        }

        output = (CorrectAnswers / allAnswers * 100) + "%";

        return output;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if(userPressedTrue == answerIsTrue){
                mQuestionBank[mCurrentIndex].setCorrectAnswer(true);
                messageResId = R.string.correct_toast;
            } else {
                mQuestionBank[mCurrentIndex].setCorrectAnswer(false);
                messageResId = R.string.incorrect_toast;
            }
        }

        mCurrentIndex = mCurrentIndex + 1;
        if (mCurrentIndex > mQuestionBank.length){
            mCurrentIndex = 0;
        }
        mIsCheater = false;
        updateQuestion();
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
