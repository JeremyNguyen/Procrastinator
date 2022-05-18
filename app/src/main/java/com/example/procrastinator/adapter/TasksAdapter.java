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
import com.example.procrastinator.util.DatabaseUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksAdapter extends ArrayAdapter<Task> {

    private List<Task> tasks;
    private final String user;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public TasksAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.tasks = tasks;
        this.user = DatabaseUtil.getUser(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = tasks.get(position);
        Context context = getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        TextView taskTitle = convertView.findViewById(R.id.list_item_title);
        taskTitle.setText(task.getTitle());
        TextView taskRemind = convertView.findViewById(R.id.list_item_remind);
        if (user != null) {
            Date date;
            if (AppConstant.USER_NOEMIE.equals(user)) {
                date = task.getRemindNoemie().toDate();
            } else {
                date = task.getRemindJeremy().toDate();
            }
            taskRemind.setText(formatter.format(date));
        }

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(AppConstant.EXTRA_TASK, task);
            intent.putExtra(AppConstant.EXTRA_MODE, AppConstant.MODE_EDIT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
