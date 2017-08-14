package com.osalaam.immersionproj2;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadableActivity extends AppCompatActivity implements View.OnClickListener
{


    private static final int PICK_IMAGE_REQUEST = 234;
    private ImageView mimageView;
    private Button mbuttonChoose, mbuttonUpload;

    // Button mUpload;
    // String className = "";
    String fileTitle = "";
    String teacherName = "";
    String fileComment = "";
    String fileType = "";

    private EditText mGetFileTitle = (EditText) findViewById(R.id.getfiletitle);

    private EditText mGetTeacher = (EditText) findViewById(R.id.getteacher);

    private EditText mGetComment = (EditText) findViewById(R.id.getcomment);

    private String mType;

    private String mClass; // will be intent from resources activity

    /* private String mAuthor; // will be intent from resources activity
     private String mTitle; // will be intent from resources activity
     private String mComment; // will be intent from resources activity
 */
    private Uri filePath;//the file path to the new storage object

    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues() {
        Button uploadButton = (Button) findViewById(R.id.buttonUpload);

        String s1 = mGetComment.getText().toString();
        String s2 = mGetFileTitle.getText().toString();
        String s3 = mGetTeacher.getText().toString();


        if (s1.equals("") || s2.equals("") || s3.equals("")) {
            uploadButton.setEnabled(false);
        } else {
            uploadButton.setEnabled(true);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mClass = getIntent().getStringExtra("class_title");//gets intents
     /*   mComment = getIntent().getStringExtra("file_comment");
        mAuthor = getIntent().getStringExtra("teacher_name");
        mTitle = getIntent().getStringExtra("file_title");
        mType = getIntent().getStringExtra("file_type");*/


        fileTitle = mGetFileTitle.getText().toString(); //gets content of edit texts
        teacherName = mGetTeacher.getText().toString();
        fileComment = mGetComment.getText().toString();


//get the spinner from the xml.
        final Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"Homework", "Text Book", "Exams"};
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadable);


        //references to XML
        mimageView = (ImageView) findViewById(R.id.imageView);
        mbuttonChoose = (Button) findViewById(R.id.buttonChoose);
        mbuttonUpload = (Button) findViewById(R.id.buttonUpload);

        //button click listneres
        mbuttonChoose.setOnClickListener(this);
        mbuttonUpload.setOnClickListener(this);

        // set listeners
        mGetTeacher.addTextChangedListener(mTextWatcher);
        mGetFileTitle.addTextChangedListener(mTextWatcher);
        mGetComment.addTextChangedListener(mTextWatcher);
        // run once to disable if empty
        checkFieldsForEmptyValues();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
    }


    private void uploadFile() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference relevantRef = mStorageReference.child(mType + "/" + filePath.getLastPathSegment().toString());

            final DatabaseReference uploadedRef = mDatabaseReference.child(mType);


            //pass in metadata alongside filepath
            relevantRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            /*assigning it to the new file*/
                            //  mDatabaseReference.push();

                            ResourceObj newHW = new ResourceObj(teacherName, mClass, fileTitle, downloadUrl.toString(), fileComment);
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mimageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {

        if (view == mbuttonChoose) {
            // This opens the file chooser
            showFileChooser();

        } else if (view == mbuttonUpload) {

            // This uploads the file
            uploadFile();
        }
    }
}


