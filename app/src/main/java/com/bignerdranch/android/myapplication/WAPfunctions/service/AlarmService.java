package com.bignerdranch.android.myapplication.WAPfunctions.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.bignerdranch.android.myapplication.R;
import com.bignerdranch.android.myapplication.WAPfunctions.PlanActivity;
import com.bignerdranch.android.myapplication.WAPfunctions.PlanEdit;
import com.bignerdranch.android.myapplication.WAPfunctions.ScanPlan;
import com.bignerdranch.android.myapplication.WAPfunctions.db.Plan;

import org.litepal.crud.DataSupport;

import java.util.List;


public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = intent.getIntExtra("id",100);
        String text ="This is content text";
        String title = "This is content title" ;
        List<Plan> plans = DataSupport.findAll(Plan.class);
        for (Plan plan:plans) {
            if(id==plan.getId()){
                text=plan.getBody();
                title = "记得您的"+plan.getType()+"哦";
            }
        }

        Intent i = new Intent(this, ScanPlan.class);
        i.putExtra("id",id);
        PendingIntent pi = PendingIntent.getActivity(this, id, i, 0);
        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentText(text)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();
        manager.notify(id, notification);
          return super.onStartCommand(intent, flags, startId);
    }

}
