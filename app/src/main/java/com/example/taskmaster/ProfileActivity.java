package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmaster.GameStuff.MainGameActivity;
import com.example.taskmaster.TaskStuff.TaskList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    public static int health;
    public static int skintype;

    private Button signout, taskList, toBattle, upgradeBttn;

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

        toBattle = (Button) findViewById(R.id.battle_Button);

        toBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainGameActivity.class));
            }
        });

        upgradeBttn = (Button) findViewById(R.id.upgrade_Button);
        upgradeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, upgrade_Page.class));
            }
        });



        signout = (Button) findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Successfully signed out!", Toast.LENGTH_LONG).show();
                finishAffinity();
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
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        TextView highScore = (TextView) findViewById(R.id.HighScore);
        DocumentReference documentReference = fstore.collection("Users").document(userID).collection("HighestScore").document("HighScoreMap");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                int HighestScore = (documentSnapshot.getLong("Highest").intValue());
                String highScoreText = HighestScore + " points";
                highScore.setText(highScoreText);
            }
        });

        TextView credit = (TextView) findViewById(R.id.currentCredit);
        DocumentReference creditDoc = fstore.collection("Users").document(userID).collection("Credits").document("creditMap");
        creditDoc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    int currentCredit = (value.getLong("userCredit").intValue());
                    String creditDisplay = currentCredit + " credits";
                    credit.setText(creditDisplay);
                } else {
                    Map<String, Integer> creditMap = new HashMap<>();
                    creditMap.put("userCredit", 0);
                    fstore.collection("Users").document(userID).collection("Credits").document("creditMap").set(creditMap);
                    String creditDisplay = 0 + "credits";
                    credit.setText(creditDisplay);
                }
            }
        });

        DocumentReference HPDoc = fstore.collection("Users").document(userID).collection("stats").document("statsMap");
        HPDoc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    health = (value.getLong("userMaxHP").intValue());
                } else {
                    Map<String, Integer> HPMap = new HashMap<>();
                    HPMap.put("userMaxHP", 10);
                    fstore.collection("Users").document(userID).collection("stats").document("statsMap").set(HPMap);
                }
            }
        });

        DocumentReference collegeSkin = fstore.collection("Users").document(userID).collection("stats").document("CollegeSkin");
        DocumentReference knightSkin = fstore.collection("Users").document(userID).collection("stats").document("KnightSkin");
        DocumentReference wizardSkin = fstore.collection("Users").document(userID).collection("stats").document("WizardSkin");

        collegeSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    boolean selected = value.getBoolean("select");
                    if (selected) {
                        skintype = 0;
                        Log.d("SpriteSheet.java", "College skin");
                    }
                }
            }
        });

        knightSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    boolean selected = value.getBoolean("select");
                    if (selected) {
                        skintype = 1;
                        Log.d("SpriteSheet.java", "knight skin");
                    }
                }
            }
        });
        wizardSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    boolean selected = value.getBoolean("select");
                    if (selected) {
                        skintype = 2;
                        Log.d("SpriteSheet.java", "wizard skin");
                    }
                }
            }
        });

    }
}