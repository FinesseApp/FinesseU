package com.osalaam.immersionproj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ResourcesActivity extends AppCompatActivity {

    Button mUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);


        mUpload = (Button) findViewById(R.id.uploading);

        mUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ResourcesActivity.this, UploadableActivity.class);
                startActivity(intent);
            }
        });


    }
}
