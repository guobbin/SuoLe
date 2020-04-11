package com.bignerdranch.android.myapplication.WAPfunctions;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.myapplication.R;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class ChoosePlanTypeFragment extends Fragment {
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plan_type, container, false);
        button1= (Button) view.findViewById(R.id.day_plan);
        button2= (Button) view.findViewById(R.id.week_plan);
        button3= (Button) view.findViewById(R.id.month_plan);
        button4= (Button) view.findViewById(R.id.year_plan);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("日计划");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("周计划");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("月计划");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click("年计划");
            }
        });
    }

    private void click(String plan) {
        PlanActivity activity = (PlanActivity) getActivity();
        activity.drawerLayout.closeDrawers();
//        activity.swipeRefresh.setRefreshing(true);
        activity.showPlansInfo(plan);
        setSelectedButtonColor(plan);
    }

    private void setSelectedButtonColor(String plan) {
        button1.setBackgroundColor(Color.rgb(255,255,255));
        button2.setBackgroundColor(Color.rgb(255,255,255));
        button3.setBackgroundColor(Color.rgb(255,255,255));
        button4.setBackgroundColor(Color.rgb(255,255,255));
        if(plan.equals("日计划")){
            button1.setBackgroundColor(Color.rgb(221,221,221));
        }else if(plan.equals("周计划")){
            button2.setBackgroundColor(Color.rgb(221,221,221));
        }else if(plan.equals("月计划")){
            button3.setBackgroundColor(Color.rgb(221,221,221));
        }else if(plan.equals("年计划")){
            button4.setBackgroundColor(Color.rgb(221,221,221));
        }
    }
}
