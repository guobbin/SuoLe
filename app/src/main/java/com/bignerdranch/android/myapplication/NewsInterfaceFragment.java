package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.DefaultTransformer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.support.v7.recyclerview.R.attr.layoutManager;
import static cn.bmob.v3.Bmob.getApplicationContext;


/**
 * Created by T540p on 2017/6/7.
 */

public class NewsInterfaceFragment extends Fragment {
    private List<News> newsList=new ArrayList<>();
    private NewsAdapter adapter;
    private Handler handler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String[] photoList=new String[7];
    private RecyclerView mRecyclerView;
    private GridLayoutManager mlayoutManager;
    private boolean isLoading=false;
    private int lastVisibleItemPosition;
    private int loadtimes=1;
//    private Banner banner;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.news_interface,container,false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh) ;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView=(RecyclerView)v.findViewById(R.id.news_lv);

        getPhoto();

        mlayoutManager=new GridLayoutManager(getContext(),1);
        mRecyclerView.setLayoutManager(mlayoutManager);
        adapter=new NewsAdapter(getContext(),newsList);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new MyItemClickListener() {
                                         @Override
                                         public void onItemClick(View view, int position) {
                                             News news=newsList.get(position);
                                             Intent intent=new Intent(getActivity(),NewsDisplayActivity.class);
                                             intent.putExtra("news_url",news.getNewsUrl());
                                             startActivity(intent);
                                         }
                                     }
        );

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    adapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    adapter.notifyItemChanged(adapter.getItemCount());
                    isLoading=false;
                }
            }
        };



        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNewsList();
                //刷新
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);
                lastVisibleItemPosition = mlayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    Log.d("test", "loading executed");

                    if (!isLoading) {
                        isLoading = true;
                        uploadNewsList();
                        //加载更多
                    }
                }
            }
        });


        return v;
    }

    private void getPhoto(){
        BmobQuery<SlideView> query=new BmobQuery<>();
        query.order("+createdAt");
        query.setLimit(7);
        query.findObjects(new FindListener<SlideView>() {
                              @Override
                              public void done(List<SlideView> list, BmobException e) {
                                  if(e==null){
                                      int i=0;
                                      for(SlideView slideView:list){
                                          photoList[i]=slideView.getPhoto().getUrl();
                                          i++;
                                      }
                                  }else{
                                      e.printStackTrace();
                                  }
                                  getNews();
                              }
                          }
        );
    }

    private void getNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String web="http://news.scu.edu.cn/news2012/cdzx/I0201index_"+loadtimes+".htm";
                    Document doc=Jsoup.connect(web).get();
                    Elements trs=doc.getElementById("__01").select("tr");
                    Document tt=Jsoup.parse(trs.toString());
                    Elements ttt=tt.getElementsByClass("pcenter_t19");
                    Document td=Jsoup.parse(ttt.toString());
                    Elements tdd=td.select("table[width=700]");

                    for(int i=0;i<tdd.size();i++) {
                            String title=tdd.get(i).select("a").text();
                            String uri1=tdd.get(i).select("a").attr("href");
                            String uri="http://news.scu.edu.cn"+uri1;
                            String time=tdd.get(i).select("div").text();
                        if(photoList[i%7]!=null){
                            News news=new News(title,uri,time,photoList[i%7]);
                            newsList.add(news);
                        }
                    }

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void refreshNewsList(){
        loadtimes=1;
        getNews();
    }

    public void uploadNewsList(){
        loadtimes++;
        getNews();
    }
}