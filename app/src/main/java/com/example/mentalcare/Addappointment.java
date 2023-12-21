package com.example.mentalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Addappointment extends AppCompatActivity {

    private Data dbHelper;
    private EditText editTextDoctorName;
    private DatePicker datePickerAppointmentDate;
    private Button buttonMakeAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addappointment);

        dbHelper = new Data(this);
        editTextDoctorName = findViewById(R.id.editTextDoctorName);
        datePickerAppointmentDate = findViewById(R.id.datePickerAppointmentDate);
        buttonMakeAppointment = findViewById(R.id.buttonMakeAppointment);

        buttonMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAppointment();
            }
        });
    }
    private void makeAppointment() {

        String loggedInUsername = getIntent().getStringExtra("username");
        String doctorName = editTextDoctorName.getText().toString().trim();
        // Convert date from DatePicker to a suitable string format
        String appointmentDate = datePickerToString(datePickerAppointmentDate);

        if (!doctorName.isEmpty() && !appointmentDate.isEmpty()) {
            if (dbHelper.addAppointment(doctorName, appointmentDate)) {
                Toast.makeText(this, "Appointment added successfully", Toast.LENGTH_SHORT).show();

                // Navigate to the DoctorPage activity
                Intent intent = new Intent(Addappointment.this, DoctorPage.class);
                intent.putExtra("doctorName", doctorName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to add appointment", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private String datePickerToString(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is 0-based
        int year = datePicker.getYear();
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }}
