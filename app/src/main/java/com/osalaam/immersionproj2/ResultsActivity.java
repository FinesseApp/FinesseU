package com.osalaam.immersionproj2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ResultsActivity extends AppCompatActivity {
    private SearchView searchView;


    private ListView mListView;
    private List<String> mResults = new ArrayList<>();

    private ArrayList<ResourceObj> mResourceObjList = new ArrayList<ResourceObj>();


    String results;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        final String mType = getIntent().getStringExtra("resource_type");

        final String mClass  = getIntent().getStringExtra("class_title");


        mListView = (ListView) findViewById(R.id.list);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));



        DatabaseReference resourceRef = databaseReference.child(mType);

        resourceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String test = "";
                int counter = 0;
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    mResourceObjList.add(child.getValue(ResourceObj.class));
                    test = child.child("urlLink").getValue().toString();
                    mResourceObjList.get(counter).setURL(test);
                    counter++;
                }

                ArrayList<ResourceObj> ResultList = searchResources(mResourceObjList, mClass.toLowerCase());


                for (int i = 0; i < ResultList.size(); i++)
                {
                    results = "";
                //    Log.i("TEST------------", "A");

                    results += " Title: " + ResultList.get(i).getTitle() + "\n" + " Teacher: " + ResultList.get(i).getAuthor() + "\n" + " Class: " + ResultList.get(i).getClassTitle() + "\n" + " URL: " + ResultList.get(i).getURL() +"\n";
                    mResults.add(results);
                }
                if (ResultList.size() == 0)
                {
                    mResults.add("No Results Found. Try Uploading A File Of Type: " + mType + "!");
                }
                DisplayResults();
              //  mTextBookTree.setText(mReslt);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
               // mTextBookTree.setText(databaseError.toString());
            }
        });




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
    }


    protected void DisplayResults()
    {
        ListAdapter result_list = new ArrayAdapter<String>(this, R.layout.resource_list_item, mResults);//places the content from the mCoursesII list into the listviews adapter
        mListView.setAdapter(result_list);
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