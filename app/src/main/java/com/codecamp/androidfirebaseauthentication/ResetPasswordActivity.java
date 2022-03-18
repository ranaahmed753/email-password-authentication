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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    private ConstraintLayout mConstraintLayout;
    private EditText mEmailEditText;
    private RelativeLayout mResetPasswordButton;
    private ImageView mBackButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mConstraintLayout = findViewById(R.id.constraintLayoutID);
        mEmailEditText = findViewById(R.id.emailID);
        mResetPasswordButton = findViewById(R.id.resetPasswordButtonID);
        mBackButton = findViewById(R.id.backButtonID);

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResetPasswordButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                String email = mEmailEditText.getText().toString();
                resetPassword(email);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBackButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void resetPassword(String email)
    {
        if(TextUtils.isEmpty(email))
        {
            mEmailEditText.setError("Email is empty");
            mEmailEditText.requestFocus();

        }else if(!mCurrentUser.getEmail().equals(email)){

            mEmailEditText.setError("use your own email");
            mEmailEditText.requestFocus();

        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            mEmailEditText.setError("wrong email pattern");
            mEmailEditText.requestFocus();

        }else{

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Logging User");
            progressDialog.setMessage("Please Wait.....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Check your email to reset your password",Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
//                        finish();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
    }
}