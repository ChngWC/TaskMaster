package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView signUp;
    private TextInputEditText mainTextEmail, mainTextPassword;
    private Button logIn, forgor;

    private FirebaseAuth mAuth;
    private ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = (TextView) findViewById(R.id.button6);
        signUp.setOnClickListener(this);

        logIn = (Button) findViewById(R.id.logInButton);
        logIn.setOnClickListener(this);

        forgor = (Button) findViewById(R.id.button7);
        forgor.setOnClickListener(this);

        mainTextEmail = (TextInputEditText)  findViewById(R.id.mainEmail);
        mainTextPassword = (TextInputEditText)  findViewById(R.id.mainPassword);

        mainProgressBar = (ProgressBar) findViewById(R.id.mainProgress);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button6:
                startActivity(new Intent(this, Register_Page.class));
                break;

            case R.id.logInButton:
                userLogin();
                break;

            case R.id.button7:
                startActivity(new Intent(this,reset.class));
                break;

        }
    }

    private void userLogin() {
        String email = mainTextEmail.getText().toString().trim();
        String password = mainTextPassword.getText().toString().trim();

        if(email.isEmpty()) {
            mainTextEmail.setError("Email is required!");
            mainTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mainTextEmail.setError("Please provide valid email!");
            mainTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mainTextPassword.setError("Password is required!");
            mainTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mainTextPassword.setError("Min password is 6 characters!");
            mainTextPassword.requestFocus();
            return;
        }

        mainProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        // redirect to user profile;
                    }else{
                        Toast.makeText(MainActivity.this,"Please Verify Email!",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please check again!", Toast.LENGTH_LONG).show();
            }
        }


    });
    }
}