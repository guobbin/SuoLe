package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by T540p on 2017/7/11.
 */

public class MingleFragment extends Fragment{

    private List<mingle_social> mingleList=new ArrayList<>();
    private List<mingle_social> mNewmingle=new ArrayList<>();
    private MingleAdapter adapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private Handler handler;
    private AppBarLayout mAppBarLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFlat;
    private boolean isLoading=false;
    private int lastVisibleItemPosition;
    private String bottomTime;
    private String topTime;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.mingle_interface,container,false);

        Bmob.initialize(getActivity(),"15901fbabf4589c6bf0c5d7d4a75de92");

        mFlat=(FloatingActionButton)v.findViewById(R.id.floating);
        mAppBarLayout=(AppBarLayout)getActivity().findViewById(R.id.appbar_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh) ;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView=(RecyclerView)v.findViewById(R.id.recycler_view);

        getMingleList();

        layoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new MingleAdapter(getContext(),mingleList);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new MyItemClickListener() {
                                         @Override
                                         public void onItemClick(View view, int postion) {
                                             mingle_social m=mingleList.get(postion);
                                             Intent intent=new Intent(getActivity(),CommentActivity.class);
                                             intent.putExtra("mingleId",m.getMingleId());
                                             startActivity(intent);

                                         }
                                     }
        );

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        for(mingle_social mingleSocial:(List<mingle_social>)msg.obj){
                            mingle_social mingleSocial1=new mingle_social(mingleSocial.getObjectId(),mingleSocial.getWriter(),mingleSocial.getText(),null,mingleSocial.getWriterPersonalIcon(),mingleSocial.getCreatedAt());
                            mingleList.add(mingleSocial1);
                        }

                        break;
                    case 1:
                        mNewmingle.clear();
                        for(mingle_social mingleSocial:(List<mingle_social>)msg.obj){
                            mingle_social mingleSocial1=new mingle_social(mingleSocial.getObjectId(),mingleSocial.getWriter(),mingleSocial.getText(),null,mingleSocial.getWriterPersonalIcon(),mingleSocial.getCreatedAt());
                            mNewmingle.add(mingleSocial1);
                        }
                        for(int n=0;n<mingleList.size();n++){
                            mNewmingle.add(mingleList.get(n));
                        }
                        mingleList.clear();
                        for(int n=0;n<mNewmingle.size();n++){
                            mingleList.add(mNewmingle.get(n));
                        }

                        break;
                    case 2:
                        for(mingle_social mingleSocial:(List<mingle_social>)msg.obj){
                            mingle_social mingleSocial1=new mingle_social(mingleSocial.getObjectId(),mingleSocial.getWriter(),mingleSocial.getText(),null,mingleSocial.getWriterPersonalIcon(),mingleSocial.getCreatedAt());
                            mingleList.add(mingleSocial1);
                        }
                        isLoading=false;

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
                refreshMingleList();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    Log.d("test", "loading executed");

                    if (!isLoading) {
                        isLoading = true;
                        uploadMingleList();

                    }
                }
            }
        });

        mFlat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getActivity(),WriteTextActivity.class);
                startActivityForResult(intent,6);
            }
        });

//        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//                if (verticalOffset >= 0 && !isLoading) {
//                    mSwipeRefreshLayout.setEnabled(true);
//                }
// else {
//                    mSwipeRefreshLayout.setEnabled(false);
//                }
//            }
//        });

        return v;
    }

    public void getMingleList(){


        BmobQuery<mingle_social> query = new BmobQuery<mingle_social>();
        query.order("-createdAt");
        query.setLimit(20);
        query.findObjects( new FindListener<mingle_social>() {
            @Override
            public void done(List<mingle_social> object, BmobException e) {
                if(e==null){
                    Message message=handler.obtainMessage();
                    message.what=0;
                    message.obj=object;
                    handler.sendMessage(message);
                    bottomTime=object.get(object.size()-1).getCreatedAt();
                    topTime=object.get(0).getCreatedAt();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }

    public void refreshMingleList(){
        BmobQuery<mingle_social> query = new BmobQuery<mingle_social>();
        Date date=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            date=sdf.parse(topTime);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND,1);
        date=cal.getTime();
        query.order("-createdAt");
        query.setLimit(20);
        query.addWhereGreaterThan("createdAt",new BmobDate(date));
        query.findObjects( new FindListener<mingle_social>() {
            @Override
            public void done(List<mingle_social> object, BmobException e) {
                if(e==null){
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj=object;
                    handler.sendMessage(message);
                    topTime=object.get(0).getCreatedAt();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

    public void uploadMingleList(){

        BmobQuery<mingle_social> query = new BmobQuery<mingle_social>();
        Date date=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            date=sdf.parse(bottomTime);
        }catch (ParseException e){
            e.printStackTrace();
        }
        query.order("-createdAt");
        query.setLimit(20);
        query.addWhereLessThan("createdAt",new BmobDate(date));
        query.findObjects( new FindListener<mingle_social>() {
            @Override
            public void done(List<mingle_social> object, BmobException e) {
                if(e==null){
                    Message message=handler.obtainMessage();
                    message.what=2;
                    message.obj=object;
                    handler.sendMessage(message);
                    bottomTime=object.get(object.size()-1).getCreatedAt();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

}



