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

public class FavouritesActivity extends AppCompatActivity {

    RecyclerView resultsList;
    ResultAdapter adapter;
    ArrayList<String> myArrChecked;
    Button addToFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        getSupportActionBar().setTitle("Favourite Movies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addToFavourites = findViewById(R.id.buttonFavourite);
        resultsList = findViewById(R.id.resultsList);


        ParseQuery<ParseObject> displayMovies = new ParseQuery<ParseObject>("movies");
        displayMovies.orderByAscending("title");
        displayMovies.whereEqualTo("fav", true);
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
                myArrChecked = adapter.getArrayListUnChecked();
                System.out.println(myArrChecked);
                for (int i = 0; i < myArrChecked.size(); i++)
                {
                    String moviesName = myArrChecked.get(i);
                    System.out.println(moviesName);
                    updateDataChecked(moviesName);
                }
                ParseQuery<ParseObject> movieQuery =
                        new ParseQuery<ParseObject>("movies");
                movieQuery.whereContainedIn("fav", myArrChecked);
                movieQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null) {
                            for(ParseObject movies : objects) {
                                movies.put("fav", false);
                                movies.saveInBackground();
                                Toast.makeText(FavouritesActivity.this, "Movie has successfully been removed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

    }
    // Checking if internet is available method
    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }

    // checking if device is connected to network
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void initData(List<ParseObject> objects)
    {
        adapter = new ResultAdapter(this, objects, true);
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
                                object.put("fav", false);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null)
                                        {
                                            System.out.println(movies_Name + " is updated false");
                                            Toast.makeText(FavouritesActivity.this, "Movie has been removed from favourites", Toast.LENGTH_SHORT).show();
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