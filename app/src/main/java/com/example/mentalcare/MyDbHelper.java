package com.example.mentalcare;
import static com.example.mentalcare.Data.COL_DOCTOR_NAME;
import static com.example.mentalcare.Data.COL_ID;
import static com.example.mentalcare.Data.TABLE_NAME;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Appointments.db";
    private static final int DATABASE_VERSION = 1;

    public static final String APPOINTMENTS_TABLE_NAME = "appointments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATIENT_NAME = "patient_name";
    public static final String COLUMN_HEALTH_STATUS = "health_status";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_DOCTOR_NAME = "doctor_name";
    public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

    // SQL statement to create the appointments table
    private static final String CREATE_APPOINTMENTS_TABLE =
            "CREATE TABLE " + APPOINTMENTS_TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PATIENT_NAME + " TEXT," +
                    COLUMN_HEALTH_STATUS + " TEXT," +
                    COLUMN_AGE + " INTEGER," +
                    COLUMN_DOCTOR_NAME + " TEXT," +
                    COLUMN_APPOINTMENT_DATE + " TEXT);";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic (if needed)
    }

    public Cursor getAppointmentsByDoctor(String doctorName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                APPOINTMENTS_TABLE_NAME + "." + COLUMN_PATIENT_NAME,
                APPOINTMENTS_TABLE_NAME + "." + COLUMN_HEALTH_STATUS,
                APPOINTMENTS_TABLE_NAME + "." + COLUMN_AGE,
                APPOINTMENTS_TABLE_NAME + "." + COLUMN_APPOINTMENT_DATE
                // Add other columns as needed
        };

        // Specify the tables to be joined and the join condition
        String tables = APPOINTMENTS_TABLE_NAME + " INNER JOIN " + TABLE_NAME +
                " ON " + APPOINTMENTS_TABLE_NAME + "." + COLUMN_PATIENT_NAME + " = " +
                TABLE_NAME + "." + COLUMN_PATIENT_NAME +
                " INNER JOIN " + TABLE_NAME +
                " ON " + APPOINTMENTS_TABLE_NAME + "." + COLUMN_DOCTOR_NAME + " = " +
                TABLE_NAME + "." + COLUMN_DOCTOR_NAME;

        String selection = TABLE_NAME + "." + COLUMN_DOCTOR_NAME + "=?";
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
    }
}