package com.example.mentalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DoctorPage extends AppCompatActivity {
    private ListView listViewAppointments;
    private Data dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_page);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        String doctorUsername = getIntent().getStringExtra("username");
        // Initialize views
        listViewAppointments=findViewById(R.id.recyclerViewAppointments);

        // Initialize database helper
        dbHelper = new Data(this);

        // Retrieve and display appointments
        displayAppointments();

    }
    private boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.navigation_add_appointment) {
            // Handle search doctor action
            openaddActivity();
            // Now you can use the searchText as needed
            return true;
        } else if (itemId == R.id.navigation_profile) {
            // Handle profile action
            openProfileActivity();
            return true;
        } else if (itemId == R.id.navigation_chat) {
            // Handle add appointment action
            openChatActivity();
            return true;
        } else if (itemId == R.id.navigation_view) {
            // Handle add appointment action
            openviewActivity();
            return true;
        } else {
            return false;
        }
    }
    private void openaddActivity() {
        // Replace this with the actual action you want to perform

        Intent intent = new Intent(DoctorPage.this, Addappointment.class);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(DoctorPage.this, Docprofile.class);
        String loggedInUsername = getIntent().getStringExtra("username");
        intent.putExtra("username", loggedInUsername); // Pass the username to the Profilepatient activity
        startActivity(intent);
    }



    private void openChatActivity() {
        // Replace this with the actual action you want to perform
        Intent intent = new Intent(DoctorPage.this,Chats.class);
        startActivity(intent);
    }
    private void openviewActivity() {
        // Replace this with the actual action you want to perform
        Intent intent = new Intent(DoctorPage.this,Doctorpateintappoint.class);
        String loggedInUsername = getIntent().getStringExtra("doctorName");
        intent.putExtra("doctorName", loggedInUsername); // Pass the username
        startActivity(intent);
    }
    private void displayAppointments() {
        // Get the logged-in username from the intent
        String loggedInUsername = getIntent().getStringExtra("username");

        // Check if the username is not null before proceeding
        if (loggedInUsername != null) {
            // Define the selection criteria
            String selection = Data.COL_DOCTOR_NAME + "=?";
            String[] selectionArgs = {loggedInUsername};

            try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
                String[] projection = {
                        Data.COL_DOCTOR_NAME,
                        Data.COL_APPOINTMENT_DATE
                };

                try (Cursor cursor = db.query(
                        Data.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,  // Pass the selectionArgs to filter by doctorName
                        null,
                        null,
                        null
                )) {
                    // Create a list to store formatted appointment details
                    ArrayList<String> appointmentList = new ArrayList<>();

                    // Iterate through the cursor and format each appointment
                    while (cursor.moveToNext()) {
                        String doctorName = cursor.getString(cursor.getColumnIndexOrThrow(Data.COL_DOCTOR_NAME));
                        String appointmentDate = cursor.getString(cursor.getColumnIndexOrThrow(Data.COL_APPOINTMENT_DATE));

                        // Format appointment details
                        String appointmentDetails = String.format(
                                "Doctor: %s\nDate: %s",
                                doctorName, appointmentDate
                        );

                        // Add formatted appointment details to the list
                        appointmentList.add(appointmentDetails);
                    }

                    // Display the appointments in a ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
                    listViewAppointments.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Log an error or handle the case where the username is not received
            Log.e("DisplayAppointments", "Username not received in intent");
        }
    }}
