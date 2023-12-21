package com.example.mentalcare;
import static com.example.mentalcare.MyDbHelper.COLUMN_APPOINTMENT_DATE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Data extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Appointmentss.db";
   public static final String TABLE_NAME = "Appointmentss";
    public static final String COL_ID = "id";
    public static final String COL_DOCTOR_NAME = "doctor_name";
    public static final String COL_APPOINTMENT_DATE = "appointment_date";

    public Data(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DOCTOR_NAME + " TEXT NOT NULL, " +
                COL_APPOINTMENT_DATE + " TEXT NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addAppointment(String doctorName, String appointmentDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DOCTOR_NAME, doctorName);
        contentValues.put(COL_APPOINTMENT_DATE, appointmentDate);

        long result = db.insert(TABLE_NAME, null, contentValues);

        // If data is inserted incorrectly, it will return -1
        return result != -1;
    }

    public Cursor getAllAppointments() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getAppointmentsByDoctor(String doctorName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve from both tables
        String[] projection = {
                TABLE_NAME + "." + COL_ID,
                TABLE_NAME + "." + COL_DOCTOR_NAME,
                MyDbHelper.APPOINTMENTS_TABLE_NAME + "." + COLUMN_APPOINTMENT_DATE,
                MyDbHelper.APPOINTMENTS_TABLE_NAME + "." + MyDbHelper.COLUMN_PATIENT_NAME,
                MyDbHelper.APPOINTMENTS_TABLE_NAME + "." + MyDbHelper.COLUMN_HEALTH_STATUS,
                MyDbHelper.APPOINTMENTS_TABLE_NAME + "." + MyDbHelper.COLUMN_AGE
        };

        // Specify the tables to be joined and the join condition
        String tables = TABLE_NAME + " INNER JOIN " + MyDbHelper.APPOINTMENTS_TABLE_NAME +
                " ON " + TABLE_NAME + "." + COL_DOCTOR_NAME + " = " +
                MyDbHelper.APPOINTMENTS_TABLE_NAME + "." + MyDbHelper.COLUMN_DOCTOR_NAME;

        String selection = TABLE_NAME + "." + COL_DOCTOR_NAME + "=?";
        String[] selectionArgs = {doctorName};

        return db.query(
                tables,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }}

