package com.osalaam.immersionproj2;

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
import android.widget.TextView;


public class CoursesActivity  extends AppCompatActivity {
    private ListView mListView;
    private String[] mCourses = {"CSCI 101", "CSCI 241"};
    private SearchView mSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mListView = (ListView) findViewById(R.id.list);
        ListAdapter course_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCourses);
        mListView.setAdapter(course_list);
        int count = mListView.getChildCount();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(CoursesActivity.this, ResourcesActivity.class);
                    startActivity(intent);
                }
        });

        SearchView mSearching = (SearchView) findViewById(R.id.search2);

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("TAG", query);
                Intent intent = new Intent(getApplicationContext(), SearchableActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;

            }
        };
       mSearching.setOnQueryTextListener(queryTextListener);

    }

}
