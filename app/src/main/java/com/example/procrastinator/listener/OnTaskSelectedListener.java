package com.example.procrastinator.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.example.procrastinator.adapter.TasksAdapter;
import com.example.procrastinator.model.Task;

import java.util.List;
import java.util.stream.Collectors;

public class OnTaskSelectedListener implements AdapterView.OnItemSelectedListener {

    private final List<Task> tasks;
    private final TasksAdapter adapter;
    private final String[] categories;
    private final Context context;

    public OnTaskSelectedListener(List<Task> tasks, TasksAdapter adapter, String[] categories, Context context) {
        this.tasks = tasks;
        this.adapter = adapter;
        this.categories = categories;
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (tasks.size() > 0) {
            List<Task> filteredTasks = tasks.stream().filter(task -> categories[position].equals(task.getCategory())).collect(Collectors.toList());
            adapter.setItems(filteredTasks);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public Context getContext() {
        return context;
    }
}
