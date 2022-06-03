package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.taskmaster.Adapter.ToDoAdapter;
import com.example.taskmaster.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class TaskList extends AppCompatActivity {

    private Button back;
    private RecyclerView taskRecycler;
    private ToDoAdapter taskAdapter;

    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        back = (Button) findViewById(R.id.back_Button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskList.this, ProfileActivity.class));
            }
        });

        //getSupportActionBar().hide(); // this may be used for future vids

        taskList = new ArrayList<>();

        taskRecycler = (RecyclerView) findViewById(R.id.taskRecycler);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new ToDoAdapter(this);
        taskRecycler.setAdapter(taskAdapter);

        ToDoModel task = new ToDoModel();
        task.setTask("This is a Test Task");
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        taskAdapter.setTasks(taskList);
    }
}