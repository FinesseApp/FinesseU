package com.osalaam.immersionproj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;


public class ClassesActivity extends AppCompatActivity{

    private SearchView mSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        GridLayout mGrid = (GridLayout) findViewById(R.id.grid);
        int count = mGrid.getChildCount();
        for(int i = 0; i < count; i++) {
            final Button mClassesButton;
            mClassesButton = (Button) mGrid.getChildAt(i);
            mClassesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ClassesActivity.this, CoursesActivity.class);
                    startActivity(intent);
                }
            });
        }


        SearchView mSearching = (SearchView) findViewById(R.id.search);

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
