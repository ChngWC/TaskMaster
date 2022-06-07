package com.example.taskmaster;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class addNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "addNewTask";
    private TextView setDueDate;
    private Button saveBtn;
    private EditText mTaskEdit;

    private FirebaseFirestore firestore;
    private Context context;
    private String dueDate = "";

    public static addNewTask newInstance() {
        return new addNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate = view.findViewById(R.id.set_due_date);
        mTaskEdit = view.findViewById(R.id.newTaskText);
        saveBtn = view.findViewById(R.id.saveButton);

        firestore = FirebaseFirestore.getInstance();

        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) {
                    saveBtn.setEnabled(false);
                    saveBtn.setBackgroundColor(Color.GRAY);
                } else {
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        setDueDate.setText(dayOfMonth + " / " + month + " / " + year);
                        dueDate = dayOfMonth + " / " + month + " / " + year;

                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String task = mTaskEdit.getText().toString();
                
                if(task.isEmpty()) {
                    Toast.makeText(context, "Please input something", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> taskMap = new HashMap<>();

                    taskMap.put("task", task);
                    taskMap.put("due", dueDate);
                    taskMap.put("status", 0);

                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

                    firestore.collection("Users").document(currentFirebaseUser.getUid()).collection("Tasks").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if ( task.isSuccessful()) {
                                Toast.makeText(context, "Task saved", Toast.LENGTH_SHORT).show();
                            } else {
                               Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
