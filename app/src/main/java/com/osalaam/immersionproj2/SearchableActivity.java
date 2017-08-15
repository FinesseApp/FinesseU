package com.osalaam.immersionproj2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchableActivity extends AppCompatActivity {

    private String query;

    private ListView mObjListView, mTextBookList, mExamsList;
    private List<String> mResultsAdaptee = new ArrayList<>();

    private List<String> mResultsExams = new ArrayList<>();

    private List<String> mResultsBooks = new ArrayList<>();



    private ArrayList<ResourceObj> mResourceObjBooks = new ArrayList<ResourceObj>();
    private ArrayList<ResourceObj> mResourceObjExams = new ArrayList<ResourceObj>();
    private ArrayList<ResourceObj> mResourceObjHW = new ArrayList<ResourceObj>();

    String results = "";
    String fileType ="";




    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        query = getIntent().getStringExtra("query");

        TextView mText = (TextView) findViewById(R.id.resultsView);
        mText.setText("Search Results For" + "\t\t" + "'" + query + "'");

        //final TextView mHomeworkTree = (TextView) findViewById(R.id.homework_view);


        mObjListView = (ListView) findViewById(R.id.listhome);





        //Specific Path Reference:



//get the spinner from the xml.
        final Spinner dropdown = (Spinner)findViewById(R.id.dropdowntype);
//create a list of items for the spinner.
        String[] items = new String[]{"Homework", "Text Books", "Exams"};
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        //  dropdown.setPrompt("Select A File Type");

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                fileType = (String) parent.getItemAtPosition(position);

                mResourceObjBooks.clear();
                mResourceObjExams.clear();
                mResourceObjHW.clear();

                DoSearch(fileType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });





        //  DisplayResults();
    }

    protected void DoSearch(String fileTypes)
    {
        if (fileTypes.equals("Text Books"))
        {
            SearchText();


        }
        else if (fileTypes.equals("Homework"))
        {
            SearchHW();
        }
        else if (fileTypes.equals("Exams"))
        {
            SearchExams();
        }

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

    protected void DisplayResults(){
        ListAdapter result_list = new ArrayAdapter<String>(this, R.layout.resource_list_item, mResultsAdaptee);//places the content from the mCoursesII list into the listviews adapter
        mObjListView.setAdapter(result_list);
    }
    protected void SearchText()
    {

        DatabaseReference textBookRef = databaseReference.child("Text Books");
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
                mResultsAdaptee.clear();
                for (int i = 0; i < ResultList.size(); i++)
                {
                    results = "";
                    results += " Title: " + ResultList.get(i).getTitle() + "\t" + " Teacher: " + ResultList.get(i).getAuthor() + "\t" + " Class: " + ResultList.get(i).getClassTitle() + "\t" + " URL: " + ResultList.get(i).getURL() +"\n";
                    results += "\n";
                    mResultsAdaptee.add(results);

                }
                DisplayResults();
                //  mTextBookTree.setText(results);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // mTextBookTree.setText(databaseError.toString());
            }
        });
    }

    protected void SearchExams()
    {
        DatabaseReference examsRef = databaseReference.child("Exams");

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
                mResultsAdaptee.clear();
                for (int i = 0; i < ResultList.size(); i++)
                {
                    results = "";
                    results += " Title: " + ResultList.get(i).getTitle() + "\t" + " Teacher: " + ResultList.get(i).getAuthor() + "\t" + " Class: " + ResultList.get(i).getClassTitle() + "\t" + " URL: " + ResultList.get(i).getURL() +"\n";
                    results += "\n";
                    mResultsAdaptee.add(results);

                }
                DisplayResults();
                //  mTextBookTree.setText(results);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   mTextBookTree.setText(databaseError.toString());
            }
        });


    }

    protected void SearchHW()
    {
        DatabaseReference homeworkRef = databaseReference.child("Homework");
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
                mResultsAdaptee.clear();
                for (int i = 0; i < ResultList.size(); i++)
                {
                    results = "";
                    results += " Title: " + ResultList.get(i).getTitle() + "\t" + " Teacher: " + ResultList.get(i).getAuthor() + "\t" + " Class: " + ResultList.get(i).getClassTitle() + "\t" + " URL: " + ResultList.get(i).getURL() +"\n";
                    results += "\n";
                    mResultsAdaptee.add(results);

                }
                DisplayResults();
                //  mHomeworkTree.setText(results);


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                //  mHomeworkTree.setText(databaseError.toString());
            }
        });


    }


}
