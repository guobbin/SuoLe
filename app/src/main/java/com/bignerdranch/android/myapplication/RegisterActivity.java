package com.bignerdranch.android.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.R.attr.button;
import static com.bignerdranch.android.myapplication.R.id.Message_btn;

/**
 * Created by T540p on 2017/8/1.
 */

public class RegisterActivity extends AppCompatActivity{
    private EditText userName,phoneNumber,password,passwordAgain,mPassWord;
    private TextView register,login;
    private String username,pass,passAgain,phone,passWord;
    private Button mIdentifyingCode;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_interface);
        ExitApplication.getInstance().addActivity(this);
        initViews();
        Bmob.initialize(this,"15901fbabf4589c6bf0c5d7d4a75de92");
        register.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(flag){
                                                registerUser();
                                            }
                                            else{
                                                Toast.makeText(RegisterActivity.this,"请进行验证码验证",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
        );
        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                         startActivity(intent);
                                     }
                                 }

        );
        mIdentifyingCode.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    getIdentifyingCode();
                                                }
                                            }

        );
    }

    private void initViews(){
        register = (TextView) findViewById(R.id.tv_register_now);
        passwordAgain = (EditText) findViewById(R.id.passwordAgain);
        password = (EditText) findViewById(R.id.password);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        userName = (EditText) findViewById(R.id.userName);
        mPassWord=(EditText)findViewById(R.id.passWord_et);
        login = (TextView) findViewById(R.id.tv_return_login);
        mIdentifyingCode=(Button)findViewById(Message_btn);
    }

    private void registerUser(){
        username=userName.getText().toString();
        pass=password.getText().toString();
        phone=phoneNumber.getText().toString();
        passAgain=passwordAgain.getText().toString();
        passWord=mPassWord.getText().toString();
        Pattern p = Pattern.compile("[0-9A-Za-z]{8,16}$");
        Matcher m = p.matcher(username);

        if(username==null || pass==null || phone==null || passAgain==null || passWord==null){
            userName.setText("");
            password.setText("");
            phoneNumber.setText("");
            passwordAgain.setText("");
            Toast.makeText(this, "请填入完整信息", Toast.LENGTH_SHORT).show();
        }
        else{
            if(m.matches() ){
                if(password.getTextSize()>=6){

                }else {
                    Toast.makeText(this,"密码少于6个字符，请重新输入",Toast.LENGTH_SHORT).show();
                    password.setText("");
                    passwordAgain.setText("");
                }
                if(pass.equals(passAgain)){
                    BmobQuery<User> query2=new BmobQuery<User>();
                    query2.addWhereEqualTo("userName",username);
                    query2.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                if(list.size()>=1){
                                    userName.setText("");
                                    Toast.makeText(RegisterActivity.this, "该用户名已被注册", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    BmobSMS.verifySmsCode(phone, passWord, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                registerSave();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "用户名匹配出错，请重试", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    password.setText("");
                    passwordAgain.setText("");
                    Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();

                }
            }
            else{
                userName.setText("");
                Toast.makeText(RegisterActivity.this,"用户名不符合要求，请重新输入",Toast.LENGTH_LONG).show();

            }
        }
    }

    private void registerSave(){
        User user=new User();
        user.setPhoneNumber(phone);
        user.setPassword(pass);
        user.setUserName(username);
        user.setPetName("所乐用户");
        user.save(new SaveListener<String>() {
                      @Override
                      public void done(String s, BmobException e) {
                          if(e==null){
                              Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                              Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                              startActivity(intent);
                          }
                          else{
                              Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                          }
                      }
                  }

        );
    }

    private void getIdentifyingCode(){
        phone=phoneNumber.getText().toString();
        if (phone.length() != 11) {
            Toast.makeText(this, "请输入11位有效手机号码", Toast.LENGTH_SHORT).show();
        }
        else {
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereEqualTo("phoneNumber",phone);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list1, BmobException e) {
                if (e == null) {
                    if(list1.size()>=1){
                        phoneNumber.setText("");
                        Toast.makeText(RegisterActivity.this, "该手机号已被注册", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        BmobSMS.requestSMSCode(phone, "短信模板", new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException ex) {
                                if (ex == null) {
                                    flag = true;
                                    //发送成功时，让获取验证码按钮不可点击，且为灰色
                                     mIdentifyingCode.setEnabled(false);
                                     mIdentifyingCode.setBackgroundColor(Color.GRAY);
                                     Toast.makeText(RegisterActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
                                    // /*
                                    // * 倒计时1分钟操作
                                    // */
                                    new CountDownTimer(60000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            mIdentifyingCode.setBackgroundResource(R.drawable.identifying_code_button_shape02);
                                            mIdentifyingCode.setText(millisUntilFinished / 1000 + "秒");
                                        }

                                        @Override
                                        public void onFinish() {
                                            mIdentifyingCode.setEnabled(true);
                                            mIdentifyingCode.setBackgroundResource(R.drawable.identifying_code_button_shape);
                                            mIdentifyingCode.setText("重新发送");
                                        }
                                    }.start();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "验证码发送失败，请检查网络连接", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "手机号匹配出错，请重试", Toast.LENGTH_LONG).show();
                }
            }
        });



        }

    }
}
