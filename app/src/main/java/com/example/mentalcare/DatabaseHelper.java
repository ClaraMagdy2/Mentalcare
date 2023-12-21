package com.example.mentalcare;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and table constants
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    // User table constants
    public static final String USERS_TABLE_NAME = "users";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";

    // SQL statements
    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + USERS_TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_ROLE + " TEXT)";

    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + USERS_TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the database (if needed)
        db.execSQL(SQL_DELETE_USERS_TABLE);
        onCreate(db);
    }
    // Check if a user with the given credentials exists in the database
    public boolean checkUser(String username, String password) {
        String[] columns = {COLUMN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USERS_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }
    // Retrieve user data based on username
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USERNAME, COLUMN_EMAIL};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(USERS_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            user = new User(username, userEmail);
        }

        cursor.close();
        db.close();
        return user;
    }
    public String getUserRole(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ROLE}; // Replace with your actual column name
        String selection = COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                USERS_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String userRole = null;
        if (cursor.moveToFirst()) {
            userRole = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
        }

        cursor.close();
        db.close();

        return userRole;
    }
}


