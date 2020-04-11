package com.bignerdranch.android.myapplication;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bignerdranch.android.myapplication.Account.*;
import com.bignerdranch.android.myapplication.WAPfunctions.PlanActivity;
import com.bignerdranch.android.myapplication.WAPfunctions.WeatherActivity;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.DefaultTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by T540p on 2017/7/11.
 */

public class FuncFragment extends Fragment implements OnBannerListener {
    private Button mLearning,mPlan,mWeather,mAccount;
    private Banner banner;
    private List titles=new ArrayList<String>();
    private List urls=new ArrayList<>();
    private List webs = new ArrayList<>();
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.func_interface,container,false);

        Bmob.initialize(getActivity(), "15901fbabf4589c6bf0c5d7d4a75de92");

//        mLearning=(Button)v.findViewById(R.id.learning_button);
        mPlan=(Button)v.findViewById(R.id.plan_button_func);
        mWeather=(Button)v.findViewById(R.id.whether_button_func);
        mAccount = (Button)v.findViewById(R.id.manageM_button);
        banner = (Banner) v.findViewById(R.id.banner);

        getUrl();
//        banner.setImages(urls)
//               .setBannerTitles(titles)
//                .setImageLoader(new MyLoader())
//                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//                .setBannerAnimation(DefaultTransformer.class)
//                .setIndicatorGravity(BannerConfig.RIGHT)
//                .setOnBannerListener(this)
//                .start();
        mAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.bignerdranch.android.myapplication.Account.MainActivity.class);
                getActivity().startActivityForResult(intent,7);
            }
        });

//        mLearning.setOnClickListener(new View.OnClickListener() {
//                                         @Override
//                                         public void onClick(View v) {
//                                             Intent intent=new Intent(getActivity(),CourseActivity.class);
//                                             getActivity().startActivityForResult(intent,7);
//                                         }
//                                     });

        mPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String planType = "日计划";
                Intent intent = new Intent(getActivity(), PlanActivity.class);
                intent.putExtra("planType",planType);
                getActivity().startActivityForResult(intent,7);
            }
        });

        mWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weatherId = "CN101270106";
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                if (prefs.getString("weather", null) == null) {
                    intent.putExtra("weather_id",weatherId);
                }
                getActivity().startActivityForResult(intent,7);
            }
        });
        return v;
    }

    public void OnBannerClick(int position) {
        Intent intent = new Intent(getApplicationContext(),NewsDisplayActivity.class );
        intent.putExtra("news_url",webs.get(position).toString());
        startActivity(intent);
    }
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    public void getUrl(){
        BmobQuery<SlideView> query=new BmobQuery<>();
        query.setLimit(3);
        query.order("-createdAt");
        query.findObjects(new FindListener<SlideView>() {
                              @Override
                              public void done(List<SlideView> list, BmobException e) {
                                  if(e==null){
                                      int i=0;
                                      titles.clear();
                                      urls.clear();
                                      webs.clear();
                                      for(SlideView slideView:list){
                                          titles.add(slideView.getDetail());
                                          urls.add(slideView.getPhoto().getUrl());
                                          webs.add(slideView.getWeb());
                                          i=i+1;
                                      }
                                      setBanner();
                                  }else{
                                      e.printStackTrace();
                                  }
                              }
                          }
        );
    }

    public void setBanner(){
        banner.setImages(urls)
                .setBannerTitles(titles)
                .setImageLoader(new MyLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setBannerAnimation(DefaultTransformer.class)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setOnBannerListener(this)
                .start();
    }

}
