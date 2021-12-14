package com.example.moviescw2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class UpdateFilms extends AppCompatActivity {

    // Creating Variables for Entering Details
    private EditText Titletxt, Yeartxt, Directortxt, ActorsTxt, Ratingtxt, Reviewtxt;

    // Creating String for stroing values from textFields
    private String title, year, director, actors, rating, review;

    // Creating variable for save button
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_films);
        getSupportActionBar().setTitle("Update Movie");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initalising textViews and Buttons
        Titletxt = findViewById(R.id.edtTitle);
        Yeartxt = findViewById(R.id.edtYear);
        Directortxt = findViewById(R.id.edtDirector);
        ActorsTxt = findViewById(R.id.edtActors);
        Ratingtxt = findViewById(R.id.edtRating);
        Reviewtxt = findViewById(R.id.edtReview);
        btnUpdate = findViewById(R.id.btnUpdate);

        // on below line we are setting data to our edit text field.
        Titletxt.setText(getIntent().getStringExtra("Title"));
        Yeartxt.setText(getIntent().getStringExtra("Year"));
        Directortxt.setText(getIntent().getStringExtra("Director"));
        ActorsTxt.setText(getIntent().getStringExtra("Actor"));
        Reviewtxt.setText(getIntent().getStringExtra("Review"));
        Ratingtxt.setText(getIntent().getStringExtra("Rating"));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = Titletxt.getText().toString();
                year = Yeartxt.getText().toString();
                director = Directortxt.getText().toString();
                actors = ActorsTxt.getText().toString();
                review = Reviewtxt.getText().toString();
                rating = Ratingtxt.getText().toString();
                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(title)) {
                    Titletxt.setError("Please enter Title");
                } else if (TextUtils.isEmpty(year)) {
                    Yeartxt.setError("Please enter year");
                } else if (TextUtils.isEmpty(director)) {
                    Directortxt.setError("Please enter director");
                } else if (TextUtils.isEmpty(actors)) {
                    ActorsTxt.setError("Please enter actors");
                } else if (TextUtils.isEmpty(rating)) {
                    Ratingtxt.setError("Please enter rating");
                } else if (TextUtils.isEmpty(review)) {
                    Reviewtxt.setError("Please enter review");
                } else {
                    // calling method to update data.
                    updateDataChecked(title, year, director, actors, rating, review);
                }
            }
        });
    }

        private void updateDataChecked(String title , String year, String director, String actors, String rating, String review )
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("movies");
            query.whereEqualTo("title", title);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null)
                    {
                        String objectID = object.getObjectId().toString();
                        query.getInBackground(objectID, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null)
                                {
                                    object.put("title", title);
                                    object.put("year", year);
                                    object.put("director", director);
                                    object.put("actors", actors);
                                    object.put("review", review);
                                    object.put("rating", rating);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null)
                                            {
                                                Toast.makeText(UpdateFilms.this, "Course Updated..", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(UpdateFilms.this, EditMovies.class);
                                                startActivity(i);
                                            } else {
                                                // below line is for error handling.
                                                Toast.makeText(UpdateFilms.this, "Fail to update data " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(UpdateFilms.this, "Fail to update course " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(UpdateFilms.this, "Fail to get object ID..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

}

