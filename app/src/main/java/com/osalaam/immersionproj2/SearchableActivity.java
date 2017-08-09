package com.osalaam.immersionproj2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SearchableActivity extends AppCompatActivity {

    private String query;
    private TextView mText;

    // Get references to text views to display database data.
    private TextView mHomeworkTree;
    private TextView mTextBookTree;
    private ArrayList<ResourceObj> mResourceObjBooks = new ArrayList<ResourceObj>();
    private ArrayList<ResourceObj> mResourceObjExams = new ArrayList<ResourceObj>();
    private ArrayList<ResourceObj> mResourceObjHW = new ArrayList<ResourceObj>();

    private String results;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();




    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        query = getIntent().getStringExtra("query");

        TextView mText = (TextView) findViewById(R.id.resultsView);
        mText.setText("Search Results For" + "\t\t" + "'" + query + "'");

        final TextView mHomeworkTree = (TextView) findViewById(R.id.homework_view);
        final TextView mTextBookTree = (TextView) findViewById(R.id.text_book_view);
        final TextView mExamsTree = (TextView) findViewById(R.id.exams_view);




       /* // add a ValueEventListener on the ROOT of the database, to update the rootTextView.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mHomeworkTree.setText(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mHomeworkTree.setText(databaseError.toString());
            }
        });

        // add a ValueEventListener on the ROOT of the database, to update the rootTextView.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTextBookTree.setText(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mTextBookTree.setText(databaseError.toString());
            }
        });  */


        //Specific Path Reference:
        DatabaseReference textBookRef = databaseReference.child("Text Book");
        final DatabaseReference homeworkRef = databaseReference.child("Homework");
        DatabaseReference examsRef = databaseReference.child("Exams");




        // add a ValueEventListener on that path of the storage to update TextbookView.
        textBookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    mResourceObjBooks.add(child.getValue(ResourceObj.class));
                }

                ArrayList<ResourceObj> result = searchResources(mResourceObjBooks, query.toLowerCase());

                results = "Text Book Results\n";
                for (int i = 0; i < result.size(); i++)
                {
                    results += "Title: " + result.get(i).getTitle() + "\t" + "Author: " + result.get(i).getAuthor() + "\t" + "Class: " + result.get(i).getClassTitle() + "\t" + " URL: " + result.get(i).getURL();
                    results += "\n";
                }
                mTextBookTree.setText(results);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mTextBookTree.setText(databaseError.toString());
            }
        });

        // add a ValueEventListener on that path of the storage to update TextbookView.
        examsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    mResourceObjExams.add(child.getValue(ResourceObj.class));
                }

                ArrayList<ResourceObj> result = searchResources(mResourceObjExams, query.toLowerCase());

                results = "Exam Results\n";
                for (int i = 0; i < result.size(); i++)
                {
                    results += "Title: " + result.get(i).getTitle() + "\t" + "Author: " + result.get(i).getAuthor() + "\t" + "Class: " + result.get(i).getClassTitle() + "\t" + " URL: " + result.get(i).getURL();
                    results += "\n";
                }
                mExamsTree.setText(results);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mTextBookTree.setText(databaseError.toString());
            }
        });

        // add a ValueEventListener on that path of the database, to update HomeworkView.
        homeworkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                String test = "";
                int counter = 0;
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    mResourceObjHW.add(child.getValue(ResourceObj.class));
                    test = child.child("urlLink").getValue().toString();
                    mResourceObjHW.get(counter).setURL(test);
                  //System.out.println("--------------------" + mResourceObjHW.get(0).getURL());

                }

                ArrayList<ResourceObj> result = searchResources(mResourceObjHW, query.toLowerCase());

                results = "Homework Results\n";
                for (int i = 0; i < result.size(); i++)
                {
                    results += "Title: " + result.get(i).getTitle() + "\t" + "Author: " + result.get(i).getAuthor() + "\t" + "Class: " + result.get(i).getClassTitle() + "\t" + " URL: " + result.get(i).getURL() + " TEST:" + test;
                    results += "\n";
                }
                mHomeworkTree.setText(results);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                mHomeworkTree.setText(databaseError.toString());
            }
        });
    }

    protected ArrayList<ResourceObj> searchResources(ArrayList<ResourceObj> resourceList, String query)
    {
        ArrayList<ResourceObj> result = new ArrayList<ResourceObj>();
        for (int i = 0; i < resourceList.size(); i++)
        {
            if (resourceList.get(i).getAuthor().toLowerCase().contains((String) query))
            {
                result.add(resourceList.get(i));
            }
            else if (resourceList.get(i).getClassTitle().toLowerCase().contains((String) query))
            {
                result.add(resourceList.get(i));
            }
            else if (resourceList.get(i).getTitle().toLowerCase().contains((String) query))
            {
                result.add(resourceList.get(i));
            }
            //      else if (homeworkList.get(i).getURL().toLowerCase().contains((String) query))
            //      {
            //         result.add(homeworkList.get(i));
            //     }
        }
        return result;
    }
}
