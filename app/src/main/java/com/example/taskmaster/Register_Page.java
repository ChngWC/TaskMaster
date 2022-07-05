package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Register_Page extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText editTextFullName, editTextEmail, editTextPassword;
     private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        TextView registerUser = (Button) findViewById(R.id.registerButton);
        registerUser.setOnClickListener(this);

        editTextFullName = (TextInputEditText) findViewById(R.id.nameRegister);
        editTextEmail = (TextInputEditText)  findViewById(R.id.emailRegister);
        editTextPassword = (TextInputEditText)  findViewById(R.id.passwordRegister);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    public void onClick(View v) {
        boolean successful = false;

        switch (v.getId()) {
            case R.id.registerButton:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();

        //if all fields are not filled
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() & (password.isEmpty() || password.length() < 6) &
                fullName.isEmpty()) {
            editTextEmail.setError("Please provide valid Email");

            if (password.isEmpty()) {
                editTextPassword.setError("Password is Required");
            }
            else if (password.length() < 6) {
                editTextPassword.setError("Min password should be 6 characters!");
            }

            editTextFullName.setError("Full Name is Required");
            return;
        }



        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Min password should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if (fullName.isEmpty()) {
            editTextFullName.setError("Full Name is Required");
            editTextFullName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(fullName, email);



                            FirebaseDatabase.getInstance("https://taskmaster-15439-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(Register_Page.this, "Please check email for verification (spam)!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                                user.sendEmailVerification();
                                                firestore = FirebaseFirestore.getInstance();
                                                DocumentReference documentReference = firestore.collection("Users").document(user.getUid());
                                                Map<String, Object> userMap = new HashMap<>();
                                                userMap.put("Name",fullName);
                                                userMap.put("Email", email);
                                                documentReference.set(userMap);
                                                Map<String, Integer> gameScoreMap = new HashMap<>();
                                                gameScoreMap.put("Highest", 0);
                                                firestore.collection("Users").document(user.getUid()).collection("HighestScore").document("HighScoreMap").set(gameScoreMap);
                                                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                                                startActivity(new Intent(Register_Page.this, MainActivity.class));
                                                //redirect to login page
                                            } else {
                                                Toast.makeText(Register_Page.this, "User fail to register!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Register_Page.this, "User fail to register!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }

}