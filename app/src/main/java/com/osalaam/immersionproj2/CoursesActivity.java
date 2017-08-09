package com.osalaam.immersionproj2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CoursesActivity  extends AppCompatActivity {
    private ListView mListView;
    private SearchView searchView;
    private List<String> mCoursesCodes = new ArrayList<>();
    private List<String> mCoursesII = new ArrayList<>();
    private String[] mCourses = {"CSCI 101", "CSCI 241"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        Integer index = extras.getInt("index");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        try {//reads from the list of codes and puts them into a list of strings
            InputStream is = getAssets().open("Codes");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                mCoursesCodes.add(line);
                line = reader.readLine();
            }
            is.close();
        }
        catch(IOException e){}

        try {//takes one of he course codes from the list using the index passed by ClassesActivity to load class names into the mCoursesII list
            InputStream is = getAssets().open("AllClasses/" + mCoursesCodes.get(index));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                mCoursesII.add(line);
                line = reader.readLine();
            }
            is.close();
        }
        catch(IOException e){}

        mListView = (ListView) findViewById(R.id.list);
        ListAdapter course_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCoursesII);//places the content from the mCoursesII list into the listviews adapter
        mListView.setAdapter(course_list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//when an item is clicked, redirect to the resources activity
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CoursesActivity.this, ResourcesActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("OOS", query);
        }
    }
}
