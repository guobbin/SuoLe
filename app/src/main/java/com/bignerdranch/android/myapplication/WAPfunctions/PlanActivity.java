package com.bignerdranch.android.myapplication.WAPfunctions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import com.bignerdranch.android.myapplication.WAPfunctions.db.Plan;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.bignerdranch.android.myapplication.R;
import com.bignerdranch.android.myapplication.WAPfunctions.service.AlarmService;
import com.bignerdranch.android.myapplication.WAPfunctions.service.HeadService;

public class PlanActivity extends AppCompatActivity {
    private List<PlanForSwipeMenu> planContentList = new ArrayList<>();
    private AppAdapter planAdapter;
    private SwipeMenuListView mListView;
//    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    //    private TextView title;
//    private Button typeList;
    private Button back;
//    private Button titleButton;
    private LinearLayout  changeTypeButton;
    private TextView  changeType;
    private Button addPlan;
    private String plansType;
    private SwipeMenuItem remindItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_plan);
        Toast.makeText(this, "侧滑可删除和开启关闭提醒哦^0^", Toast.LENGTH_SHORT).show();
//        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        changeType = (TextView)findViewById(R.id.title_plan) ;
        changeTypeButton = (LinearLayout) findViewById(R.id.change_type) ;
        back = (Button) findViewById(R.id.back_to_func);
        addPlan = (Button) findViewById(R.id.add_plan);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        planAdapter = new AppAdapter();
        if(getIntent().hasExtra("type")){
            plansType = getIntent().getStringExtra("type");
        }
        if (plansType==null){
            plansType = "日计划";
        }
        showPlansInfo(plansType);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlanForSwipeMenu selectedPlan = planContentList.get(position);
                Intent intent = new Intent(PlanActivity.this, PlanEdit.class);
//                intent.putExtra("body",selectedPlan.getBody());
                intent.putExtra("type",plansType);
//                intent.putExtra("remindTime",selectedPlan.getTime());
//                intent.putExtra("remindPlace",selectedPlan.getPlace());
                intent.putExtra("id",selectedPlan.getId());
//                intent.putExtra("placeNum",selectedPlan.getPlaceNum());
                startActivityForResult(intent, 1);
            }
        });
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
//            @Override
//            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);
//                        }catch (InterruptedException e ){
//                            e.printStackTrace();
//                        }
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showPlansInfo(plansType);
//                            }
//                        });
//                    }
//                }).start();
//
//            }
//        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        changeTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivity.this, PlanEdit.class);
                intent.putExtra("type",plansType);
                startActivity(intent);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu){
                // create "open" item
                remindItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                remindItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xCC,
                        0xCC)));
                // set item width
                remindItem.setWidth(dp2px(90));
                // set item title
                remindItem.setTitle("提醒");
                // set item title fontsize
                remindItem.setTitleSize(18);
                // set item title font color
                remindItem.setTitleColor(Color.WHITE);
                // add to menu

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xCC,
                        0xCC, 0xFF)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu

                SwipeMenuItem closeRemindItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                closeRemindItem.setBackground(new ColorDrawable(Color.rgb(0x99, 0xCC,
                        0xCC)));
                // set item width
                closeRemindItem.setWidth(dp2px(90));
                // set item title
                closeRemindItem.setTitle("关闭提醒");
                // set item title fontsize
                closeRemindItem.setTitleSize(18);
                // set item title font color
                closeRemindItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(closeRemindItem);
                menu.addMenuItem(remindItem);
                menu.addMenuItem(deleteItem);

            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final PlanForSwipeMenu item = planContentList.get(position);
                int id = item.getId();
                final int psion = position;
                switch (index) {
                    case 0:
                        Plan plan3=DataSupport.find(Plan.class, id);
                        if(plan3.getRemind().equals("关")) {
                            Toast.makeText(PlanActivity.this, "这个计划还没开启提醒呢¬_¬", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(PlanActivity.this, AlarmService.class);
                            PendingIntent pi = PendingIntent.getService(PlanActivity.this,id, intent, 0);
                            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            manager.cancel(pi);
                            Plan plan1 = new Plan();
                            plan1.setRemind("关");
                            plan1.update(id);
                            Intent intent1 = new Intent(PlanActivity.this, HeadService.class);
                            startService(intent1);
                            Toast.makeText(PlanActivity.this, "提醒关闭成功啦", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:

//                        long intervalTime;
                        String timeStr;
                        if(item.getType().equals("日计划")){
                            timeStr = item.getTime()+":00";
//                            intervalTime=60*60*1000;
                        }else if (item.getType().equals("周计划")){
                            timeStr = item.getTime()+" 10:00:00";
//                            intervalTime=24*60*60*1000;
                        }else if (item.getType().equals("月计划")) {
                            timeStr = item.getTime() + " 10:00:00";
//                            intervalTime=7*24*60*60*1000;
                        }else{
                            timeStr = item.getTime() + " 10:00:00";
//                            intervalTime=30*7*24*60*60*1000;
                        }

                        Calendar c = Calendar.getInstance();
                        try{
                            c.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(timeStr));
                        }
                        catch(Exception e){
                        }
                        long millionSeconds=c.getTimeInMillis();
                        Calendar calendar= Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());//将时间设定为系统目前的时间

                        if(calendar.getTimeInMillis()<millionSeconds) {
//                            Intent intent =new Intent(PlanActivity.this, AlarmService.class);
//                            intent.putExtra("id", id);
//                            PendingIntent sender= PendingIntent.getService(PlanActivity.this, id, intent, 0);
//                            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
//                            alarm.setRepeating(AlarmManager.RTC_WAKEUP, millionSeconds, intervalTime, sender);

//                            List<plan> plans = DataSupport.findAll(plan.class);
//                            for (plan plan:plans) {
//                                if (id == plan.getId()) {
//                                    plan.setRemind(true);
//                                }
//                            }
//                            List<plan> plans1 = DataSupport.findAll(plan.class);

                            Plan plan2=DataSupport.find(Plan.class, id);
                            if(plan2.getRemind().equals("关")) {
                                Plan plan1 = new Plan();
                                plan1.setRemind("开");
                                plan1.update(id);
//                            List<plan> plans = DataSupport.findAll(plan.class);
                            }
                            Intent intent1 = new Intent(PlanActivity.this, HeadService.class);
                            startService(intent1);
//                            LBSTest();

                            Toast.makeText(PlanActivity.this, "提醒开启成功啦^0^", Toast.LENGTH_SHORT).show();

                        }else{
                            Plan p = DataSupport.find(Plan.class, id);
                            if(p.getRemindPlace().equals("选择")) {
                                Toast.makeText(PlanActivity.this, "修改好时间再点我哦^_~", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(PlanActivity.this, "最迟给您提醒的时间也要设置正确哦", Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case 2:
                        // delete
                        Plan p = DataSupport.find(Plan.class, id);
                        final int ID = id;
                        if(p.getRemind().equals("开")){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(PlanActivity.this);
                            dialog.setMessage("该计划已开启提醒，确定删除？");
                            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(PlanActivity.this, AlarmService.class);
                                    PendingIntent pi = PendingIntent.getService(PlanActivity.this,ID, intent, 0);
                                    AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    manager.cancel(pi);
                                    delete(item);
                                    planContentList.remove(psion);
                                    isNoPlan();
                                    planAdapter.notifyDataSetChanged();
                                    Intent intent1 = new Intent(PlanActivity.this, HeadService.class);
                                    startService(intent1);
                                }
                            });
                            dialog.setNegativeButton("不，点错啦", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                finish();
                                }
                            });
                            dialog.show();
                        }
                        else {
                            delete(item);
                            planContentList.remove(psion);
                            isNoPlan();
                            planAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
//                swipeRefresh.setEnabled(false);
            }
            @Override
            public void onSwipeEnd(int position) {
//                swipeRefresh.setEnabled(false);

            }
        });
        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
//                swipeRefresh.setEnabled(false);
            }

            @Override
            public void onMenuClose(int position) {
//                swipeRefresh.setEnabled(true);
            }
        });
    }

    public void showPlansInfo(String Type) {
        plansType=Type;
        changeType.setText(Type);
        initPlanContentList();
        mListView.setAdapter(planAdapter);
//        swipeRefresh.setRefreshing(false);
    }

    private void initPlanContentList() {
        planContentList.clear();
        List<Plan> plans = DataSupport.findAll(Plan.class);

        for(Plan plan: plans){
            if(plan.getType().equals(plansType)){
                planContentList.add(new PlanForSwipeMenu(plan.getRemindPlace(),plan.getBody(),plan.getRemindTime(),plan.getType(),plan.getId()));
            }
        }
        isNoPlan();
    }

    private void isNoPlan() {
        TextView noPlanText = (TextView) findViewById(R.id.no_plan_text);
        if (planContentList.size()==0){
            noPlanText.setVisibility(View.VISIBLE);
        }else {
            noPlanText.setVisibility(View.GONE);
        }
    }

    private void delete(PlanForSwipeMenu item) {
        // delete app
//        DataSupport.deleteAll(plan.class,"type=? and body=?",plansType,item.getBody());
        DataSupport.delete(Plan.class,item.getId());
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return planContentList.size();
        }

        @Override
        public PlanForSwipeMenu getItem(int position) {
            return planContentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_plan, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            PlanForSwipeMenu item = getItem(position);
            holder.tv_name.setText(item.getBody());
//            if(item.getRemind().equals("开")){
            if(!item.getTime().equals("选择")) {
                holder.rm_time.setText(item.getTime());
            }
            if(!item.getPlace().equals("选择")) {
                holder.rm_place.setText(item.getPlace());
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            TextView rm_time;
            TextView rm_place;
            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                rm_time = (TextView) view.findViewById(R.id.rm_time);
                rm_place = (TextView) view.findViewById(R.id.rm_place);
                view.setTag(this);
            }
        }

    }
    class PlanForSwipeMenu {
        private String body;
        private String time;
        private String type;
        private String place;
        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        private int id;
        PlanForSwipeMenu(String place, String body, String time, String type, int id){
            this.place = place;
            this.body = body;
            this.type = type;
            this.time = time;
            this.id = id;
//            this.idString  = idString;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initPlanContentList();
        planAdapter.notifyDataSetChanged();
    }
}
