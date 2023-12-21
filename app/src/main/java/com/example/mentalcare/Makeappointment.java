package com.example.mentalcare;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Makeappointment extends Activity {

    private EditText patientNameEditText, healthStatusEditText, ageEditText, doctorNameEditText;
    private DatePicker datePicker;
    private Button makeAppointmentButton;

    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeappointment);

        // Initialize views
        patientNameEditText = findViewById(R.id.editTextPatientName);
        healthStatusEditText = findViewById(R.id.editTextHealthStatus);
        ageEditText = findViewById(R.id.editTextAge);
        doctorNameEditText = findViewById(R.id.editTextDoctorName);
        datePicker = findViewById(R.id.datePickerAppointmentDate);
        makeAppointmentButton = findViewById(R.id.buttonMakeAppointment);

        // Initialize database helper
        dbHelper = new MyDbHelper(this);

        makeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAppointment();
            }
        });
    }

    private void makeAppointment() {
        // Get user input
        String patientName = patientNameEditText.getText().toString();
        String healthStatus = healthStatusEditText.getText().toString();
        int age = Integer.parseInt(ageEditText.getText().toString());
        String doctorName = doctorNameEditText.getText().toString();
        String appointmentDate = datePicker.getDayOfMonth() + "/" +
                (datePicker.getMonth() + 1) + "/" +
                datePicker.getYear();

        // Insert data into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDbHelper.COLUMN_PATIENT_NAME, patientName);
        values.put(MyDbHelper.COLUMN_HEALTH_STATUS, healthStatus);
        values.put(MyDbHelper.COLUMN_AGE, age);
        values.put(MyDbHelper.COLUMN_DOCTOR_NAME, doctorName);
        values.put(MyDbHelper.COLUMN_APPOINTMENT_DATE, appointmentDate);

        long newRowId = db.insert(MyDbHelper.APPOINTMENTS_TABLE_NAME, null, values);

        // Display a message based on the success of the insertion
        if (newRowId != -1) {
            showToast("Appointment made successfully");
        } else {
            // Insertion failed
            // You can handle the failure (e.g., show an error message)
            showToast("Failed to make appointment. Please try again.");
        }

        // Close the database connection
        db.close();
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
