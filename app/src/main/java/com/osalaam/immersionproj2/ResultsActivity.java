package com.osalaam.immersionproj2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ResultsActivity extends AppCompatActivity {
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

                    results += " Title: " + ResultList.get(i).getTitle() + "\t" + " Teacher: " + ResultList.get(i).getAuthor() + "\t" + " Class: " + ResultList.get(i).getClassTitle() + "\t" + " URL: " + ResultList.get(i).getURL() +"\n";
                    results += "\n";
                    mResults.add(results);
                }
                if (ResultList.size() == 0)
                {
                    mResults.add("No Results Found. Try Uploading A File Of Type: " + mType + "!");
                }

              //  mTextBookTree.setText(mReslt);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
               // mTextBookTree.setText(databaseError.toString());
            }
        });

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



