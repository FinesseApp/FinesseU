package com.osalaam.immersionproj2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SearchableActivity extends AppCompatActivity {

    private String query;
    private TextView mText;

    // Get references to text views to display database data.
    private TextView mHomeworkTree;
    private TextView mTextBookTree;
    private ArrayList<TextBook> mBooks = new ArrayList<TextBook>();
    private ArrayList<Homework> mHomework = new ArrayList<Homework>();
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
        DatabaseReference homeworkRef = databaseReference.child("Homework");


        StorageReference HWRef = storageReference.child("homework");
        StorageReference imageRef = storageReference.child("images");


        imageRef.child("images/*");



        //storage refence
        // add a ValueEventListener on that path of the storage to update TextbookView.
        textBookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    mBooks.add(child.getValue(TextBook.class));
                }

                ArrayList<TextBook> result = searchTextbooks(mBooks, query.toLowerCase());

                results = "Text Book Results\n";
                for (int i = 0; i < result.size(); i++)
                {
                    results += "Title: " + result.get(i).getTitle() + "\t" + "Author: " + result.get(i).getAuthor() + "\t" + "Class: " + result.get(i).getClassTitle() + "\t" + "URL: " + result.get(i).getURL() + "\n";
                    results += "\n";
                }
                mTextBookTree.setText(results);

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

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    mHomework.add(child.getValue(Homework.class));
                }

                results = "Homework Results\n";
                ArrayList<Homework> result = searchHomework(mHomework, query.toLowerCase());
                for (int i = 0; i < result.size(); i++)
                {
                    //create URL, malformed URL exception
                    results += "Title: " + result.get(i).getTitle() + "\t" + "Author: " + result.get(i).getAuthor() + "\t" + "Class: " + result.get(i).getClassTitle() + "\t" + "URL: " + result.get(i).getURL() + "\n";
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

    protected ArrayList<Homework> searchHomework(ArrayList<Homework> homeworkList, String query)
    {
        ArrayList<Homework> result = new ArrayList<Homework>();
        for (int i = 0; i < homeworkList.size(); i++)
        {
            if (homeworkList.get(i).getAuthor().toLowerCase().contains((String) query))
            {
                result.add(homeworkList.get(i));
            }
            else if (homeworkList.get(i).getClassTitle().toLowerCase().contains((String) query))
            {
                result.add(homeworkList.get(i));
            }
            else if (homeworkList.get(i).getTitle().toLowerCase().contains((String) query))
            {
                result.add(homeworkList.get(i));
            }
            //      else if (homeworkList.get(i).getURL().toLowerCase().contains((String) query))
            //      {
            //         result.add(homeworkList.get(i));
            //     }
        }
        return result;
    }


    protected ArrayList<TextBook> searchTextbooks(ArrayList<TextBook> textbookList, String query)
    {
        ArrayList<TextBook> result = new ArrayList<TextBook>();
        for (int i = 0; i < textbookList.size(); i++)
        {
            if (textbookList.get(i).getAuthor().toLowerCase().contains((String) query))
            {
                result.add(textbookList.get(i));
            }
            else if (textbookList.get(i).getTitle().toLowerCase().contains((String) query))
            {
                result.add(textbookList.get(i));
            }
            //  else if (textbookList.get(i).getURL().toLowerCase().contains((String) query))
            // {
            //     result.add(textbookList.get(i));
            //}
            else if (textbookList.get(i).getClassTitle().toLowerCase().contains((String) query))
            {
                result.add(textbookList.get(i));
            }
        }
        return result;
    }
}
