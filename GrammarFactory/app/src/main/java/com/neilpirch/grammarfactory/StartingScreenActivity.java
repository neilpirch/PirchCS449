package com.neilpirch.grammarfactory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        Button buttonStartQuizPos = findViewById(R.id.button_start_quiz);
        buttonStartQuizPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuizPos();
            }
        });
    }

    private void startQuizPos() {
        Intent intent = new Intent(StartingScreenActivity.this, QuizActivity.class);
        startActivity(intent);
    }
}
