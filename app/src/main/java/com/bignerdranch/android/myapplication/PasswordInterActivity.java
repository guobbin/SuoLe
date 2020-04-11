package com.bignerdranch.android.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;

import cn.bmob.v3.Bmob;

/**
 * Created by T540p on 2017/11/12.
 */

public class PasswordInterActivity extends AppCompatActivity {
    private CardView mCardView;
    private TextView mPhone,mUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);

        ExitApplication.getInstance().addActivity(this);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }


        mPhone=(TextView)findViewById(R.id.user_phone_change);
        mUserName=(TextView)findViewById(R.id.user_id_change);
        mCardView=(CardView)findViewById(R.id.cardview_password);

        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        mPhone.setText(sharedPreferences.getString("phone",""));
        mUserName.setText(sharedPreferences.getString("userName",""));
        mCardView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent=new Intent(PasswordInterActivity.this,PasswordChangeActivity.class);
                                             startActivity(intent);
                                         }
                                     }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.myinformation_interface,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当点击返回键以及点击重复次数为0
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 执行事件
            setResult(RESULT_OK);
            finish();

        }
        return false;
    }
}
