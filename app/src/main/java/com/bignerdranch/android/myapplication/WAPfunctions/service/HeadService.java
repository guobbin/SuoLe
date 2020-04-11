package com.bignerdranch.android.myapplication.WAPfunctions.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bignerdranch.android.myapplication.R;
import com.bignerdranch.android.myapplication.WAPfunctions.PlanActivity;
import com.bignerdranch.android.myapplication.WAPfunctions.db.Plan;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HeadService extends Service {
    public LocationClient mLocationClient;

    private  class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

//            double a = (double)bdLocation.getLatitude();
//            Intent i = new Intent(this, PlanActivity.class);
//            PendingIntent pi = PendingIntent.getActivity(this, 33, i, 0);
            List<Plan> plans = DataSupport.findAll(Plan.class);
            for (Plan plan:plans) {
                if (plan.getRemind().equals("开")&&!plan.getRemindPlace().equals("选择")) {
                    String Latitude, Longitude;
                    String[] arr2 = plan.getRemindPlaceNumber().split(" ");
                    Latitude = arr2[0];
                    Longitude = arr2[1];
                    if(getDistance((double)bdLocation.getLatitude(),
                            (double)bdLocation.getLongitude(), Double.valueOf(Latitude)
                            , Double.valueOf(Longitude))<100){
                            Intent intent1 =new Intent(getApplicationContext(), AlarmService.class);
                            intent1.putExtra("id", plan.getId());
                            startService(intent1);
//                        NotificationManager manager = (NotificationManager)
//                                getSystemService(Context.NOTIFICATION_SERVICE);
//                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                                .setContentText(getDistance((double)bdLocation.getLatitude(),
//                                        (double)bdLocation.getLongitude(),30.552477,103.992099)+"")
//                                .setContentTitle("sdfafaffd")
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                                .setAutoCancel(true)
//                                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                                .build();
//                        manager.notify(321, notification);
                    }
                }
            }

        }
        public double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
            // 纬度
            double lat1 = (Math.PI / 180) * latitude1;
            double lat2 = (Math.PI / 180) * latitude2;
            // 经度
            double lon1 = (Math.PI / 180) * longitude1;
            double lon2 = (Math.PI / 180) * longitude2;
            // 地球半径
            double R = 6371;
            // 两点间距离 km，如果想要米的话，结果*1000就可以了
            double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
            return d * 1000;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        initLocation();
        mLocationClient.start();
        remindClock();
        return super.onStartCommand(intent, flags, startId);
    }
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(30*1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
    private void remindClock() {
        int num=0;
        List<Plan> plans = DataSupport.findAll(Plan.class);
        for (Plan plan:plans) {
            if (plan.getRemind().equals("开")) {
                num++;
                int id = plan.getId();
                String timeStr;
                long intervalTime;
                if(plan.getType().equals("日计划")){
                    timeStr = plan.getRemindTime()+":00";
                    intervalTime=5*60*1000;
                }else if (plan.getType().equals("周计划")){
                    timeStr = plan.getRemindTime()+" 10:00:00";
                    intervalTime=24*60*60*1000;
                }else if (plan.getType().equals("月计划")) {
                    timeStr = plan.getRemindTime() + " 10:00:00";
                    intervalTime=7*24*60*60*1000;
                }else{
                    timeStr = plan.getRemindTime() + " 10:00:00";
                    intervalTime=30*7*24*60*60*1000;
                }
                Calendar c = Calendar.getInstance();
                try{
                    c.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(timeStr));
                }
                catch(Exception e){
                }
                long millionSeconds=c.getTimeInMillis();
                Intent intent1 =new Intent(this, AlarmService.class);
                intent1.putExtra("id", id);
                PendingIntent sender= PendingIntent.getService(this, id, intent1, 0);
                AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, millionSeconds, intervalTime, sender);
            }
        }
        if (num!=0) {
            Intent i = new Intent(this, PlanActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 33, i, 0);
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentText(num + "条将提醒")
                    .setContentTitle("您已开启提醒")
//                .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .build();
            startForeground(33, notification);
        }
        else{
            stopSelf();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
