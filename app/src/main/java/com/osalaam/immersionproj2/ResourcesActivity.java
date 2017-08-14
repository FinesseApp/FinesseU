package com.osalaam.immersionproj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ResourcesActivity extends AppCompatActivity {

    Button mUpload;
    Button mHomework;
    Button mTextBook;
    Button mExams;

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



        mUpload = (Button) findViewById(R.id.uploading);
        mHomework = (Button) findViewById(R.id.homeworksource);
        mTextBook = (Button) findViewById(R.id.textbooksource);
        mExams = (Button) findViewById(R.id.examsource);

        mUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                Intent intent = new Intent(ResourcesActivity.this, UploadableActivity.class);
                intent.putExtra("class_title", className);
            /*    intent.putExtra("file_title", fileTitle);
                intent.putExtra("teacher_name", teacherName);
                intent.putExtra("file_comment", fileComment);
                intent.putExtra("file_type", fileType);*/

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

    }
}


