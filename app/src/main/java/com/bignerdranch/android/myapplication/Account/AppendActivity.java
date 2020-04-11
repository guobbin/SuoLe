package com.bignerdranch.android.myapplication.Account;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bignerdranch.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14/014.
 */

public class AppendActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Detail> detailList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private List<String> mList = new ArrayList<>();
    private TextView mCancelTextView;
    private Spinner mAppendSpinner;

    private Fragment mOutAppendFragment;
    private Fragment mInAppendFragment;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_append);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }
        InitFragment(0);
        mList.add("支出");
        mList.add("收入");
        mAppendSpinner = (Spinner)findViewById(R.id.spinner_append);
        mAdapter = new ArrayAdapter<>(this,R.layout.append_spinner,mList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAppendSpinner.setAdapter(mAdapter);
        mAppendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void  onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                if(position==1){
                    ClearFragment();
                    InitFragment(1);
                }
                if(position==0){
                    ClearFragment();
                    InitFragment(0);
                }
            }

            @Override
            public void  onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        mCancelTextView = (TextView)findViewById(R.id.cancel_text_view);
        mCancelTextView.setOnClickListener(this);
    }

    private void InitFragment(int index)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (index) {
            case 0:
                if (mOutAppendFragment== null) {
                    mOutAppendFragment= new OutAppendFragment();
                    transaction.replace(R.id.append_content,  mOutAppendFragment);
                } else {
                    transaction.show( mOutAppendFragment);
                }
                break;

            case 1:
                if (mInAppendFragment== null) {
                    mInAppendFragment= new InAppendFragment();
                    transaction.replace(R.id.append_content,  mInAppendFragment);
                } else {
                    transaction.show( mInAppendFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void ClearFragment()
    {
        mOutAppendFragment = null;
        mInAppendFragment= null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cancel_text_view:
                finish();
                break;
            default:
                break;
        }
    }
}
