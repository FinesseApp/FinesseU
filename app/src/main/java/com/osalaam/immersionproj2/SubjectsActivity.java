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

public class SubjectsActivity extends AppCompatActivity{
    private ListView mListView;
    private SearchView mSearching;
    private List<String> mSubjects = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);

        try {
            InputStream is = getAssets().open("SubjectListResult");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                mSubjects.add(line);
                line = reader.readLine();
            }
            is.close();
        }
        catch(IOException e){}

        mListView = (ListView) findViewById(R.id.listv);
        ListAdapter subject_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSubjects);
        mListView.setAdapter(subject_list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SubjectsActivity.this, CoursesActivity.class);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });


        mSearching = (SearchView) findViewById(R.id.search);

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
