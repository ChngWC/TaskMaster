package com.example.taskmaster.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmaster.R;
import com.example.taskmaster.TaskStuff.TaskList;
import com.example.taskmaster.TaskStuff.addNewTask;
import com.example.taskmaster.model.ToDoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter <ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private TaskList activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(TaskList activity, List<ToDoModel> todoList){
        this.todoList = todoList;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        firestore = FirebaseFirestore.getInstance();
        return new ViewHolder(itemView);
    }
    public void deleteTask(int position){
        ToDoModel toDoModel = todoList.get(position);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        firestore.collection("Users").document(currentFirebaseUser.getUid()).collection("Tasks").document(toDoModel.TaskID).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return activity;
    }

    public void editTask(int position){
        ToDoModel toDoModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("due", toDoModel.getDue());
        bundle.putString("id", toDoModel.TaskID);

        addNewTask addNewTask = new addNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());

    }

    public void onBindViewHolder(ViewHolder holder, int position){
        ToDoModel item = todoList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));

        holder.mDueDate.setText("Due on " + item.getDue());

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firestore.collection("task").document(item.TaskID).update("status", 1);
                } else {
                    firestore.collection("task").document(item.TaskID).update("status", 0);
                }
            }
        });
    }

    public int getItemCount(){
        return todoList.size();
    }

    private boolean toBoolean(int status){
        return status!=0;
    }

    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        TextView mDueDate;

        ViewHolder(View view){
            super(view);
            mCheckBox = view.findViewById(R.id.toDoCheckBox);
            mDueDate = view.findViewById(R.id.dueDate);
        }

    }

}
