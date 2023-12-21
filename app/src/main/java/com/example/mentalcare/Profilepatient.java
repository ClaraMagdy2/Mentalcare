package com.example.mentalcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Profilepatient extends AppCompatActivity {

    private TextView textViewPatientUsername;
    private TextView textViewPatientEmail;
    private ImageView imageViewProfilePhoto;
    private DatabaseHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepatient);

        dbHelper = new DatabaseHelper(this);

        textViewPatientUsername = findViewById(R.id.textViewPatientUsername);
        textViewPatientEmail = findViewById(R.id.textViewPatientEmail);

        // Retrieve the logged-in user's username (you need to pass this information from the login screen)
        String loggedInUsername = getIntent().getStringExtra("username");

        // Retrieve user data and update TextViews
        User user = dbHelper.getUserByUsername(loggedInUsername);
        if (user != null) {
            textViewPatientUsername.setText("Username: " + user.getUsername());
            textViewPatientEmail.setText("Email: " + user.getEmail());
        }
        // Set up the button click listener for adding a photo
        Button buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
    }

    // Open the image picker
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Handle the result from the image picker

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {
                    // Image URI is not null, proceed
                    Uri imageUri = data.getData();
                    Log.d("ImagePicker", "Image URI: " + imageUri.toString());

                    // Set the image URI to your ImageView
                    imageViewProfilePhoto.setImageURI(imageUri);

                    // You can also save the image URI to the database or perform other actions as needed.
                } else {
                    Log.e("ImagePicker", "Data or data URI is null");
                }
            } else {
                Log.e("ImagePicker", "Image picking failed with result code: " + resultCode);
            }
        }}}

