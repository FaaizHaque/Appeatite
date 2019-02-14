package com.example.customerclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    EditText editTextUsername, editTextPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        editTextUsername =  findViewById(R.id.actSignup_textfield_username);
        editTextPassword = (EditText) findViewById(R.id.actSignup_textfield_password);

        findViewById(R.id.actSignup_button_signup).setOnClickListener(this);


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

         mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     Toast.makeText(getApplicationContext(),"user registeration successful.", Toast.LENGTH_LONG).show();
                 }
                 else{
                     Toast.makeText(getApplicationContext(),"some error occured.", Toast.LENGTH_LONG);
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
