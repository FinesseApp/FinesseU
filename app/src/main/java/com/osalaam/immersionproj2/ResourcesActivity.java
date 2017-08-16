package com.osalaam.immersionproj2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResourcesActivity extends AppCompatActivity {
    private SearchView searchView;

    Button mUpload;
    Button mHomework;
    Button mTextBook;
    Button mExams;

    private TextView headerText;
    String className = "";

    //The fuck move all of these to UploadAbleActivity...this should be able to display a list of resources
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        className = getIntent().getStringExtra("class_name");


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        mUpload = (Button) findViewById(R.id.uploading);
        mHomework = (Button) findViewById(R.id.homeworksource);
        mTextBook = (Button) findViewById(R.id.textbooksource);
        mExams = (Button) findViewById(R.id.examsource);

        headerText = (TextView) findViewById(R.id.resourcesHeaderText);
        headerText.setText(className);

        mUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                Intent intent = new Intent(ResourcesActivity.this, UploadableActivity.class);
                intent.putExtra("class_title", className);

                startActivity(intent);
            }
        });

        View.OnClickListener results_listener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResourcesActivity.this, ResultsActivity.class);
                intent.putExtra("class_title", className);
                Button b = (Button) view;
                intent.putExtra("resource_type", b.getText().toString());
                startActivity(intent);
            }
        };

        mHomework.setOnClickListener(results_listener);

        mTextBook.setOnClickListener(results_listener);

        mExams.setOnClickListener(results_listener);


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("OOS", query);
        }

        //mSearching = (SearchView) findViewById(R.id.search2);

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
        searchView.setOnQueryTextListener(queryTextListener);
    }
 }



