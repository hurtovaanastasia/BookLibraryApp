package com.example.booklibraryapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BookFormActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText etTitle;
    Spinner authorSpinner;
    int bookId = -1;
    ArrayAdapter<String> authorAdapter;
    ArrayList<Integer> authorIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);

        dbHelper = new DBHelper(this);
        etTitle = findViewById(R.id.etTitle);
        authorSpinner = findViewById(R.id.authorSpinner);
        authorIds = new ArrayList<>();

        loadAuthors();

        bookId = getIntent().getIntExtra("bookId", -1);
        if (bookId != -1) loadBook();

        findViewById(R.id.btnSave).setOnClickListener(v -> saveBook());
        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (bookId != -1) deleteBook();
        });
    }

    private void loadAuthors() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name FROM authors", null);
        ArrayList<String> authorNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            authorIds.add(cursor.getInt(0));
            authorNames.add(cursor.getString(1));
        }
        cursor.close();
        authorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorNames);
        authorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        authorSpinner.setAdapter(authorAdapter);
    }

    private void loadBook() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, author_id FROM books WHERE id = ?", new String[]{String.valueOf(bookId)});
        if (cursor.moveToFirst()) {
            etTitle.setText(cursor.getString(0));
            int authorId = cursor.getInt(1);
            int pos = authorIds.indexOf(authorId);
            authorSpinner.setSelection(pos);
        }
        cursor.close();
    }

    private void saveBook() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", etTitle.getText().toString());
        values.put("author_id", authorIds.get(authorSpinner.getSelectedItemPosition()));

        if (bookId == -1) {
            db.insert("books", null, values);
        } else {
            db.update("books", values, "id = ?", new String[]{String.valueOf(bookId)});
        }

        finish();
    }

    private void deleteBook() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("books", "id = ?", new String[]{String.valueOf(bookId)});
        finish();
    }
}
