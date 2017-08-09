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


public class CoursesActivity  extends AppCompatActivity {
    private ListView mListView;
    private SearchView searchView;
    private String[] mCourses = {"CSCI 101", "CSCI 241"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mListView = (ListView) findViewById(R.id.list);
        ListAdapter course_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCourses);
        mListView.setAdapter(course_list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
