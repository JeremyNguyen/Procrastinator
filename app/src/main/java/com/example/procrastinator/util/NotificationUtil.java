package com.example.procrastinator.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.procrastinator.R;
import com.example.procrastinator.activity.RemindActivity;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;

public class NotificationUtil {

    public static void sendMessageNotification(String title, String text, int notificationId, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstant.CHANNEL_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);
        notificationManager.notify(notificationId, builder.build());
    }

    public static void sendReminderNotification(Task task, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstant.CHANNEL_REMINDER)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(task.getTitle())
                .setAutoCancel(true);
        Intent intent = new Intent(context, RemindActivity.class);
        intent.putExtra(AppConstant.EXTRA_TASK, task);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        int flags;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else {
            flags = PendingIntent.FLAG_UPDATE_CURRENT;
        }
        PendingIntent pi = PendingIntent.getActivity(context, task.getId().hashCode(), intent, flags);
        builder.setContentIntent(pi);
        notificationManager.notify(task.getId().hashCode(), builder.build());
    }
}
