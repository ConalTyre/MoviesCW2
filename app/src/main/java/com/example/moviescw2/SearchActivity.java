package com.example.moviescw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    Button Lookup;
    RecyclerView resultsList;
    EditText searchMovie;
    ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search Movie");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultsList = findViewById (R.id.resultsList);
        searchMovie = findViewById(R.id.editTxtSearch);
        Lookup = findViewById(R.id.btnLookup);

        ParseQuery<ParseObject> displayMovie = new ParseQuery<ParseObject>("movies");

        Lookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchMovie.getText().toString();
                getMovie(search);
            }
        });
    }

    public void getMovie(String search){

        String pattern = "^.*" + search + ".*$";

        ParseQuery<ParseObject> query = new ParseQuery<>("movies");
        query.whereMatches("title", pattern, "i");

        ParseQuery<ParseObject> query2 = new ParseQuery<>("movies");
        query2.whereMatches("director", pattern, "i");

        ParseQuery<ParseObject> query3 = new ParseQuery<>("movies");
        query3.whereMatches("actors", pattern, "i");

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query);
        queries.add(query2);
        queries.add(query3);

        ParseQuery<ParseObject> finalQuery = ParseQuery.or(queries);
        finalQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    initData(objects);
                    if(objects.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No Movie available", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("fetching " + objects.toString());
                } else {
                    Log.d("ParseQuery", e.getMessage());
                }
            }
        });
    }

    public void initData(List<ParseObject> objects)
    {
        adapter = new ResultAdapter(this, objects, false);
        resultsList.setLayoutManager(new LinearLayoutManager(this));
        resultsList.setAdapter(adapter);
    }
}