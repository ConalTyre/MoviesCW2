package com.example.moviescw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EditMovies extends AppCompatActivity {
    EditText edtTitle, edtYear, edtDirector, edtActors, edtRating, edtReview, edtFav;
    Button btnSave;
    ;
    String title;
    String year, rating;
    String director;
    String actors;
    String review;
    String originalTitle;
    String Fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);
        getSupportActionBar().setTitle("Edit Movies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtTitle = findViewById(R.id.edtTitle);
        edtYear = findViewById(R.id.edtYear);
        edtDirector = findViewById(R.id.edtDirector);
        edtRating = findViewById(R.id.edtRating);
        edtReview = findViewById(R.id.edtReview);
        btnSave = findViewById(R.id.btnUpdate);
        edtActors = findViewById(R.id.edtActors);
        edtFav = findViewById(R.id.editTextFav);


        Intent intent = getIntent();

        originalTitle = intent.getStringExtra("title");
        year = intent.getStringExtra("year");
        System.out.println("Title" + originalTitle);
        System.out.println("Title" + year);
        edtTitle.setText(originalTitle);
        edtYear.setText(intent.getStringExtra("year"));
        edtDirector.setText(intent.getStringExtra("director"));
        edtActors.setText(intent.getStringExtra("actors"));
        edtRating.setText(intent.getStringExtra("rating"));
        edtReview.setText(intent.getStringExtra("review"));
        edtFav.setText(intent.getStringExtra("fav"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = edtTitle.getText().toString();
                year = edtYear.getText().toString();
                director = edtDirector.getText().toString();
                actors = edtActors.getText().toString();
                rating = edtRating.getText().toString();
                review = edtReview.getText().toString();
                Fav = edtFav.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    edtTitle.setError("Please enter Title");
                } else if (TextUtils.isEmpty(year)) {
                    edtYear.setError("Please enter year");
                } else if (TextUtils.isEmpty(director)) {
                    edtDirector.setError("Please enter director");
                } else if (TextUtils.isEmpty(actors)) {
                    edtActors.setError("Please enter actors");
                } else if (TextUtils.isEmpty(rating)) {
                    edtRating.setError("Please enter rating");
                } else if (TextUtils.isEmpty(review)) {
                    edtReview.setError("Please enter review");
                } else {
                    // calling method to update data.
                    addDataToDatabase(title, year, director, actors, rating, review);
                }
            }
        });
    }

    private void addDataToDatabase(String title, String year, String director, String actors, String rating, String review) {
        // Config Query
        ParseQuery<ParseObject> registerMovie = ParseQuery.getQuery("movies");
        registerMovie.whereEqualTo("title", title);
        registerMovie.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String objectID = object.getObjectId().toString();

                    registerMovie.getInBackground(objectID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                object.put("title", title);
                                object.put("year", year);
                                object.put("director", director);
                                object.put("actors", actors);
                                object.put("rating", rating);
                                object.put("review", review);
                                object.put("fav", Fav);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "Movie has been updated", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });
    }
}
