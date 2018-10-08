package com.neilpirch.grammarfactory;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountdown;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;

    private List<QuizQuestion> quizQuestionList;

    private int questionCounter;
    private int questionCountTotal;
    private QuizQuestion currentQuestion;

    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question_pos);
        textViewScore = findViewById(R.id.text_view_score_pos);
        textViewQuestionCount = findViewById(R.id.text_view_question_count_pos);
        textViewCountdown = findViewById(R.id.text_view_countdown_pos);
        radioGroup = findViewById(R.id.radio_group_pos);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button_confirm_pos);

        textColorDefaultRb = rb1.getTextColors();

        QuizDBHelper dbHelper = new QuizDBHelper(this);
        quizQuestionList = dbHelper.getAllQuestions();
        questionCountTotal = quizQuestionList.size();
        Collections.shuffle(quizQuestionList);

        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast noAnswer = Toast.makeText(QuizActivity.this, "No answer selected. Try again.", Toast.LENGTH_SHORT);
                        noAnswer.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        noAnswer.show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });

    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        radioGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = quizQuestionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNum = radioGroup.indexOfChild(rbSelected) + 1;

        if (answerNum == currentQuestion.getAnswerNum()) {
            score++;
            textViewScore.setText("Score: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNum()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                Toast t1 = Toast.makeText(QuizActivity.this, "Correct answer: " + rb1.getText(), Toast.LENGTH_SHORT);
                t1.setGravity(Gravity.CENTER_VERTICAL,0,0);
                t1.show();
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                Toast t2 = Toast.makeText(QuizActivity.this, "Correct answer: " + rb2.getText(), Toast.LENGTH_SHORT);
                t2.setGravity(Gravity.CENTER_VERTICAL,0,0);
                t2.show();
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                Toast t3 = Toast.makeText(QuizActivity.this, "Correct answer: " + rb3.getText(), Toast.LENGTH_SHORT);
                t3.setGravity(Gravity.CENTER_VERTICAL,0,0);
                t3.show();
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                Toast t4 = Toast.makeText(QuizActivity.this, "Correct answer: " + rb4.getText(), Toast.LENGTH_SHORT);
                t4.setGravity(Gravity.CENTER_VERTICAL,0,0);
                t4.show();
                break;
        }

        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz() {
        finish();
    }
}

