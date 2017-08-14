package com.osalaam.immersionproj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ResourcesActivity extends AppCompatActivity {

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



        mUpload = (Button) findViewById(R.id.uploading);
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


    }
}


