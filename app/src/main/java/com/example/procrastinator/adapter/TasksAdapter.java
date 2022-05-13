package com.example.procrastinator.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.procrastinator.R;
import com.example.procrastinator.activity.MainActivity;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;

import java.util.List;

public class TasksAdapter extends ArrayAdapter<Task> {

    private List<Task> tasks;

    public TasksAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        Context context = getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.list_item_textView);
        textView.setText(task.getTitle());
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("task", task);
            intent.putExtra("mode", AppConstant.MODE_EDIT);
            context.startActivity(intent);
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    public void setItems(List<Task> tasks) {
        this.tasks = tasks;
    }
}
