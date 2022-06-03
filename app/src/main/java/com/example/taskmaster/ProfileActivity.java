package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button signout, taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        taskList = (Button) findViewById(R.id.taskList_button);

        taskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, TaskList.class));
            }
        });



        signout = (Button) findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Successfully signed out!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://taskmaster-15439-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        final TextView emailTextView = (TextView) findViewById(R.id.emailText);
        final TextView nameTextView = (TextView)  findViewById(R.id.nameText);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                
                if(userProfile != null) {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;

                    /*if(fullName.length() <= 15 && email.length() <= 15) {
                        nameTextView.setText(fullName);
                        emailTextView.setText(email);
                    }*/

                    if (fullName.length() > 15) {
                        fullName = fullName.substring(0, Math.min(fullName.length(), 15)) + "...";
                        //nameTextView.setText(fullName + "...");
                    }

                    if (email.length() > 15) {
                        email = email.substring(0, Math.min(email.length(), 15)) + "...";
                    //emailTextView.setText(email + "...");
                    }
                    nameTextView.setText(fullName);
                    emailTextView.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Unexpected error!!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}