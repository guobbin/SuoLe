package com.bignerdranch.android.myapplication.Account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bignerdranch.android.myapplication.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout mLlDetail;
    private LinearLayout mLlAppend;
    private LinearLayout mLlGraph;
    private LinearLayout mLlInformation;
    private LinearLayout mLlCategory;

    private ImageView mIvDetail;
    private ImageView mIvAppend;
    private ImageView mIvGraph;
    private ImageView mIvInformation;
    private ImageView mIvCategory;

    private TextView mTvDetail;
    private TextView mTvAppend;
    private TextView mTvGraph;
    private TextView mTvInformation;
    private TextView mTvCategory;

    private Fragment mDetailsFragment;
    private Fragment mDetailFragment;
    private Fragment mGraphFragment;
    private Fragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }
        setContentView(R.layout.account_activity_main);

        // 初始化控件
        InitView();
        // 初始化底部按钮事件
        InitEvent();
        // 初始化并设置当前Fragment
        mIvDetail.setImageResource(R.drawable.ic_detail_1);
        mTvDetail.setTextColor(getResources().getColor(R.color.button_blue));
        InitFragment(0);
    }


    private void InitView()
    {
        this.mLlDetail = (LinearLayout) findViewById(R.id.ll_detail);
        this.mLlAppend = (LinearLayout) findViewById(R.id.ll_append);
        this.mLlCategory = (LinearLayout)findViewById(R.id.ll_category);
        this.mLlInformation = (LinearLayout)findViewById(R.id.ll_information);
        this.mLlGraph= (LinearLayout)findViewById(R.id.ll_graph);

        this.mIvDetail = (ImageView) findViewById(R.id.iv_detail);
        this.mIvAppend = (ImageView) findViewById(R.id.iv_append);
        this.mIvCategory = (ImageView) findViewById(R.id.iv_category);
        this.mIvInformation= (ImageView) findViewById(R.id.iv_information);
        this.mIvGraph= (ImageView) findViewById(R.id.iv_graph);

        this.mTvDetail = (TextView) findViewById(R.id.tv_detail);
        this.mTvAppend = (TextView) findViewById(R.id.tv_append);
        this.mTvCategory = (TextView) findViewById(R.id.tv_category);
        this.mTvInformation= (TextView) findViewById(R.id.tv_information);
        this.mTvGraph= (TextView) findViewById(R.id.tv_graph);
    }

    private void InitEvent()
    {
        // 设置按钮监听
        mLlDetail.setOnClickListener(this);
        mLlAppend.setOnClickListener(this);
        mLlCategory.setOnClickListener(this);
        mLlInformation.setOnClickListener(this);
        mLlGraph.setOnClickListener(this);
    }

    private void InitFragment(int index)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragment(transaction);

        switch (index) {
            case 0:
                if (mDetailsFragment == null) {
                    mDetailsFragment = new DetailsFragment();
                    transaction.replace(R.id.main_content,  mDetailsFragment);
                } else {
                    transaction.show( mDetailsFragment);
                }
                break;
            case 1:

                Intent i = new Intent(this,AppendActivity.class);
                startActivity(i);
                break;
            case 2:
                if(mGraphFragment == null){
                    mGraphFragment = new GraphFragment();
                    transaction.replace(R.id.main_content, mGraphFragment);
                }else {
                    transaction.show(mGraphFragment);
                }
                break;
            case 3:
                if(mCategoryFragment == null){
                    mCategoryFragment = new CategoryFragment();
                    transaction.replace(R.id.main_content, mCategoryFragment);
                }else {
                    transaction.show(mCategoryFragment);
                }
                break;
            case 4:
//                if(mDetailsFragment == null){
//                    mDetailsFragment= new DetailsFragment();
//                    transaction.replace(R.id.main_content,mDetailsFragment);
//                }else {
//                    transaction.show(mDetailsFragment);
//                }
                break;
            default:
                break;
        }
        transaction.commit();
    }
//    private void hideFragment(FragmentTransaction transaction) {
//        if (mDetailFragment != null) {
//            transaction.hide(mDetailFragment);
//        }
//        if (mAppendFragment != null) {
//            transaction.hide(mAppendFragment);
//        }
//    }
    private void ClearFragment()
    {
        mDetailFragment = null;
        mGraphFragment = null;
        mCategoryFragment = null;
        mDetailsFragment= null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_detail:
                restartText();
                restartButton();
                ClearFragment();
                mIvDetail.setImageResource(R.drawable.ic_detail_1);
                mTvDetail.setTextColor(getResources().getColor(R.color.button_blue));
                InitFragment(0);
                break;
            case R.id.ll_append:
         //    ClearFragment();
         //    mIvAppend.setImageResource(R.drawable.ic_append_1);
         //    mTvAppend.setTextColor(getResources().getColor(R.color.button_blue));
                InitFragment(1);
                break;
            case R.id.ll_graph:
                restartText();
                restartButton();
                ClearFragment();
                mIvGraph.setImageResource(R.drawable.ic_graph_1);
                mTvGraph.setTextColor(getResources().getColor(R.color.button_blue));
                InitFragment(2);
                break;
            case R.id.ll_category:
                restartText();
                restartButton();
                ClearFragment();
                mIvCategory.setImageResource(R.drawable.ic_category_1);
                mTvCategory.setTextColor(getResources().getColor(R.color.button_blue));
                InitFragment(3);
                break;
            case R.id.ll_information:
                restartText();
                restartButton();
                ClearFragment();
                mIvInformation.setImageResource(R.drawable.ic_information_1);
                mTvInformation.setTextColor(getResources().getColor(R.color.button_blue));
                InitFragment(4);
                break;
            default:
                break;
        }

    }
    private void restartText(){
        mTvDetail.setTextColor(getResources().getColor(R.color.black));
        mTvGraph.setTextColor(getResources().getColor(R.color.black));
        mTvCategory.setTextColor(getResources().getColor(R.color.black));
        mTvAppend.setTextColor(getResources().getColor(R.color.black));
        mTvInformation.setTextColor(getResources().getColor(R.color.black));
    }

    private void restartButton() {
        // ImageView置为灰色
        mIvDetail.setImageResource(R.drawable.ic_detail);
        mIvAppend.setImageResource(R.drawable.ic_append);
        mIvCategory.setImageResource(R.drawable.ic_category);
        mIvGraph.setImageResource(R.drawable.ic_graph);
        mIvInformation.setImageResource(R.drawable.ic_information);
    }

}
