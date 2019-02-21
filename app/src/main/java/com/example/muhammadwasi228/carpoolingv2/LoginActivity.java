package com.example.muhammadwasi228.carpoolingv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String USER_EMAIL="user_email";
    public static final String USER_ID="user_id";
    public static final String USER_DISPLAY_NAME="user_display_name";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText=findViewById(R.id.input_email);
        passwordText=findViewById(R.id.input_password);
        loginButton=findViewById(R.id.btn_login);
        signupLink=findViewById(R.id.link_signup);
        mAuth=FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "onAuthStateChanged: called");
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    onLoginSuccess();

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: loginButton clicked");
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Log.d(TAG, "onClick: signUp link called");
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        Log.d(TAG, "onLoginFailed: login failed");
        loginButton.setEnabled(true);
    }
    public boolean validate() {
        Log.d(TAG, "validate: validation user input values");
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess: login is successful,user is directed to main activity");
        loginButton.setEnabled(true);

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra(USER_EMAIL,mAuth.getCurrentUser().getEmail());
        intent.putExtra(USER_DISPLAY_NAME,mAuth.getCurrentUser().getDisplayName());
        intent.putExtra(USER_ID,mAuth.getCurrentUser().getUid());
        this.finish();
        startActivity(intent);

    }
    public void login() {

        if (!validate()) {
            onLoginFailed("Invalid input");
            return;
        }

        Log.d(TAG, "login: logging in");

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        progressDialog.setIndeterminate(true);
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    onLoginSuccess();
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    onLoginFailed("Invalid Passwword");
                }else if (e instanceof FirebaseAuthInvalidUserException){
                    onLoginFailed("No Account with this email");

                }else{
                    onLoginFailed("Login failed");

                }
            }
        });

    }
}
