package com.advrtizr.sannacodetest.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDBHelper extends SQLiteOpenHelper {

    private static ContactsDBHelper mInstance = null;

    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "contactsDB";
    public final static String TABLE_NAME = "contacts";

    public final static String KEY_ID = "_id";
    public final static String KEY_NAME = "name";
    public final static String KEY_LASTNAME = "lastName";
    public final static String KEY_EMAIL = "email";
    public final static String KEY_PHONE = "phone";
    public final static String KEY_USER_ID = "userId";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_NAME + TEXT_TYPE + COMMA_SEP +
                    KEY_LASTNAME + TEXT_TYPE + COMMA_SEP +
                    KEY_EMAIL + TEXT_TYPE + COMMA_SEP +
                    KEY_PHONE + TEXT_TYPE + COMMA_SEP +
                    KEY_USER_ID + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private ContactsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static ContactsDBHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new ContactsDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
