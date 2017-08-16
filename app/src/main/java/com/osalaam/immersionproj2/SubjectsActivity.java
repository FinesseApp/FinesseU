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
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectsActivity extends AppCompatActivity{
    private GridView mGridView;
    private SearchView mSearching;
    private List<String> mSubjects = new ArrayList<>();

    private Map<String, List<String>> mSubjectsMap = new HashMap<>();

    private List<String> mFilterSubjects = new ArrayList<>();
    private List<String> mLetters = new ArrayList<>();

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

        String curletter;
        mLetters.add("ALL");
        mSubjectsMap.put("ALL", mSubjects);
        for (String item : mSubjects){
            curletter = item.substring(0,1);
            if(mSubjectsMap.containsKey(curletter)){
                mSubjectsMap.get(curletter).add(item);
            }
            else{
                List<String> curitem = new ArrayList<>();
                mLetters.add(item.substring(0,1));
                curitem.add(item);
                mSubjectsMap.put(curletter, curitem);
            }
        }

        mFilterSubjects = mSubjects;
        mGridView = (GridView) findViewById(R.id.listv);
        ListAdapter subject_list = new ArrayAdapter<String>(this, R.layout.grid_item, mFilterSubjects);
        mGridView.setAdapter(subject_list);

        final Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mLetters);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mFilterSubjects = mSubjectsMap.get(mLetters.get(position));
                ListAdapter subject_list_two = new ArrayAdapter<String>(getApplicationContext(), R.layout.grid_item, mFilterSubjects);
                mGridView.setAdapter(subject_list_two);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SubjectsActivity.this, CoursesActivity.class);
                for(int j = 0; j < mSubjects.size(); j++) {
                    if (mSubjects.get(j).equals(mFilterSubjects.get(i))) {
                        intent.putExtra("index", j);
                        intent.putExtra("subject_name", mSubjects.get(i));

                        startActivity(intent);
                    }
                }
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