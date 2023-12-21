package com.example.mentalcare;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private RadioGroup radioGroupRole;
    private Button buttonSignUp;
    private TextView textViewHaveAccount;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dbHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewHaveAccount = findViewById(R.id.textViewHaveAccount);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
                Intent intent2= new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent2);
            }
        });

        textViewHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the login screen
                Intent intent= new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (validateInput(username, email, password, confirmPassword)) {
            int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
            String role = (selectedRoleId == R.id.radioButtonDoctor) ? "Doctor" : "Patient";

            // Store user data in the SQLite database
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USERNAME, username);
            values.put(DatabaseHelper.COLUMN_EMAIL, email);
            values.put(DatabaseHelper.COLUMN_PASSWORD, password);
            values.put(DatabaseHelper.COLUMN_ROLE, role);

            long newRowId = db.insert(DatabaseHelper.USERS_TABLE_NAME, null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                finish(); // Close the sign-up activity
            } else {
                Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show();
            }

            db.close();
        }
    }

    private boolean validateInput(String username, String email, String password, String confirmPassword) {
        // Add your validation logic here
        // For simplicity, we are checking if fields are not empty

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
