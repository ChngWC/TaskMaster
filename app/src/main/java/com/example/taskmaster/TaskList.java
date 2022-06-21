package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.taskmaster.Adapter.ToDoAdapter;
import com.example.taskmaster.model.ToDoModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList extends AppCompatActivity implements OnDialogCloseListener {

    private Button back, addbutton, helpButton;
    private RecyclerView taskRecycler;
    private ToDoAdapter taskAdapter;
    private FirebaseFirestore firestore;
    private List<ToDoModel> taskList;
    private Query query;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        helpButton = (Button) findViewById(R.id.help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTaskHelpWindow();
            }
        });

        back = (Button) findViewById(R.id.back_Button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskList.this, ProfileActivity.class));
            }
        });

        //getSupportActionBar().hide(); // this may be used for future vids

        taskList = new ArrayList<>();

        addbutton = (Button) findViewById(R.id.addTask_Button);
        firestore = FirebaseFirestore.getInstance();
        taskRecycler = (RecyclerView) findViewById(R.id.taskRecycler);

        taskRecycler.setHasFixedSize(true);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new ToDoAdapter(TaskList.this, taskList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecycler);
        taskRecycler.setAdapter(taskAdapter);

        taskRecycler.setAdapter(taskAdapter);
        showData();


        //for testing purpose*****
        /*ToDoModel task = new ToDoModel();
        task.setTask("This is a Test Task");
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        taskAdapter.setTasks(taskList);*/
        //for testing purpose*****

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask.newInstance().show(getSupportFragmentManager(), addNewTask.TAG);

            }
        });


    }

    private void openTaskHelpWindow() {
        startActivity(new Intent(TaskList.this, taskHelpWindow.class));
    }

    private void showData() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        //Object id = currentFirebaseUser.getUid();
        //Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
        // Testing the retrieval of userID
        query = firestore.collection("Users").document(currentFirebaseUser.getUid()).collection("Tasks").orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if( documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);

                        taskList.add(toDoModel);
                        taskAdapter.notifyDataSetChanged();

                    }

                }
                listenerRegistration.remove();
            }
        });
    }
    @Override
    public void onDialogClose(DialogInterface dialogInterface){
        taskList.clear();
        showData();
        taskAdapter.notifyDataSetChanged();
    }
}