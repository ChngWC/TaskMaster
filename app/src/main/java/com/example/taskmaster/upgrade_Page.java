package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class upgrade_Page extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private String userID;
    private Button backBttn, increaseMaxBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_page);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        firestore = FirebaseFirestore.getInstance();

        backBttn = (Button) findViewById(R.id.upgradBack_Button);
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(upgrade_Page.this, ProfileActivity.class));
            }
        });

        increaseMaxBttn = (Button) findViewById(R.id.increaseMax_bttn);
        increaseMaxBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Users").document(userID).collection("stats").document("statsMap").update("userMaxHP", FieldValue.increment(1));
                firestore.collection("Users").document(userID).collection("Credits").document("creditMap").update("userCredit", FieldValue.increment(-500));
            }
        });

        TextView credit = (TextView) findViewById(R.id.currentCredit2);
        DocumentReference creditDoc = firestore.collection("Users").document(userID).collection("Credits").document("creditMap");
        creditDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                int currentCredit = (value.getLong("userCredit").intValue());
                String creditDisplay = currentCredit + " credits";
                credit.setText(creditDisplay);
            }
        });

        TextView userMaxHP = (TextView) findViewById(R.id.currentHP);
        DocumentReference HPDoc = firestore.collection("Users").document(userID).collection("stats").document("statsMap");
        HPDoc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    int currentHP = (value.getLong("userMaxHP").intValue());
                    String HPDisplay = currentHP + " HP";
                    userMaxHP.setText(HPDisplay);
                } else {
                    Map<String, Integer> HPMap = new HashMap<>();
                    HPMap.put("userMaxHP", 10);
                    firestore.collection("Users").document(userID).collection("stats").document("statsMap").set(HPMap);
                    String HPDisplay = 10 + "HP";
                    userMaxHP.setText(HPDisplay);
                }
            }
        });

        creditDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                int currentCredit = (value.getLong("userCredit").intValue());
                if (currentCredit < 500) {
                    increaseMaxBttn.setEnabled(false);
                    increaseMaxBttn.setBackgroundColor(Color.GRAY);
                } else {
                    increaseMaxBttn.setEnabled(true);
                }
            }
        });

    }
}