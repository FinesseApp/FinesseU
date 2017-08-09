package com.osalaam.immersionproj2;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class UploadableActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int PICK_IMAGE_REQUEST = 234;
    private ImageView mimageView;
    private Button mbuttonChoose, mbuttonUpload;

    private String mType = "homework";
    private String mClass = "computer organization";
    private String mTeacher = "pierre";
    private String mRating = "5";

    private Uri filePath;
    // = Uri.fromFile(new File("path/to/doc/rivers.pdf"));

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadable);

        mStorageReference = FirebaseStorage.getInstance().getReference();

        DatabaseReference textBookRef = databaseReference.child("Text Book");
        DatabaseReference homeworkRef =  databaseReference.child("Homework");



        mimageView = (ImageView) findViewById(R.id.imageView);
        mbuttonChoose = (Button) findViewById(R.id.buttonChoose);
        mbuttonUpload = (Button) findViewById(R.id.buttonUpload);

        mbuttonChoose.setOnClickListener(this);
        mbuttonUpload.setOnClickListener(this);

    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
    }


    private void uploadFile(){

        if (filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference relevantRef = mStorageReference.child(mType +  "/" + filePath.getLastPathSegment().toString());
            //StorageReference relevantRef = mStorageReference.child("doc/" + filePath.getLastPathSegment());
            mDatabaseReference.child(mType +  "/" + filePath.getLastPathSegment().toString());


                            /*creating metadata*/
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("application/*")
                    .setCustomMetadata("teacher", mTeacher).setCustomMetadata("rating", mRating).setCustomMetadata("class", mClass).setCustomMetadata("type", mType )
                    .build();


           /* relevantRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    // Metadata now contains the metadata for 'images/forest.jpg'
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });*/
/*
            relevantRef.updateMetadata(metadata)
                    .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            // Updated metadata is in storageMetadata
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });
*/
            //pass in metadata alongside filepath
            relevantRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            // Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            /*assigning it to the new file*/
                          //  mDatabaseReference.push();
                            progressDialog.dismiss();


                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
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
}



/*
Notes:

Not chanigng metadata

WE MUST ENCODE METADATA ON THE THING. Doneish - make unique from upload screen.......

On search, get a list of all the metadata list and parse



investigate using file metadata

We autogenerate a firebase download link for webbrowser, that vs displaying in app or in  auxillary phone applicaiton
 */



