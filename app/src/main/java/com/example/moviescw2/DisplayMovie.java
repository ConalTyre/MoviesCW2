package com.example.moviescw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class DisplayMovie extends AppCompatActivity {
    Button addToFavourites;
    RecyclerView resultsList;
    ResultAdapter adapter;
    ArrayList<String> ArrChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);
        getSupportActionBar().setTitle("Display Movies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addToFavourites = findViewById(R.id.buttonFavourite);
        resultsList = findViewById(R.id.resultsList);


            ParseQuery<ParseObject> displayMovies = new ParseQuery<ParseObject>("movies");
            displayMovies.orderByAscending("title");
            displayMovies.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objList, ParseException e) {
                    if (e == null)
                    {
                        System.out.println(" Size " + objList.size());
                        initData(objList);
                    }
                    else {
                        Log.d("ParseQuery ", e.getMessage());
                    }
                }
            });

        // Set listener for favourite button
        addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrChecked = adapter.getListChecked();
                System.out.println(ArrChecked);
                for (int i = 0; i < ArrChecked.size(); i++)
                {
                    String moviesName = ArrChecked.get(i);
                    System.out.println(moviesName);
                    updateDataChecked(moviesName);
                }
            }
        });

    }


    // checking if device is connected to network
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void initData(List<ParseObject> objects)
    {
        adapter = new ResultAdapter(this, objects, false);
        resultsList.setLayoutManager(new LinearLayoutManager(this));
        resultsList.setAdapter(adapter);
    }

    private void updateDataChecked(String movies_Name)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("movies");
        query.whereEqualTo("title", movies_Name);
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
                                object.put("fav", true);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null)
                                        {
                                            System.out.println(movies_Name + " is updated true");
                                            Toast.makeText(DisplayMovie.this, "Movie has been added to fsvourites", Toast.LENGTH_SHORT).show();
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