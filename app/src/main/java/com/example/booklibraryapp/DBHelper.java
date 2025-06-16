package com.example.booklibraryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Library.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE authors (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
        db.execSQL("CREATE TABLE readers (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
        db.execSQL("CREATE TABLE books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "author_id INTEGER, " +
                "FOREIGN KEY(author_id) REFERENCES authors(id));");

        db.execSQL("INSERT INTO authors(name) VALUES ('Лев Толстой'), ('Фёдор Достоевский');");
        db.execSQL("INSERT INTO readers(name) VALUES ('Иван Иванов'), ('Анна Смирнова');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS authors");
        db.execSQL("DROP TABLE IF EXISTS readers");
        onCreate(db);
    }
}

