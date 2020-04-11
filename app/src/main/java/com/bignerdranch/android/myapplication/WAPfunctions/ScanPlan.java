package com.bignerdranch.android.myapplication.WAPfunctions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.myapplication.R;
import com.bignerdranch.android.myapplication.WAPfunctions.db.Plan;
import com.bignerdranch.android.myapplication.WAPfunctions.service.AlarmService;
import com.bignerdranch.android.myapplication.WAPfunctions.service.HeadService;

import org.litepal.crud.DataSupport;

import java.security.interfaces.DSAKey;
import java.util.List;

public class ScanPlan extends AppCompatActivity {
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }

        setContentView(R.layout.activity_scan_plan);
        TextView textView = (TextView) findViewById(R.id.remind_content);
        Button btBack = (Button)findViewById(R.id.scan_back);
        Button btCancel = (Button) findViewById(R.id.scan_cancel_remind);
        TextView title = (TextView) findViewById(R.id.scan_title);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",100);

        List<Plan> plans = DataSupport.findAll(Plan.class);
        for (Plan plan:plans) {
            if(id==plan.getId()){
                textView.setText(plan.getBody());
                title.setText(plan.getType());
            }
        }
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plan s = new Plan();
                s.setRemind("关");
                s.update(id);
                int num=0;
                List<Plan> plans1 = DataSupport.findAll(Plan.class);
                for (Plan p:plans1) {
                    if (p.getRemind().equals("开")) {
                        num++;
                    }
                }
                Intent intent = new Intent(ScanPlan.this, AlarmService.class);
                PendingIntent pi = PendingIntent.getService(ScanPlan.this,id, intent, 0);
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                manager.cancel(pi);
                Intent intent1 = new Intent(ScanPlan.this, HeadService.class);
                if(num!=0) {
                    startService(intent1);
                }else{
                    stopService(intent1);
                }
                Toast.makeText(ScanPlan.this, "提醒关闭成功", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 100);
    }
}
