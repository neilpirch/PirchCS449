package com.neilpirch.grammarfactory;

import android.provider.BaseColumns;

// container class for constants needed by SQLite
public final class QuizContract {

    private QuizContract() {}

    public static class QuestionTable implements BaseColumns{
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER_NUM = "answer_num";
        public static final String COLUMN_DIFFICULTY = "difficulty";
    }
}
