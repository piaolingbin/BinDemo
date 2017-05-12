package com.soufun.bin.bindemo.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.soufun.bin.bindemo.Book;
import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.User;

public class ProviderActivity extends AppCompatActivity {
    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Uri bookUri = Uri.parse("content://com.soufun.bin.bindemo.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "程序设计的艺术");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver()
                .query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book(bookCursor.getInt(0), bookCursor.getString(1));
            Log.d(TAG, "query book:" + book.toString());
        }
        bookCursor.close();

        Uri userUri = Uri.parse("content://com.soufun.bin.bindemo.provider/user");
        Cursor userCursor = getContentResolver()
                .query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while(userCursor.moveToNext()){
            User user = new User(userCursor.getInt(0) , userCursor.getString(1) , userCursor.getInt(2) == 1);
            Log.d(TAG , "query user:" + user.toString());
        }
        userCursor.close();
    }
}
