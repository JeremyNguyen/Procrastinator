package com.example.procrastinator.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.example.procrastinator.adapter.TasksAdapter;
import com.example.procrastinator.model.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OnCategorySelectedListener implements AdapterView.OnItemSelectedListener {

    private final List<Task> tasks;
    private final TasksAdapter adapter;
    private final String[] categories;
    private final Context context;
    private final SwitchMaterial switchMaterial;

    public OnCategorySelectedListener(List<Task> tasks, TasksAdapter adapter, String[] categories, SwitchMaterial switchMaterial, Context context) {
        this.tasks = tasks;
        this.adapter = adapter;
        this.categories = categories;
        this.context = context;
        this.switchMaterial = switchMaterial;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (categories[position].equals(task.getCategory())) {
                filteredTasks.add(task);
            }
        }
        if (switchMaterial.isChecked()) {
            for (Iterator<Task> iterator = filteredTasks.iterator(); iterator.hasNext(); ) {
                Task task = iterator.next();
                if (task.isShared()) {
                    iterator.remove();
                }
            }
        }
        adapter.setItems(filteredTasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public Context getContext() {
        return context;
    }
}
