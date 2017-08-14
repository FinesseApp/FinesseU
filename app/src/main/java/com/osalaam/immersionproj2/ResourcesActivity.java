package com.osalaam.immersionproj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ResourcesActivity extends AppCompatActivity {
    private List<String> mResults = new ArrayList<>();

    Button mUpload;
    String className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        className = getIntent().getStringExtra("class_name");

        mUpload = (Button) findViewById(R.id.uploading);
        Log.i("CLASS-----------", className);

        mUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResourcesActivity.this, UploadableActivity.class);
                intent.putExtra("class_title", className);
                startActivity(intent);
            }
        });

        ListView mListView = (ListView) findViewById(R.id.list);
        ListAdapter results_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mResults);//places the content from the mCoursesII list into the listviews adapter
        mListView.setAdapter(results_list);

    }
}
