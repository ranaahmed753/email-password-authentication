package com.codecamp.androidfirebaseauthentication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.codecamp.androidfirebaseauthentication.R.color.orange_color;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout mRelativeLayout, mLoginButton;
    private ConstraintLayout mConstraintLayout;
    private EditText mEmailEditText, mPasswordEditText;
    private TextView mSignupText, mForgotPasswordText;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mRelativeLayout = findViewById(R.id.relativeLayoutID);
        mConstraintLayout = findViewById(R.id.constraintLayoutID);

        mEmailEditText = findViewById(R.id.emailID);
        mPasswordEditText = findViewById(R.id.passwordID);

        mLoginButton = findViewById(R.id.loginButtonID);

        mSignupText = findViewById(R.id.signupTextID);
        mForgotPasswordText = findViewById(R.id.forgotPasswordTextID);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLoginButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                loginProcess(email,password);
            }
        });

        mSignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSignupText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
            }
        });

        mForgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mForgotPasswordText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                finish();
            }
        });

    }


    private void loginProcess(String email, String password)
    {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            mEmailEditText.setError("Email is empty");
            mPasswordEditText.setError("Password is empty");
            mEmailEditText.requestFocus();
            mPasswordEditText.requestFocus();

        }else if(!mCurrentUser.getEmail().equals(email)){

            mEmailEditText.setError("use your own email");
            mEmailEditText.requestFocus();


        }else{
            login(email,password);
        }
    }


    private void login(String email, String password)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging User");
        progressDialog.setMessage("Please Wait.....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    if(mCurrentUser.isEmailVerified())
                    {
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Please verify your email",Toast.LENGTH_SHORT).show();
                    }
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