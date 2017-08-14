package com.osalaam.immersionproj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.widget.EditText;
import android.widget.Spinner;


public class ResourcesActivity extends AppCompatActivity {
    private List<String> mResults = new ArrayList<>();

    Button mUpload;
    String className = "";
    String fileTitle = "";
    String teacherName = "";
    String fileComment = "";
    String fileType = "";

//The fuck move all of these to UploadAbleActivity...this should be able to display a list of resources
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        className = getIntent().getStringExtra("class_name");

        final EditText mGetFileTitle = (EditText) findViewById(R.id.getfiletitle);

        final EditText mGetTeacher = (EditText) findViewById(R.id.getteacher);

        final EditText mGetComment = (EditText) findViewById(R.id.getcomment);

//get the spinner from the xml.
        final Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"Homework", "Text Book", "Exams"};
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                fileType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        mUpload = (Button) findViewById(R.id.uploading);
        mUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                fileTitle = mGetFileTitle.getText().toString(); //encodes edit text texts as intents
                teacherName = mGetTeacher.getText().toString();
                fileComment = mGetComment.getText().toString();


                Intent intent = new Intent(ResourcesActivity.this, UploadableActivity.class);
                intent.putExtra("class_title", className);
                intent.putExtra("file_title", fileTitle);
                intent.putExtra("teacher_name", teacherName);
                intent.putExtra("file_comment", fileComment);
                intent.putExtra("file_type", fileType);

                startActivity(intent);
            }
        });

        ListView mListView = (ListView) findViewById(R.id.list);
        ListAdapter results_list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mResults);//places the content from the mCoursesII list into the listviews adapter
        mListView.setAdapter(results_list);

    }
}


