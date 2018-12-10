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

    private static QuizDBHelper instance;

    private SQLiteDatabase db;

    private QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

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
                QuestionTable.COLUMN_ANSWER_NUM + " INTEGER, " +
                QuestionTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Parts of Speech");
        addCategory(c1);
        Category c2 = new Category("Phrases");
        addCategory(c2);
        Category c3 = new Category("Sentence Parts");
        addCategory(c3);
        Category c4 = new Category("Clauses");
        addCategory(c4);
        Category c5 = new Category("Capitalization");
        addCategory(c5);
        Category c6 = new Category("Punctuation");
        addCategory(c6);
    }

    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionTable() {
        QuizQuestion q1 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word cat? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 1, QuizQuestion.DIFFICULTY_EASY, Category.PARTS_OF_SPEECH);
        QuizQuestion q2 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word black? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 3, QuizQuestion.DIFFICULTY_EASY, Category.PARTS_OF_SPEECH);
        QuizQuestion q3 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word quietly? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 4, QuizQuestion.DIFFICULTY_MEDIUM, Category.PARTS_OF_SPEECH);
        QuizQuestion q4 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word ate? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 2, QuizQuestion.DIFFICULTY_MEDIUM, Category.PARTS_OF_SPEECH);
        QuizQuestion q5 = new QuizQuestion(1, "POS",
                "In the following sentence, what part of speech is the word dinner? \n" +
                        "The black cat quietly ate his dinner.", "noun", "verb", "adjective", "adverb", 1, QuizQuestion.DIFFICULTY_HARD, Category.PARTS_OF_SPEECH);
        QuizQuestion q6 = new QuizQuestion(1, "POS",
                "In the following sentence, what is the simple subject? \n" +
                        "The black cat quietly ate his dinner.", "black", "cat", "ate", "dinner", 2, QuizQuestion.DIFFICULTY_EASY, Category.SENTENCE_PARTS);
        QuizQuestion q7 = new QuizQuestion(1, "POS",
                "In the following sentence, what is the direct object? \n" +
                        "The black cat quietly ate his dinner.", "black", "cat", "ate", "dinner", 4, QuizQuestion.DIFFICULTY_EASY, Category.SENTENCE_PARTS);
        addQuestion(q1);
        addQuestion(q2);
        addQuestion(q3);
        addQuestion(q4);
        addQuestion(q5);
        addQuestion(q6);
        addQuestion(q7);
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
        cv.put(QuestionTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<QuizQuestion> getAllQuestions() {
        ArrayList<QuizQuestion> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                QuizQuestion question = new QuizQuestion();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setLevel(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_LEVEL)));
                question.setCategory(c.getString(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public ArrayList<QuizQuestion> getQuestions(int categoryID, String difficulty) {
        ArrayList<QuizQuestion> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};
        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                QuizQuestion question = new QuizQuestion();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setLevel(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_LEVEL)));
                question.setCategory(c.getString(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return questionList;
    }

}
