package com.osalaam.immersionproj2;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploadableActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 234;
    private ImageView mimageView;
    private Button mbuttonChoose, mbuttonUpload;
    private EditText mGetComment, mGetTeacher, mGetTitle;

    private String mType; // all intents from resource activity
    private String mClass; // will be intent from resources activity

    /*
        private String mAuthor; // will be intent from resources activity
        private String mTitle; // will be intent from resources activity
        private String mComment; // will be intent from resources activity
    */
    private Uri filePath;//the file path to the new storage object
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();


    private File mPhotoFile;
    private ImageView mPhotoView;
    private static final int REQUEST_PHOTO = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mClass = getIntent().getStringExtra("class_title");//gets intents
      /* mComment = getIntent().getStringExtra("file_comment");
        mAuthor = getIntent().getStringExtra("teacher_name");
        mTitle = getIntent().getStringExtra("file_title");
        mType = getIntent().getStringExtra("file_type");
        */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadable);

        //references to XML
        mimageView = (ImageView) findViewById(R.id.iview);
        mbuttonChoose = (Button) findViewById(R.id.buttonChoose);
        mbuttonUpload = (Button) findViewById(R.id.buttonUpload);

        mGetTeacher = (EditText) findViewById(R.id.getteacher);
        mGetComment = (EditText) findViewById(R.id.getcomment);
        mGetTitle = (EditText) findViewById(R.id.getfiletitle);

        //button click listneres
        mbuttonChoose.setOnClickListener(this);
        mbuttonUpload.setOnClickListener(this);

//get the spinner from the xml.
        final Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"Homework", "Text Books", "Exams"};
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                mType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        mPhotoView = (ImageView)findViewById(R.id.iview);

        final Button photoButton = (Button) findViewById(R.id.take_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File filesDir = getFilesDir();
                mPhotoFile = new File(filesDir, "IMG_foo.jpg");

                Uri uri = FileProvider.getUriForFile(UploadableActivity.this, "com.osalaam.immersionproj2.fileprovider", mPhotoFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // skipped checking with packagemanager and resolver
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_IMAGE_REQUEST);
    }

    private void uploadFile(){

        if (filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference relevantRef = mStorageReference.child(mType +  "/" + filePath.getLastPathSegment().toString());

            final DatabaseReference uploadedRef =  mDatabaseReference.child(mType);

            //pass in metadata alongside filepath
            relevantRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            /*assigning it to the new file*/
                            //  mDatabaseReference.push();

                            ResourceObj newHW = new ResourceObj(mGetTeacher.getText().toString(), mClass, mGetTitle.getText().toString(), downloadUrl.toString(), mGetComment.getText().toString());
                            DatabaseReference newFileRef = uploadedRef.push();//autogenerated unique key
                            newFileRef.setValue(newHW.toMap());//sets its as value of generated key

                            progressDialog.dismiss();


                            // Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int) progress) + "% Uploaded...");
                        }
                    })
            ;
        } else {
            // display an error Toast
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try{



                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
               mimageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }



        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            Log.i("OA", mPhotoFile.toString());
            Uri uri = FileProvider.getUriForFile(this, "com.osalaam.immersionproj2.fileprovider", mPhotoFile);
            filePath = uri;
            revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    @Override
    public void onClick(View view) {

        if (view == mbuttonChoose){
            // This opens the file chooser
            showFileChooser();

        } else if (view == mbuttonUpload){
            // This uploads the file
            uploadFile();
        }
    }





    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            // old way - uses up a lot of RAM!
            /*Bitmap bm = BitmapFactory.decodeFile(mPhotoFile.getPath());
            Log.i("DEI", bm.getByteCount()+" "+bm.getAllocationByteCount());
            mPhotoView.setImageBitmap(bm);
*/

            // New way - get size of top-level view, use that as max height/width. Can also send any other values for desired height/width.
            View rootView = findViewById(R.id.activity_main);
            Log.i("OA", "display:"+rootView.getWidth()+" "+rootView.getHeight());

            Bitmap bm = TakePicture.getScaledBitmap(mPhotoFile.getPath(), rootView.getWidth(), rootView.getHeight() );
            Log.i("OA", bm.getByteCount()+" "+bm.getAllocationByteCount()+" "+bm.getWidth()+" "+bm.getHeight());
            mPhotoView.setImageBitmap(bm);


        }
    }
}