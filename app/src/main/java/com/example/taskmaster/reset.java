package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset extends AppCompatActivity {

    private EditText forgotemail;
    private Button resetpassword;
    private ProgressBar progressBar3;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        forgotemail=(EditText) findViewById(R.id.forgotemail);
        resetpassword=(Button) findViewById(R.id.resetpassword);
        progressBar3= (ProgressBar) findViewById(R.id.progressBar3);

        auth= FirebaseAuth.getInstance();

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpass();
            }
        });
    }

    private void resetpass(){
        String email = forgotemail.getText().toString().trim();

        if (email.isEmpty()){
            forgotemail.setError("Please input email");
            forgotemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            forgotemail.setError("Provide valid email");
            forgotemail.requestFocus();
            return;
        }

        progressBar3.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(reset.this,"Check your email", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(reset.this, MainActivity.class));

                }else{
                    Toast.makeText(reset.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}