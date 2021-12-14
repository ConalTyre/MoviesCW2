package com.example.moviescw2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class RegisterMovie extends AppCompatActivity {

    // Creating Variables for Entering Details
    private EditText Titletxt, Yeartxt, Directortxt, ActorsTxt, Ratingtxt, Reviewtxt;

    // Creating String for stroing values from textFields
    private String title, year, director, actors, rating, review;

    private int mYear, intRating;

    // Creating variable for save button
    private Button Savebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);
        getSupportActionBar().setTitle("Register Movie");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initalising textViews and Buttons
        Titletxt = findViewById(R.id.edtTitle);
        Yeartxt = findViewById(R.id.edtYear);
        Directortxt = findViewById(R.id.edtDirector);
        ActorsTxt = findViewById(R.id.edtActors);
        Ratingtxt = findViewById(R.id.edtRating);
        Reviewtxt = findViewById(R.id.edtReview);
        Savebtn = findViewById(R.id.btnUpdate);

        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from editText fields.
                title = Titletxt.getText().toString();
                year = Yeartxt.getText().toString();
                director = Directortxt.getText().toString();
                actors = ActorsTxt.getText().toString();
                rating = Ratingtxt.getText().toString();
                review = Reviewtxt.getText().toString();
                mYear = Integer.parseInt(Yeartxt.getText().toString());
                intRating = Integer.parseInt(Ratingtxt.getText().toString());

                // Validating if the fields are empty or not
                if (TextUtils.isEmpty(title))
                {
                    Titletxt.setError("Please Enter Title");
                }

                if ( TextUtils.isEmpty(year))
                {
                    Yeartxt.requestFocus();
                    Yeartxt.setError("Please Enter Year");
                }
                else if ( mYear < 1895)
                {
                    Yeartxt.requestFocus();
                    Yeartxt.setError("Year cannot be less than 1895");
                }
                else if (TextUtils.isEmpty(director))
                {
                    Directortxt.requestFocus();
                    Directortxt.setError("Please Enter Director");
                }

                else if (TextUtils.isEmpty(actors))
                {
                    ActorsTxt.requestFocus();
                    ActorsTxt.setError("Please Enter Actors");
                }

                else if (TextUtils.isEmpty(rating))
                {
                    Ratingtxt.requestFocus();
                    Ratingtxt.setError("Please Enter Rating between 1 - 10 ");
                }

                else if (intRating < 1 || intRating > 10)
                {
                    Ratingtxt.requestFocus();
                    Ratingtxt.setError("Rating must be between 1 & 10");
                }

                else if (TextUtils.isEmpty(review))
                {
                    Reviewtxt.requestFocus();
                    Reviewtxt.setError("Please Enter Review");
                }

                else {
                    // calling method to add data to database
                    addDataToDatabase(title, year, director, actors, rating, review);
                }
            }
        });
    }



    private void addDataToDatabase(String title, String year, String director, String actors, String rating, String review)
    {
        // Config Query
        ParseObject registerMovie = new ParseObject("movies");

        // Data Values
        registerMovie.put("title", title);
        registerMovie.put("year", year);
        registerMovie.put("director", director);
        registerMovie.put("actors", actors);
        registerMovie.put("rating", rating);
        registerMovie.put("review", review);

        // Method to save data in DB
        registerMovie.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    // Toast message
                    Toast.makeText(RegisterMovie.this, "Data has been successfully entered to Database", Toast.LENGTH_SHORT).show();

                    // Setting edit text fields to empty values
                    Titletxt.setText("");
                    Yeartxt.setText("");
                    Directortxt.setText("");
                    ActorsTxt.setText("");
                    Ratingtxt.setText("");
                    Reviewtxt.setText("");
                }
                else
                {
                    // Display Error message to user
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}