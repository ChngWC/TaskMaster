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
    private Button backBttn, increaseMaxBttn, selectBttnCollege, selectBttnKnight, buyBttnKnight, selectBttnWizard, buyBttnWizard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_page);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        firestore = FirebaseFirestore.getInstance();

        selectBttnCollege = (Button) findViewById(R.id.select_bttn_college);
        selectBttnCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Boolean> collegeSkinMapSelect = new HashMap<>();
                collegeSkinMapSelect.put("select", true);
                firestore.collection("Users").document(userID).collection("stats").document("CollegeSkin").set(collegeSkinMapSelect);
                firestore.collection("Users").document(userID).collection("stats").document("KnightSkin").update("select", false);
                firestore.collection("Users").document(userID).collection("stats").document("WizardSkin").update("select", false);
            }
        });

        DocumentReference collegeSkinSelected = firestore.collection("Users").document(userID).collection("stats").document("CollegeSkin");
        collegeSkinSelected.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()) {
                    boolean selected = value.getBoolean("select");
                    if(selected) {
                        selectBttnCollege.setEnabled(false);
                        selectBttnCollege.setBackgroundColor(Color.GRAY);
                    } else {
                        selectBttnCollege.setEnabled(true);
                        selectBttnCollege.setBackgroundColor(Color.MAGENTA);
                    }
                } else {
                    Map<String, Boolean> collegeSkinMapSelect = new HashMap<>();
                    collegeSkinMapSelect.put("select", true);
                    firestore.collection("Users").document(userID).collection("stats").document("CollegeSkin").set(collegeSkinMapSelect);
                    selectBttnCollege.setEnabled(false);
                    selectBttnCollege.setBackgroundColor(Color.GRAY);
                }
            }
        });

        //buying knight skin will set bought to be true and select to be false
        buyBttnKnight = (Button) findViewById(R.id.buy_bttn_knight2);
        buyBttnKnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Boolean> knightSkinMap = new HashMap<>();
                knightSkinMap.put("bought", true);
                knightSkinMap.put("select", false);
                firestore.collection("Users").document(userID).collection("stats").document("KnightSkin").set(knightSkinMap);
                firestore.collection("Users").document(userID).collection("Credits").document("creditMap").update("userCredit", FieldValue.increment(-5000));
            }
        });



        // select knight skin will make it true
        selectBttnKnight = (Button) findViewById(R.id.select_bttn_knight);
        selectBttnKnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Users").document(userID).collection("stats").document("KnightSkin").update("select", true);
                firestore.collection("Users").document(userID).collection("stats").document("CollegeSkin").update("select", false);
                firestore.collection("Users").document(userID).collection("stats").document("WizardSkin").update("select", false);
            }
        });


        //when knight skin is selected(true) and bought(true), selectKnight bttn will disabled, if knight skin is not selected but bought(true), selectKnight bttn will be enabled
        DocumentReference knightSkin = firestore.collection("Users").document(userID).collection("stats").document("KnightSkin");
        knightSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    boolean selected = value.getBoolean("select");
                    boolean bought = value.getBoolean("bought");
                    if (selected && bought) {
                        selectBttnKnight.setEnabled(false);
                        selectBttnKnight.setBackgroundColor(Color.GRAY);
                    } else if(!selected && bought) {
                        selectBttnKnight.setEnabled(true);
                        selectBttnKnight.setBackgroundColor(Color.MAGENTA);
                    } else {
                        selectBttnKnight.setEnabled(false);
                        selectBttnKnight.setBackgroundColor(Color.GRAY);
                    }

                    if(bought) {
                        buyBttnKnight.setEnabled(false);
                        buyBttnKnight.setBackgroundColor(Color.GRAY);
                        buyBttnKnight.setText("Bought!");
                    }
                } else {
                    Map<String, Boolean> knightSkinMap = new HashMap<>();
                    knightSkinMap.put("select", false);
                    knightSkinMap.put("bought", false);
                    firestore.collection("Users").document(userID).collection("stats").document("KnightSkin").set(knightSkinMap);
                }
            }
        });


        //buying wizard skin will set bought to be true and select to be false
        buyBttnWizard = (Button) findViewById(R.id.buy_bttn_wizard);
        buyBttnWizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Boolean> wizardSkinMap = new HashMap<>();
                wizardSkinMap.put("bought", true);
                wizardSkinMap.put("select", false);
                firestore.collection("Users").document(userID).collection("stats").document("WizardSkin").set(wizardSkinMap);
                firestore.collection("Users").document(userID).collection("Credits").document("creditMap").update("userCredit", FieldValue.increment(-10000));
            }
        });


        // select wizard skin will make it true
        selectBttnWizard = (Button) findViewById(R.id.select_bttn_wizard);
        selectBttnWizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Users").document(userID).collection("stats").document("WizardSkin").update("select", true);
                firestore.collection("Users").document(userID).collection("stats").document("KnightSkin").update("select", false);
                firestore.collection("Users").document(userID).collection("stats").document("CollegeSkin").update("select", false);
            }
        });

        //when wizard skin is selected(true) and bought(true), selectwizard bttn will disabled, if wizard skin is not selected but bought(true), selectwizard bttn will be enabled
        DocumentReference wizardSkin = firestore.collection("Users").document(userID).collection("stats").document("WizardSkin");
        wizardSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    boolean selected = value.getBoolean("select");
                    boolean bought = value.getBoolean("bought");
                    if (selected && bought) {
                        selectBttnWizard.setEnabled(false);
                        selectBttnWizard.setBackgroundColor(Color.GRAY);
                    } else if(!selected && bought) {
                        selectBttnWizard.setEnabled(true);
                        selectBttnWizard.setBackgroundColor(Color.MAGENTA);
                    } else {
                        selectBttnWizard.setEnabled(false);
                        selectBttnWizard.setBackgroundColor(Color.GRAY);
                    }

                    if(bought) {
                        buyBttnWizard.setEnabled(false);
                        buyBttnWizard.setBackgroundColor(Color.GRAY);
                        buyBttnWizard.setText("Bought!");
                    }
                } else {
                    Map<String, Boolean> wizardSkinMap = new HashMap<>();
                    wizardSkinMap.put("select", false);
                    wizardSkinMap.put("bought", false);
                    firestore.collection("Users").document(userID).collection("stats").document("WizardSkin").set(wizardSkinMap);
                }
            }
        });


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

                //final boolean[] knightBought = new boolean[1];
               // final boolean[] wizardBought = new boolean[1];

               DocumentReference wizardRef = firestore.collection("Users").document(userID).collection("stats").document("WizardSkin");
                wizardRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value.exists()) {
                            boolean wizardBought = value.getBoolean("bought");
                            //for buying wizard
                            if (currentCredit < 10000) {
                                buyBttnWizard.setEnabled(false);
                                buyBttnWizard.setBackgroundColor(Color.GRAY);
                            } else if (wizardBought == true) {
                                buyBttnWizard.setEnabled(false);
                                buyBttnWizard.setBackgroundColor(Color.GRAY);
                            } else {
                                buyBttnWizard.setEnabled(true);
                            }
                        } else {
                            Map<String, Boolean> wizardSkinMap = new HashMap<>();
                            wizardSkinMap.put("select", false);
                            wizardSkinMap.put("bought", false);
                            firestore.collection("Users").document(userID).collection("stats").document("WizardSkin").set(wizardSkinMap);
                        }
                    }
                });

                DocumentReference knightRef = firestore.collection("Users").document(userID).collection("stats").document("KnightSkin");
                knightRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value.exists()) {
                            boolean knightBought = value.getBoolean("bought");
                            //for buying knight skin
                            if (currentCredit < 5000) {
                                buyBttnKnight.setEnabled(false);
                                buyBttnKnight.setBackgroundColor(Color.GRAY);
                            } else if (knightBought == true) {
                                buyBttnKnight.setEnabled(false);
                                buyBttnKnight.setBackgroundColor(Color.GRAY);
                            } else {
                                buyBttnKnight.setEnabled(true);
                            }
                        } else {
                            Map<String, Boolean> knightSkinMap = new HashMap<>();
                            knightSkinMap.put("select", false);
                            knightSkinMap.put("bought", false);
                            firestore.collection("Users").document(userID).collection("stats").document("KnightSkin").set(knightSkinMap);
                        }
                    }
                });

            }
        });

    }
}