package com.osalaam.immersionproj2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class SearchableActivity extends AppCompatActivity implements View.OnClickListener {

    private SearchView searchView;

    private String query;

    private ImageView EXImage, HWImage, TBImage;
    private ListView mObjListView, mTextBookList, mExamsList;
    private List<String> mResultsAdaptee = new ArrayList<>();

    private TextView mHWClick, mTBClick, mEXClick;



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

        //TextView mText = (TextView) findViewById(R.id.resultsView);
      //  mText.setText("Search Results For" + "\t\t" + "'" + query + "'");

        //final TextView mHomeworkTree = (TextView) findViewById(R.id.homework_view);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        mObjListView = (ListView) findViewById(R.id.listhome);
        mHWClick = (TextView) findViewById(R.id.hwclick);
        mTBClick = (TextView) findViewById(R.id.tbclick);
        mEXClick = (TextView) findViewById(R.id.exclick);

        HWImage =(ImageView) findViewById(R.id.hwimage);
        TBImage = (ImageView) findViewById(R.id.tbimage);
        EXImage = (ImageView) findViewById(R.id.eximage);

        mResourceObjBooks.clear();
        mResourceObjExams.clear();
        mResourceObjHW.clear();

        DoSearch("Homework");


        HWImage.setOnClickListener(this);
        TBImage.setOnClickListener(this);
        EXImage.setOnClickListener(this);
        mHWClick.setOnClickListener(this);
        mTBClick.setOnClickListener(this);
        mEXClick.setOnClickListener(this);




        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("OOS", query);
        }

        //mSearching = (SearchView) findViewById(R.id.search2);

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("TAG", query);
                Intent intent = new Intent(getApplicationContext(), SearchableActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;

            }
        };
        searchView.setOnQueryTextListener(queryTextListener);



        //  DisplayResults();
    }

    @Override
    public void onClick(View view)
    {
        if(view == mHWClick || view == HWImage)
        {

            mResourceObjBooks.clear();
            mResourceObjExams.clear();
            mResourceObjHW.clear();

            DoSearch("Homework");


        }
        else if(view == mTBClick || view == TBImage)
        {

            mResourceObjBooks.clear();
            mResourceObjExams.clear();
            mResourceObjHW.clear();

            DoSearch("Text Books");


        }
        else if(view == mEXClick || view == EXImage)
        {

            mResourceObjBooks.clear();
            mResourceObjExams.clear();
            mResourceObjHW.clear();

            DoSearch("Exams");


        }
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
                    results += " Title: " + ResultList.get(i).getTitle() + "\n" + " Teacher: " + ResultList.get(i).getAuthor() + "\n" + " Class: " + ResultList.get(i).getClassTitle() + "\n" + " URL: " + ResultList.get(i).getURL() +"\n";
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
                    results += " Title: " + ResultList.get(i).getTitle() + "\n" + " Teacher: " + ResultList.get(i).getAuthor() + "\n" + " Class: " + ResultList.get(i).getClassTitle() + "\n" + " URL: " + ResultList.get(i).getURL() +"\n";
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
                    results += " Title: " + ResultList.get(i).getTitle() + "\n" + " Teacher: " + ResultList.get(i).getAuthor() + "\n" + " Class: " + ResultList.get(i).getClassTitle() + "\n" + " URL: " + ResultList.get(i).getURL() +"\n";
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
