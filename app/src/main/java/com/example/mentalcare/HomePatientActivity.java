package com.example.mentalcare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomePatientActivity extends AppCompatActivity {

    private MyDbHelper dbHelper;
    private ListView listViewAppointments;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Initialize views
        listViewAppointments=findViewById(R.id.recyclerViewAppointments);

        // Initialize database helper
        dbHelper = new MyDbHelper(this);
        String loggedInUsername = getIntent().getStringExtra("username");
        // Retrieve and display appointments
        displayAppointments(loggedInUsername);

    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.navigation_search_doctor) {
            // Handle search doctor action
            openDoctorActivity();
            // Now you can use the searchText as needed
            return true;
        } else if (itemId == R.id.navigation_profile) {
            // Handle profile action
            openProfileActivity();
            return true;
        } else if (itemId == R.id.navigation_add_appointment) {
            // Handle add appointment action
            openAppointActivity();
            return true;
        } else {
            return false;
        }
    }
    private void openDoctorActivity() {
        // Replace this with the actual action you want to perform

        Intent intent = new Intent(HomePatientActivity.this, Searchdoctor.class);
        startActivity(intent);
    }


    private void openProfileActivity() {
        Intent intent = new Intent(HomePatientActivity.this, Profilepatient.class);
        String loggedInUsername = getIntent().getStringExtra("username");
        intent.putExtra("username", loggedInUsername); // Pass the username to the Profilepatient activity
        startActivity(intent);
    }



    private void openAppointActivity() {
        // Replace this with the actual action you want to perform
        Intent intent = new Intent(HomePatientActivity.this,Makeappointment.class);
        startActivity(intent);
    }

    private void displayAppointments(String loggedInUsername) {
        // Open a readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Retrieve the currently logged-in patient's username

        // Define the columns you want to retrieve
        String[] projection = {
                MyDbHelper.COLUMN_PATIENT_NAME,
                MyDbHelper.COLUMN_HEALTH_STATUS,
                MyDbHelper.COLUMN_AGE,
                MyDbHelper.COLUMN_DOCTOR_NAME,
                MyDbHelper.COLUMN_APPOINTMENT_DATE
        };

        // Define the WHERE clause to filter appointments for the logged-in patient
        String selection = MyDbHelper.COLUMN_PATIENT_NAME + " = ?";
        String[] selectionArgs = {loggedInUsername};

        // Query the database to get appointments for the logged-in patient
        Cursor cursor = db.query(
                MyDbHelper.APPOINTMENTS_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Create a list to store appointment details
        ArrayList<String> appointmentList = new ArrayList<>();

        // Iterate through the cursor and add appointments to the list
        while (cursor.moveToNext()) {
            // Log each column value for debugging
            for (String columnName : projection) {
                int columnIndex = cursor.getColumnIndexOrThrow(columnName);
                String columnValue = cursor.getString(columnIndex);
                Log.d("DatabaseDebug", "Column: " + columnName + ", Value: " + columnValue);
            }

            String patientName = cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_PATIENT_NAME));
            String healthStatus = cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_HEALTH_STATUS));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_AGE));
            String doctorName = cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_DOCTOR_NAME));
            String appointmentDate = cursor.getString(cursor.getColumnIndexOrThrow(MyDbHelper.COLUMN_APPOINTMENT_DATE));

            // Create a string representation of the appointment
            String appointmentDetails = "Patient: " + patientName +
                    "\nHealth Status: " + healthStatus +
                    "\nAge: " + age +
                    "\nDoctor: " + doctorName +
                    "\nDate: " + appointmentDate;

            appointmentList.add(appointmentDetails);
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        // Log the contents of the appointment list
        for (String appointment : appointmentList) {
            Log.d("AppointmentDebug", "Appointment: " + appointment);
        }

        // Display the appointments in a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
        listViewAppointments.setAdapter(adapter);
    }}

