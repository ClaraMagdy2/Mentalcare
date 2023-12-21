package com.example.mentalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Searchdoctor extends AppCompatActivity {
    private EditText editTextSearch;
    private Button buttonSearch;
    private TextView textViewSearchResults;
    private ListView listViewDoctorAppointments;
    private Data dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchdoctor);

        // Initialize views
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        textViewSearchResults = findViewById(R.id.textViewSearchResults);
        listViewDoctorAppointments = findViewById(R.id.listViewDoctorAppointments);

        // Initialize database helper
        dbHelper = new Data(this);

        // Set click listener for the search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform search when the button is clicked
                performSearch();
            }
        });
    }

    private void performSearch() {
        // Get the doctor's name from the EditText
        String doctorName = editTextSearch.getText().toString().trim();

        // Check if the doctor's name is not empty
        if (!TextUtils.isEmpty(doctorName)) {
            // Display the doctor's name in the textViewSearchResults
            textViewSearchResults.setText("Doctor: " + doctorName);

            // Retrieve and display appointments for the specific doctor
            displayDoctorAppointments(doctorName);
        } else {
            // If the doctor's name is empty, display an error message
            textViewSearchResults.setText("Please enter a doctor's name.");
            listViewDoctorAppointments.setAdapter(null); // Clear the ListView
        }
    }

    private void displayDoctorAppointments(String doctorName) {
        // Open a readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                Data.COL_APPOINTMENT_DATE
                // Add other columns as needed
        };

        // Define the selection criteria
        String selection = Data.COL_DOCTOR_NAME+ "=?";
        String[] selectionArgs = {doctorName};

        // Query the database to get appointments for the specific doctor
        Cursor cursor = db.query(
                Data.TABLE_NAME,
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
            String appointmentDate = cursor.getString(cursor.getColumnIndexOrThrow(Data.COL_APPOINTMENT_DATE));
            // Add other appointment details as needed

            // Create a string representation of the appointment
            String appointmentDetails = "Date: " + appointmentDate;
            appointmentList.add(appointmentDetails);
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        // Display the appointments in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
        listViewDoctorAppointments.setAdapter(adapter);
    }
}
