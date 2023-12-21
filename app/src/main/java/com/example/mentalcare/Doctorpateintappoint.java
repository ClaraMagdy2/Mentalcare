package com.example.mentalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Doctorpateintappoint extends AppCompatActivity {

    private MyDbHelper dbHelper;
    private ListView listViewAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorpateintappoint);

        dbHelper = new MyDbHelper(this);
        listViewAppointments = findViewById(R.id.recyclerViewAppointments);

        // Retrieve the doctor's name from the intent
        String doctorName = getIntent().getStringExtra("doctorName");

        // Check if the doctor's name is received successfully
        if (doctorName != null) {
            // Display appointments for the specified doctor
            displayAppointments(doctorName);
        } else {
            // Log an error or handle the case where the doctor's name is not received
            Log.e("Doctorpateintappoint", "Doctor's name not received in intent");
        }
    }

    private void displayAppointments(String doctorName) {
        // Retrieve appointments for the specific doctor
        Cursor cursor = dbHelper.getAppointmentsByDoctor(doctorName);

        // Create a list to store formatted appointment details
        ArrayList<String> appointmentList = new ArrayList<>();

        // Iterate through the cursor and format each appointment
        while (cursor.moveToNext()) {
            String patientName = cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_PATIENT_NAME));
            String healthStatus = cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_HEALTH_STATUS));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_AGE));
            String appointmentDate = cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_APPOINTMENT_DATE));

            // Format appointment details
            String appointmentDetails = String.format(
                    "Patient: %s\nHealth Status: %s\nAge: %d\nDate: %s",
                    patientName, healthStatus, age, appointmentDate
            );

            // Add formatted appointment details to the list
            appointmentList.add(appointmentDetails);
        }

        // Create an ArrayAdapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);

        // Set the adapter to the ListView
        listViewAppointments.setAdapter(adapter);

        // Close the cursor to release resources
        cursor.close();
    }
}
