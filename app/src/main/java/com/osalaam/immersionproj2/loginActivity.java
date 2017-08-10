package com.osalaam.immersionproj2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mloginButton;
    private Button mSignupButton;
    private EditText memail;
    private EditText mpassword;

    private ProgressDialog mProgressdialog;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseauth = FirebaseAuth.getInstance();

//        if (firebaseauth.getCurrentUser() != null) {
//            finish();
//            startActivity(new Intent(getApplicationContext(), SubjectsActivity.class));
//
//        }

        mloginButton = (Button) findViewById(R.id.loginbutton1);
//        mSignupButton = (Button) findViewById(R.id.msignupbutton);
        memail = (EditText) findViewById(R.id.memailid);
        mpassword = (EditText) findViewById(R.id.mpasswd);

        mProgressdialog = new ProgressDialog(this);
        mloginButton.setOnClickListener(this);
    }

    private void loginpage() {
        String email = memail.getText().toString().trim();
        String password = mpassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please, Enter your email", Toast.LENGTH_SHORT).show();

            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please,enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        //if it is not empty,progressdialog comes once they signup
        mProgressdialog.setMessage("Logging In");
        mProgressdialog.show();

        firebaseauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressdialog.dismiss();
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), SubjectsActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Login Failed,Please Check Your Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view == mloginButton) {
            loginpage();
        }
        if (view == mSignupButton) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}






