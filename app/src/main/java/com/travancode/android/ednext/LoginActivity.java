package com.travancode.android.ednext;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    Button mLogin;
    ProgressBar mProgressBar;

    private EditText mUserName;
    private EditText mPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = findViewById(R.id.mLogin);
        mProgressBar = findViewById(R.id.progressbar);
        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        mProgressBar.setVisibility(View.INVISIBLE);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mUserName.getText().toString().trim(), mPassword.getText().toString().trim());
            }
        });
    }

    private void signIn(String email, String password)
    {
        Log.d(TAG, "signIn:" + email);

        if(!validateForm()){
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {

                           Log.d(TAG, "signInWithEmail : Success");
                           FirebaseUser user = mAuth.getCurrentUser();
                           mProgressBar.setVisibility(View.INVISIBLE);
                           Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                       } else {

                           Log.d(TAG, "signInWithEmail : Failed");
                           mProgressBar.setVisibility(View.INVISIBLE);
                           Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }

    private boolean validateForm() {

        boolean valid = true;

        String email = mUserName.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {

            mUserName.setError("Required");
            valid = false;

        } else {

            mUserName.setError(null);

        }

        String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(password)) {

            mPassword.setError("Required");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }
}
