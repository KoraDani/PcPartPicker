package com.example.pcconf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class NotificationHandler {
    private static final String CHANNEL_ID = "pc_picker_chanel";
    private NotificationManager notiManager;
    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        this.notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PcPartPicker", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Note form app");
        this.notiManager.createNotificationChannel(channel);
    }

    public void send(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID).setContentTitle("PcPartPicker").setContentText(msg).setSmallIcon(R.drawable.circl);

        this.notiManager.notify(0,builder.build());
    }

}
