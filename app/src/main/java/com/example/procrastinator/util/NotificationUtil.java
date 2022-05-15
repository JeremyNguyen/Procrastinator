package com.example.procrastinator.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.procrastinator.R;
import com.example.procrastinator.activity.MainActivity;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.model.Task;

public class NotificationUtil {

    public static void sendMessageNotification(String title, String text, int notificationId, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(AppConstant.CHANNEL_MESSAGE, AppConstant.CHANNEL_MESSAGE, AppConstant.CHANNEL_MESSAGE + " channel", notificationManager);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstant.CHANNEL_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);
        notificationManager.notify(notificationId, builder.build());
    }

    public static void sendReminderNotification(Task task, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(AppConstant.CHANNEL_REMINDER, AppConstant.CHANNEL_REMINDER, AppConstant.CHANNEL_REMINDER + " channel", notificationManager);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AppConstant.CHANNEL_REMINDER)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(task.getTitle())
                .setAutoCancel(true);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstant.EXTRA_TASK, task);
        intent.putExtra(AppConstant.EXTRA_MODE, AppConstant.MODE_REMIND);
        PendingIntent pi = PendingIntent.getActivity(context, task.getId().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pi);
        notificationManager.notify(task.getId().hashCode(), builder.build());
    }

    private static void createNotificationChannel(String id, String name, String description, NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }
}
