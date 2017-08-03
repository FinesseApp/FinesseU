package com.osalaam.immersionproj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;


public class ClassesActivity extends AppCompatActivity{

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
    }
}
