package com.osalaam.immersionproj2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mloginButton;
    private Button mSignupButton;
    private EditText memail;
    private EditText mpassword;

    private ProgressDialog mProgressdialog;
    private FirebaseAuth firebaseauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressdialog = new ProgressDialog(this);
        firebaseauth = FirebaseAuth.getInstance();


        mloginButton = (Button) findViewById(R.id.loginbutton);
        mSignupButton = (Button) findViewById(R.id.signupbutton);
        memail = (EditText) findViewById(R.id.editText2);
        mpassword = (EditText) findViewById(R.id.editText3);


        mloginButton.setOnClickListener(this);
        mSignupButton.setOnClickListener(this);
    }



//        mClassesButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(MainActivity.this, ClassesActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
    private void signupUser() {
        String email = memail.getText().toString().trim();
        String password =mpassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please, Enter your email",Toast.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please,enter your password",Toast.LENGTH_SHORT).show();
            return;
        }

        //if it is not empty,progressdialog comes once they signup

        mProgressdialog.setMessage("Signing Up");
        mProgressdialog.show();

        firebaseauth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Registration complete",Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(MainActivity.this,"Registration Failed,Please try again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    @Override
    public void onClick(View view) {
        if(view == mSignupButton){
            signupUser();

        }
        if(view == mloginButton){
            //login activity

        }
    }


}