package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by T540p on 2017/11/7.
 */

public class WriteCommentActivity extends AppCompatActivity {
    private EditText mWriteText;
    private TextView mDeliver;
    private String mWriter,Icon;
    private String mText;
    private String mingleNo;
    private String answerTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }
        setContentView(R.layout.activity_write_text);

        ExitApplication.getInstance().addActivity(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mingleNo=getIntent().getStringExtra("mingleId");
        answerTo=getIntent().getStringExtra("answerTo");
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        mWriter=sharedPreferences.getString("petName","defaultname");
        Icon=sharedPreferences.getString("imgurl","null");
        mWriteText=(EditText)findViewById(R.id.et_write_text);
        mDeliver=(TextView)findViewById(R.id.tv_deliver);

        Bmob.initialize(this,"15901fbabf4589c6bf0c5d7d4a75de92");
        mDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadText();

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void uploadText(){
        mText=mWriteText.getText().toString();
        if(mText.isEmpty()){
            Toast.makeText(WriteCommentActivity.this,"内容不能为空哦！！！",Toast.LENGTH_SHORT).show();
        }
        else{
            Comment text=new Comment();
            text.setObserver(mWriter);
            text.setContent(mText);
            text.setObPhoto(Icon);
            text.setMingleNo(mingleNo);
            text.setAuthor(answerTo);

            text.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Toast.makeText(WriteCommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(WriteCommentActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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

