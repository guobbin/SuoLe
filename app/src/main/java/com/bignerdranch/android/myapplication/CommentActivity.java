package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by T540p on 2017/11/7.
 */

public class CommentActivity extends AppCompatActivity{
    private List<Comment> mCommentList=new ArrayList<>();
    private List<Comment> mNew=new ArrayList<>();
    private CommentAdapter adapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private Handler handler;
    private AppBarLayout mAppBarLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CircleImageView mPhoto;
    private TextView mWriter;
    private TextView mText;
    private TextView mTime;
    private FloatingActionButton mFlat;
    private String mingleNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comment_interface);

        ExitApplication.getInstance().addActivity(this);

        Bmob.initialize(this, "15901fbabf4589c6bf0c5d7d4a75de92");

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility( View.SYSUI);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }
        mFlat=(FloatingActionButton)findViewById(R.id.floating);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.appbar_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh) ;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView=(RecyclerView)findViewById(R.id.comment_view);
        mPhoto=(CircleImageView)findViewById(R.id.iv_personal_icon_mingle_comment);
        mWriter=(TextView)findViewById(R.id.tv_petName_mingle_comment);
        mText=(TextView)findViewById(R.id.tv_text_mingle_comment);
        mTime=(TextView)findViewById(R.id.tv_time_mingle_comment);

        mingleNo=getIntent().getStringExtra("mingleId");

        mSwipeRefreshLayout.setRefreshing(true);
        getMainContent();
        getComment();

        layoutManager=new LinearLayoutManager(CommentActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new CommentAdapter(CommentActivity.this,mCommentList);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new MyItemClickListener() {
                                         @Override
                                         public void onItemClick(View view, int position) {
                                             Comment comment=mCommentList.get(position);
                                             Intent intent=new Intent(CommentActivity.this,WriteCommentActivity.class);
                                             intent.putExtra("mingleId",mingleNo);
                                             intent.putExtra("answerTo",comment.getObserver());
                                             startActivity(intent);
                                         }
                                     }
        );

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        for(Comment comment:(List<Comment>)msg.obj){
                            Comment comment1=new Comment(mingleNo,comment.getObserver(),comment.getObPhoto(),comment.getContent(),comment.getAuthor(),comment.getCreatedAt());
                            mCommentList.add(comment1);
                        }

                        break;
                    case 1:
                        mNew.clear();
                        for(Comment comment:(List<Comment>)msg.obj){
                            Comment comment1=new Comment(mingleNo,comment.getObserver(),comment.getObPhoto(),comment.getContent(),comment.getAuthor(),comment.getCreatedAt());
                            mNew.add(comment1);
                        }
                        mCommentList.clear();
                        for(int n=0;n<mCommentList.size();n++){
                            mNew.add(mCommentList.get(n));
                        }
                        for(int n=0;n<mNew.size();n++){
                            mCommentList.add(mNew.get(n));
                        }

                        break;
                }
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                adapter.notifyItemChanged(adapter.getItemCount());
            }
        };

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshComment();
            }
        });

        mFlat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(CommentActivity.this,WriteCommentActivity.class);
                intent.putExtra("mingleId",mingleNo);
                startActivity(intent);
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    public void getMainContent(){
        BmobQuery<mingle_social> query=new BmobQuery<>();
        query.getObject(mingleNo, new QueryListener<mingle_social>() {
            @Override
            public void done(mingle_social mingle_social, BmobException e) {
                if(e==null){
                    mText.setText(mingle_social.getText());
                    mWriter.setText(mingle_social.getWriter());
                    mTime.setText(mingle_social.getCreatedAt());
                    Glide.with(CommentActivity.this).load(mingle_social.getWriterPersonalIcon()).error(R.mipmap.default_personal_image).into(mPhoto);
                }else{
                    e.printStackTrace();
                }
            }
        });
    }

    public void getComment(){
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.order("-createdAt");
        query.setLimit(1000);
        query.addWhereEqualTo("MingleNo",mingleNo);
        query.findObjects( new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if(e==null){
                    Message message=handler.obtainMessage();
                    message.what=0;
                    message.obj=object;
                    handler.sendMessage(message);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

    public void refreshComment(){
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.order("-createdAt");
        query.setLimit(1000);
        query.addWhereEqualTo("MingleNo",mingleNo);
        query.findObjects( new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if(e==null){
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj=object;
                    handler.sendMessage(message);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
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

