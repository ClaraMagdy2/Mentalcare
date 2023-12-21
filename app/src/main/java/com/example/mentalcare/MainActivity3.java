package com.example.mentalcare;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView textViewCreateAccount;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.editTextUsernameLogin);
        passwordEditText = findViewById(R.id.editTextPasswordLogin);
        loginButton = findViewById(R.id.buttonLogin);
        textViewCreateAccount = findViewById(R.id.textViewCreateAccount);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the sign-up screen when the link is clicked
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        // Collect user input
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity3.this, "Username and password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the user exists in the database
        if (dbHelper.checkUser(username, password)) {
            // Get the user role from the database (replace "getUserRole" with the actual method)
            String userRole = dbHelper.getUserRole(username);

            // Check the user role and open the corresponding activity
            if ("Doctor".equals(userRole)) {
                // Doctor login, open DoctorPage
                Toast.makeText(MainActivity3.this, "Doctor login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity3.this, DoctorPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish(); // Close the login activity
            } else if ("Patient".equals(userRole)) {
                // Patient login, open HomePatientActivity
                Toast.makeText(MainActivity3.this, "Patient login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity3.this, HomePatientActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish(); // Close the login activity
            } else {
                // Unknown role
                Toast.makeText(MainActivity3.this, "Unknown user role", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Invalid credentials
            Toast.makeText(MainActivity3.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }}}

