package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by T540p on 2017/11/12.
 */

public class PasswordChangeActivity extends AppCompatActivity {
    private EditText mOldPassword,mNewPassword,mNewPassword2;
    private TextView mTextView;
    private String usernameID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change_interface);

        ExitApplication.getInstance().addActivity(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }
        Bmob.initialize(this, "15901fbabf4589c6bf0c5d7d4a75de92");



        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        usernameID=sharedPreferences.getString("usernameID","defaultname");

        mOldPassword=(EditText)findViewById(R.id.et_oldPassword);
        mNewPassword=(EditText)findViewById(R.id.et_newPassword);
        mNewPassword2=(EditText)findViewById(R.id.et_newPassword2);
        mTextView=(TextView)findViewById(R.id.tv_change_password);
        mTextView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             judgePassword();
                                         }
                                     }

        );
    }

    public void judgePassword(){
        if(mNewPassword.getTextSize()>=6){
            if((mNewPassword.getText().toString()).equals(mNewPassword2.getText().toString())){
                BmobQuery<User> query=new BmobQuery<>();
                query.getObject(usernameID, new QueryListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if(e==null){
                                    if((user.getPassword()).equals(mOldPassword.getText().toString())){
                                        upload();
                                    }else{
                                        Toast.makeText(PasswordChangeActivity.this,"原密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(PasswordChangeActivity.this,"网络错误，请重试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }else{
                Toast.makeText(this,"新密码输入不一致！",Toast.LENGTH_SHORT).show();
                mNewPassword.setText("");
                mNewPassword2.setText("");
            }
        }else{
            Toast.makeText(this,"密码少于6位，请重新输入",Toast.LENGTH_SHORT).show();
            mNewPassword.setText("");
            mNewPassword2.setText("");
        }
    }

    public void upload(){
        User user=new User();
        user.setPassword(mNewPassword.getText().toString());
        user.update(usernameID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(PasswordChangeActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PasswordChangeActivity.this,"密码更改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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