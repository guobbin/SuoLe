package com.bignerdranch.android.myapplication;

         import android.app.ActivityManager;
         import android.app.Notification;
         import android.app.NotificationManager;
         import android.app.PendingIntent;
         import android.content.Context;
         import android.content.DialogInterface;
         import android.content.Intent;
         import android.content.SharedPreferences;
         import android.graphics.Bitmap;
         import android.graphics.BitmapFactory;
         import android.graphics.Color;
         import android.os.Build;
         import android.os.Bundle;
         import android.os.Environment;
         import android.os.Handler;
         import android.os.Message;
         import android.support.annotation.NonNull;
         import android.support.design.widget.NavigationView;
         import android.support.design.widget.TabLayout;
         import android.support.v4.app.Fragment;
         import android.support.v4.app.FragmentActivity;
         import android.support.v4.app.FragmentManager;
         import android.support.v4.app.FragmentPagerAdapter;
         import android.support.v4.app.FragmentTransaction;
         import android.support.v4.view.GravityCompat;
         import android.support.v4.view.ViewPager;
         import android.support.v4.widget.DrawerLayout;
         import android.support.v7.app.ActionBar;
         import android.support.v7.app.AlertDialog;
         import android.support.v7.app.AppCompatActivity;
         import android.support.v7.app.NotificationCompat;
         import android.support.v7.widget.Toolbar;
         import android.view.KeyEvent;
         import android.view.Menu;
         import android.view.MenuItem;
         import android.view.View;
         import android.view.View.OnClickListener;
         import android.widget.ImageView;
         import android.widget.LinearLayout;
         import android.widget.TextView;
         import android.widget.Toast;

         import com.bignerdranch.android.myapplication.Personal.AboutActivity;
         import com.bignerdranch.android.myapplication.Personal.NotifyActivity;
         import com.bignerdranch.android.myapplication.Personal.SecurityActivity;
         import com.bignerdranch.android.myapplication.WAPfunctions.PlanActivity;
         import com.bignerdranch.android.myapplication.WAPfunctions.service.HeadService;
         import com.bumptech.glide.Glide;
         import com.bumptech.glide.load.resource.drawable.GlideDrawable;
         import com.bumptech.glide.request.animation.GlideAnimation;
         import com.bumptech.glide.request.target.SimpleTarget;

         import java.io.File;
         import java.util.ArrayList;
         import java.util.List;

         import cn.bmob.v3.Bmob;
         import cn.bmob.v3.BmobQuery;
         import cn.bmob.v3.exception.BmobException;
         import cn.bmob.v3.listener.FindListener;
         import cn.bmob.v3.listener.QueryListener;
         import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {

    private long exitTime;
    private String imgurl=null;
    private Handler handler;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TabLayout tab_FindFragment_title;                            //定义TabLayout
    private ViewPager vp_FindFragment_pager;                             //定义viewPager
    private FragmentPagerAdapter fAdapter;                               //定义adapter

    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表

    private NewsInterfaceFragment mNewsInterfaceFragment;
    private MingleFragment mMingleFragment;
    private FuncFragment mFuncFragment;

    private ImageView mImageView;
    private Toolbar mToolbar;
    private String pushText,pushUrl,push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.main_interface);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
             ExitApplication.getInstance().addActivity(this);

             Bmob.initialize(this,"15901fbabf4589c6bf0c5d7d4a75de92");

             mToolbar=(Toolbar)findViewById(R.id.main_toolbar);
             setSupportActionBar(mToolbar);
             mDrawerLayout=(DrawerLayout)findViewById(R.id.main_drawer_layout);
             ActionBar actionBar=getSupportActionBar();
             if(actionBar!=null){
                 actionBar.setDisplayHomeAsUpEnabled(true);
                 actionBar.setHomeAsUpIndicator(R.drawable.list_button);
             }

             SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
             push=sharedPreferences.getString("push","yes");
             this.mNavigationView=(NavigationView)findViewById(R.id.main_nav);
             View headerView=mNavigationView.inflateHeaderView(R.layout.main_nav_header);
             mImageView=(ImageView)headerView.findViewById(R.id.nav_im_header);

             loadPetPhoto();
             getPush();

             mNavigationView.setCheckedItem(R.id.nav_myinfo);

             mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                                   @Override
                                                                   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                                                       int id=item.getItemId();
                                                                       switch(id){
                                                                           case R.id.nav_myinfo://个人资料
                                                                               Intent intent=new Intent(MainActivity.this,MyInfomationActivity.class);
                                                                               startActivity(intent);
                                                                               break;
                                                                           case R.id.nav_notify://消息通知
                                                                               Intent intent1=new Intent(MainActivity.this,NotifyActivity.class);
                                                                               startActivity(intent1);
                                                                               break;
                                                                           case R.id.nav_setting://账号安全
                                                                               Intent intent2=new Intent(MainActivity.this, PasswordInterActivity.class);
                                                                               startActivity(intent2);
                                                                               break;
                                                                           case R.id.nav_link://辅助链接
                                                                               break;
                                                                           case R.id.nav_about://关于所乐
                                                                               Intent intent4 = new Intent(MainActivity.this, AboutActivity.class);
                                                                               startActivity(intent4);
                                                                               break;
                                                                           case R.id.nav_help://帮助与反馈
                                                                               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                               builder.setTitle("帮助与反馈");
                                                                               builder.setMessage("您可以向992558763@qq.com发送邮件寻求帮助或提供反馈，我们会尽快阅读并回复您！");
                                                                               builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                                                                               {
                                                                                   @Override
                                                                                   public void onClick(DialogInterface dialog, int which)
                                                                                   {

                                                                                   }
                                                                               });
                                                                               builder.create().show();
                                                                               break;
                                                                           case R.id.nav_quit:
                                                                               SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
                                                                               sharedPreferences.edit().clear().commit();
                                                                               Intent intent7=new Intent(MainActivity.this,LoginActivity.class);
                                                                               startActivity(intent7);
                                                                       }
                                                                       return true;
                                                                   }
                                                               });
             tab_FindFragment_title = (TabLayout)findViewById(R.id.tab_FindFragment_title);
             vp_FindFragment_pager = (ViewPager)findViewById(R.id.vp_FindFragment_pager);

             //初始化各fragment
             mFuncFragment = new FuncFragment();
             mNewsInterfaceFragment = new NewsInterfaceFragment();
             mMingleFragment = new MingleFragment();

             //将fragment装进列表中
             list_fragment = new ArrayList<>();
             list_fragment.add(mFuncFragment);
             list_fragment.add(mMingleFragment);
             list_fragment.add(mNewsInterfaceFragment);

             //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
             list_title = new ArrayList<>();
             list_title.add("首页");
             list_title.add("树洞");
             list_title.add("新闻");



             tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);

             tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(0)));
             tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(1)));
             tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(2)));


             fAdapter = new FindTabAdapter(MainActivity.this.getSupportFragmentManager(),list_fragment,list_title);
             vp_FindFragment_pager.setAdapter(fAdapter);
             tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
//             Intent intent1 = new Intent(MainActivity.this, HeadService.class);
//                startService(intent1);
             }

    private void loadPetPhoto(){
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
       imgurl=sharedPreferences.getString("imgurl","defaultname");
        Glide.with(MainActivity.this).load(imgurl).error(R.mipmap.person).transform(new GlideCircleTransform(MainActivity.this)).into(mImageView);
    }

    public void push() {
        if(push.equals("yes")){
            Intent i = new Intent(MainActivity.this, NewsDisplayActivity.class);
            i.putExtra("news_url",pushUrl);
            PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, i, 0);
            NotificationManager manager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentText(pushText)
                    .setContentTitle("Ding~校园快报")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            manager.notify(0, notification);
        }
    }

    public void getPush(){
        BmobQuery<SlideView> query=new BmobQuery<>();
        query.setLimit(1);
        query.findObjects(new FindListener<SlideView>() {
                              @Override
                              public void done(List<SlideView> list, BmobException e) {
                                  if(e==null){
                                      for(SlideView slideView:list){
                                          pushUrl=slideView.getWeb();
                                          pushText=slideView.getDetail();
                                      }
                                      push();
                                  }else{
                                      e.printStackTrace();
                                  }
                              }
                          }
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当点击返回键以及点击重复次数为0
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
           if((System.currentTimeMillis()-exitTime) > 2000){
               Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
               exitTime = System.currentTimeMillis();
           }
            else{
               ExitApplication.getInstance().exit();
           }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(MainActivity.this).load(imgurl).error(R.mipmap.default_personal_image).transform(new GlideCircleTransform(MainActivity.this)).into(mImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_toolbar_item,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
//            case R.id.main_backup:
//                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}