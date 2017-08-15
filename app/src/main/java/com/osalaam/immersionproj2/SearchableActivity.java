package com.osalaam.immersionproj2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SearchableActivity extends AppCompatActivity {

    private String query;

    private ListView mHomeworkList, mTextBookList, mExamsList;
    private List<String> mResultsHome = new ArrayList<>();

    private List<String> mResultsExams = new ArrayList<>();

    private List<String> mResultsTexts = new ArrayList<>();



    private ArrayList<ResourceObj> mResourceObjBooks = new ArrayList<ResourceObj>();
    private ArrayList<ResourceObj> mResourceObjExams = new ArrayList<ResourceObj>();
    private ArrayList<ResourceObj> mResourceObjHW = new ArrayList<ResourceObj>();

    String results;




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

        mExamsList = (ListView) findViewById(R.id.listexam);
        mHomeworkList = (ListView) findViewById(R.id.listhome);
        mTextBookList = (ListView) findViewById(R.id.listtext);





        //Specific Path Reference:
        DatabaseReference textBookRef = databaseReference.child("Text Books");
        DatabaseReference homeworkRef = databaseReference.child("Homework");
        DatabaseReference examsRef = databaseReference.child("Exams");




        // add a ValueEventListener on that path of the storage to update TextbookView.
        textBookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String test = "";
                int counter = 0;
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    mResourceObjBooks.add(child.getValue(ResourceObj.class));
                    test = child.child("urlLink").getValue().toString();
                    mResourceObjBooks.get(counter).setURL(test);
                    counter++;
                }

                ArrayList<ResourceObj> ResultList = searchResources(mResourceObjBooks, query.toLowerCase());

                results = "Text Book Results\n\n";
                for (int i = 0; i < ResultList.size(); i++)
                {
                    results = "";
                    results += " Title: " + ResultList.get(i).getTitle() + "\t" + " Teacher: " + ResultList.get(i).getAuthor() + "\t" + " Class: " + ResultList.get(i).getClassTitle() + "\t" + " URL: " + ResultList.get(i).getURL() +"\n";
                    results += "\n";
                    mResultsTexts.add(results);

                }

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
                String test = "";
                int counter = 0;
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    mResourceObjExams.add(child.getValue(ResourceObj.class));
                    test = child.child("urlLink").getValue().toString();
                    mResourceObjExams.get(counter).setURL(test);
                    counter++;
                }

                ArrayList<ResourceObj> ResultList = searchResources(mResourceObjExams, query.toLowerCase());

                results = "Exam Results\n\n";
                for (int i = 0; i < ResultList.size(); i++)
                {
                    results = "";
                    results += " Title: " + ResultList.get(i).getTitle() + "\t" + " Teacher: " + ResultList.get(i).getAuthor() + "\t" + " Class: " + ResultList.get(i).getClassTitle() + "\t" + " URL: " + ResultList.get(i).getURL() +"\n";
                    results += "\n";
                    mResultsExams.add(results);

                }

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
                    counter++;
                }

                ArrayList<ResourceObj> ResultList = searchResources(mResourceObjHW, query.toLowerCase());

                results = "Homework Results\n\n";
                for (int i = 0; i < ResultList.size(); i++)
                {
                    results = "";
                    results += " Title: " + ResultList.get(i).getTitle() + "\t" + " Teacher: " + ResultList.get(i).getAuthor() + "\t" + " Class: " + ResultList.get(i).getClassTitle() + "\t" + " URL: " + ResultList.get(i).getURL() +"\n";
                    results += "\n";
                    mResultsHome.add(results);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                mHomeworkTree.setText(databaseError.toString());
            }
        });


        ListAdapter result_list = new ArrayAdapter<String>(this, R.layout.resource_list_item, mResultsTexts);//places the content from the mCoursesII list into the listviews adapter
        mTextBookList.setAdapter(result_list);
        ListAdapter result_list2 = new ArrayAdapter<String>(this, R.layout.resource_list_item, mResultsExams);//places the content from the mCoursesII list into the listviews adapter
        mExamsList.setAdapter(result_list2);
        ListAdapter result_list3 = new ArrayAdapter<String>(this, R.layout.resource_list_item, mResultsHome);//places the content from the mCoursesII list into the listviews adapter
        mHomeworkList.setAdapter(result_list3);
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
        }
        return result;
    }
}
