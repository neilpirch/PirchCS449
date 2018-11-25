package com.neilpirch.grammarfactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.neilpirch.grammarfactory.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizQuestions.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_LEVEL + " INTEGER, " +
                QuestionTable.COLUMN_CATEGORY + " TEXT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NUM + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionTable() {
        QuizQuestion q1 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word cat? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 1);
        QuizQuestion q2 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word black? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 3);
        QuizQuestion q3 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word quietly? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 4);
        QuizQuestion q4 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word ate? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 2);
        QuizQuestion q5 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word dinner? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 1);
        addQuestion(q1);
        addQuestion(q2);
        addQuestion(q3);
        addQuestion(q4);
        addQuestion(q5);
    }

    private void addQuestion(QuizQuestion question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_LEVEL, question.getLevel());
        cv.put(QuestionTable.COLUMN_CATEGORY, question.getCategory());
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionTable.COLUMN_ANSWER_NUM, question.getAnswerNum());
        db.insert(QuestionTable.TABLE_NAME, null, cv);
    }

    public ArrayList<QuizQuestion> getAllQuestions() {
        ArrayList<QuizQuestion> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                QuizQuestion question = new QuizQuestion();
                question.setLevel(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_LEVEL)));
                question.setCategory(c.getString(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return questionList;
    }

}
