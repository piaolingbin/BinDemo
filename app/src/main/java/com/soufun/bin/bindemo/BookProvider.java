package com.soufun.bin.bindemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.soufun.bin.bindemo.db.DbOpenHelper;


/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class BookProvider extends ContentProvider {
    public static final String TAG = "BookProvider";
    public static final String AUTHORITY = "com.soufun.bin.bindemo.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private Context mContext;
    private SQLiteDatabase mDb;

    static {
        mUriMatcher.addURI(AUTHORITY , "book" , BOOK_URI_CODE);
        mUriMatcher.addURI(AUTHORITY , "user" , USER_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG , "onCrest, current thread:" + Thread.currentThread().getName());
        mContext = getContext();
        // 操作数据库是耗时操作，尽量不要在UI线程。这里仅仅为了方便演示
        initProviderData();
        return false;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext , null , null , 1).getWritableDatabase();
        mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android');");
        mDb.execSQL("insert into book values(4,'Ios');");
        mDb.execSQL("insert into book values(5,'Html5');");
        mDb.execSQL("insert into user values(1,'bin',1);");
        mDb.execSQL("insert into user values(2,'rose',0);");
    }

    private String getTableName(Uri uri){
        String tableName = null;
        switch (mUriMatcher.match(uri)){
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:break;
        }
        return tableName;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG , "query, current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported Uri:" + uri);
        }
        return mDb.query(table , projection , selection , selectionArgs , null , null , sortOrder , null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG , "insert");
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported Uri:" + uri);
        }
        mDb.insert(table , null , values);
        mContext.getContentResolver().notifyChange(uri , null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG , "delete");
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported Uri:" + uri);
        }
        int count = mDb.delete(table , selection , selectionArgs);
        if(count > 0){
            getContext().getContentResolver().notifyChange(uri , null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG , "update");
        String table = getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported Uri:" + uri);
        }
        int row = mDb.update(table , values , selection , selectionArgs);
        if (row > 0){
            getContext().getContentResolver().notifyChange(uri , null);
        }
        return row;
    }
}
