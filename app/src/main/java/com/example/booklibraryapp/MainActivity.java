package com.example.booklibraryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ListView bookListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> bookList;
    ArrayList<Integer> bookIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        bookListView = findViewById(R.id.bookListView);
        bookList = new ArrayList<>();
        bookIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookList);
        bookListView.setAdapter(adapter);

        loadBooks();

        bookListView.setOnItemClickListener((adapterView, view, i, l) -> {
            int bookId = bookIds.get(i);
            Intent intent = new Intent(MainActivity.this, BookFormActivity.class);
            intent.putExtra("bookId", bookId);
            startActivity(intent);
        });

        findViewById(R.id.btnAddBook).setOnClickListener(v -> {
            startActivity(new Intent(this, BookFormActivity.class));
        });
    }

    private void loadBooks() {
        bookList.clear();
        bookIds.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT books.id, books.title, authors.name FROM books " +
                "LEFT JOIN authors ON books.author_id = authors.id", null);
        while (cursor.moveToNext()) {
            bookIds.add(cursor.getInt(0));
            String title = cursor.getString(1);
            String author = cursor.getString(2);
            bookList.add(title + " — " + author);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }
}
