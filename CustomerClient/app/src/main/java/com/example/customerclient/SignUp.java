package com.example.customerclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        editTextUsername =  findViewById(R.id.actSignup_textfield_username);
        editTextPassword = findViewById(R.id.actSignup_textfield_password);

        findViewById(R.id.actSignup_button_signup).setOnClickListener(this);
        findViewById(R.id.actSignup_button_back).setOnClickListener(this);

        progressBar = findViewById(R.id.progress_bar);

    }

    private void registerUser(){
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(username.isEmpty()){
            editTextUsername.setError("username is required.");
            editTextUsername.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            editTextUsername.setError("please enter a valid email.");
            editTextUsername.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("password is required.");
            editTextPassword.requestFocus();
            return;
        }

         if(password.length() < 6){
            editTextPassword.setError("Password min length = 6");
            editTextPassword.requestFocus();
            return;
         }

         progressBar.setVisibility(View.VISIBLE);

         mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 progressBar.setVisibility(View.GONE);
                 if(task.isSuccessful()){
                     Toast.makeText(getApplicationContext(),"user registeration successful.", Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(SignUp.this, MainActivity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(intent);
                 }
                 else{
                     if(task.getException() instanceof FirebaseAuthUserCollisionException){
                         Toast.makeText(getApplicationContext(), "Error: email already registered", Toast.LENGTH_LONG).show();
                     }
                     else{
                         Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                     }

                 }
             }
         });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.actSignup_button_signup:
                registerUser();
                break;

            case R.id.actSignup_button_back:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
