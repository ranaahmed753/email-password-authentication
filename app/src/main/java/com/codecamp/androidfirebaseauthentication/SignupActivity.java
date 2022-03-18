package com.codecamp.androidfirebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private RelativeLayout mRelativeLayout;
    private ConstraintLayout mConstraintLayout;
    private EditText mEmailEditText, mPasswordEditText;
    private RelativeLayout mSignupButton;
    private TextView mLoginText;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        mRelativeLayout = findViewById(R.id.relativeLayoutID);
        mConstraintLayout = findViewById(R.id.constraintLayoutID);

        mEmailEditText = findViewById(R.id.emailID);
        mPasswordEditText = findViewById(R.id.passwordID);

        mSignupButton = findViewById(R.id.signupButtonID);

        mLoginText = findViewById(R.id.loginTextID);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSignupButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                signupProcess(email,password);
            }
        });

        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLoginText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void signupProcess(String email, String password)
    {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            mEmailEditText.setError("Email is empty");
            mPasswordEditText.setError("Password is empty");
            mEmailEditText.requestFocus();
            mPasswordEditText.requestFocus();

        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            mEmailEditText.setError("wrong email pattern");
            mEmailEditText.requestFocus();

        }else if(password.length()<6){

            mEmailEditText.setError("password must be 7 or above ");
            mEmailEditText.requestFocus();

        }else{

            createUser(email,password);
        }
    }

    private void createUser(String email, String password)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating User");
        progressDialog.setMessage("Please Wait.....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mCurrentUser = mAuth.getCurrentUser();
                    mCurrentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Error:-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Error:-"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
    }
}