package com.neilpirch.grammarfactory;

public class Category {

    public static final int PARTS_OF_SPEECH = 1;
    public static final int PHRASES = 2;
    public static final int SENTENCE_PARTS = 3;
    public static final int CLAUSES = 4;
    public static final int CAPITALIZATION = 5;
    public static final int PUNCTUATION = 6;

    private int id;
    private String name;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
