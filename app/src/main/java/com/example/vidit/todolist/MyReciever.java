package com.example.vidit.todolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MyReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=26)
        {
            NotificationChannel channel=new NotificationChannel("my_channel_id","ToDo Channel",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        String content=intent.getStringExtra("TITLE_N");
        int id=intent.getIntExtra("ID",0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"my_channel_id");
        builder.setContentTitle("Reminder");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        Intent intent1=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,id,intent1,0);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        Notification notification=builder.build();
        manager.notify(id,notification);
    }
}
