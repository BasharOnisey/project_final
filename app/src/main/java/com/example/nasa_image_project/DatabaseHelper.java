package com.example.nasa_image_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nasa_images.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "saved_images";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_URL = "url";
    private static final String COL_DATE = "date";
    private static final String COL_FEEDBACK = "feedback"; // Optional: if you want user feedback

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_URL + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_FEEDBACK + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertImage(SavedImageModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, model.getTitle());
        values.put(COL_URL, model.getUrl());
        values.put(COL_DATE, model.getDate());
        values.put(COL_FEEDBACK, model.getFeedback()); // Optional
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public List<SavedImageModel> getAllImages() {
        List<SavedImageModel> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(COL_URL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
                String feedback = cursor.getString(cursor.getColumnIndexOrThrow(COL_FEEDBACK));
                images.add(new SavedImageModel(id, title, url, date, feedback));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return images;
    }

    public void deleteImage(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}
