package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {
    private TextView mTvLoginTextview;
    private TextView mRegisterTextView;
    private EditText mUserName;
    private EditText mPassword;
    private String userName,password,usernameID;
    private long exitTime;
    private String imgurl,petName,zhanghao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        autoLogin();
        setContentView(R.layout.login_interface);
        ExitApplication.getInstance().addActivity(this);

        initViews();
        Bmob.initialize(this,"15901fbabf4589c6bf0c5d7d4a75de92");

        mTvLoginTextview.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    userName=mUserName.getText().toString();
                                                    password=mPassword.getText().toString();

                                                    BmobQuery<User> query=new BmobQuery<User>();
                                                    query.addWhereEqualTo("phoneNumber",userName);
                                                    query.addWhereEqualTo("password",password);

                                                    query.findObjects(new FindListener<User>() {
                                                        @Override
                                                        public void done(List<User> users, BmobException e){
                                                            if(e==null) {
                                                                String gname=users.get(0).getPhoneNumber().toString();
                                                                String gpassword=users.get(0).getPassword().toString();
                                                                String name1=mUserName.getText().toString();
                                                                String password1=mPassword.getText().toString();
                                                                if(gname.equals(name1)&&gpassword.equals(password1)){
                                                                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                                                    usernameID=users.get(0).getObjectId().toString();
                                                                    petName=users.get(0).getPetName().toString();
                                                                    zhanghao=users.get(0).getUserName().toString();

                                                                   if(users.get(0).getPersonalIcon()!=null){
                                                                       imgurl=users.get(0).getPersonalIcon().getUrl();
                                                                   }
                                                                    saveUserName();
                                                                    Intent intent=new Intent(LoginActivity.this,FirstComeActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else{
                                                                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
        );
        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }
        );
    }

    private void initViews(){
        mTvLoginTextview=(TextView) findViewById(R.id.tv_login);
        mRegisterTextView=(TextView)findViewById(R.id.tv_register);
        mUserName=(EditText)findViewById(R.id.et_userName);
        mPassword=(EditText)findViewById(R.id.et_password);
    }

    private void autoLogin(){
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        usernameID=sharedPreferences.getString("usernameID","defaultname");
        if(usernameID.equals("defaultname") || usernameID.isEmpty()){

        }
        else{
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void saveUserName(){
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("phone",userName).commit();
        sharedPreferences.edit().putString("usernameID",usernameID).commit();
        sharedPreferences.edit().putString("imgurl",imgurl).commit();
        sharedPreferences.edit().putString("petName",petName).commit();
        sharedPreferences.edit().putString("userName",zhanghao).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当点击返回键以及点击重复次数为0
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitApplication.getInstance().exit();
        }
        return false;
    }

}
