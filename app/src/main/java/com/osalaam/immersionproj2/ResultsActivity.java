package com.osalaam.immersionproj2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ResultsActivity extends AppCompatActivity {
    private ListView mListView;
    private List<String> mResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mListView = (ListView) findViewById(R.id.list);
        ListAdapter result_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mResults);//places the content from the mCoursesII list into the listviews adapter
        mListView.setAdapter(result_list);
    }
}
