package com.bignerdranch.android.myapplication.Account;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import com.bignerdranch.android.myapplication.R;
/**
 * Created by Administrator on 2017/9/14/014.
 */

public class DetailFragment extends Fragment {
    private static String NESTED_FRAGMENT_TAG ="childfragment";
    private DatePickerDialog mMyDatePickerDialog;

    private TextView mDetailMonth;
    private TextView mDetailYear;
    private TextView mDetailExpand;
    private TextView mDetailIncome;
    private double monthOutTotal=0;
    private double monthInTotal=0;
    private double yearTotal;

    private int year,month,day,week;
    private String sMonth,sYear,sWeek;
    private RecyclerView recyclerView ;

    private List<Detail> detailList = new ArrayList<>();
    @Override
    public void onCreate(Bundle saveInstanceState){super.onCreate(saveInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_detail,container,false);

        recyclerView = (RecyclerView)v.findViewById(R.id.detail_recycler_list);
        mDetailYear = (TextView) v.findViewById(R.id.detail_year);
        mDetailMonth = (TextView) v.findViewById(R.id.detail_date_month);
        mDetailExpand = (TextView) v.findViewById(R.id.detail_expand_money) ;
        mDetailIncome = (TextView)v.findViewById(R.id.detail_income_money);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        initDetail();
        mDetailMonth.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mMyDatePickerDialog = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view ,int year,int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        mDetailYear.setText(year+"年");
                        mDetailMonth.setText(month+"月");
                    }
                }, year, month, day);
                mMyDatePickerDialog.show();
                ((ViewGroup)((ViewGroup) mMyDatePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            }
        });
        return v;
    }
    private void initDetail() {
        detailList = DataSupport.findAll(Detail.class);
        Collections.reverse(detailList);
        DetailAdapter adapter = new DetailAdapter(detailList);
        recyclerView.setAdapter(adapter);
        monthOutTotal = 0;
        monthInTotal = 0;
        for(Detail detail:detailList){
            if(detail.getYear()==year&&detail.getMonth()==month) {
                if (detail.isIncome()) {
                    monthInTotal += detail.getFee();
                } else {
                    monthOutTotal += detail.getFee();
                }
            }
        }
        date();
        mDetailYear.setText(sYear);
        mDetailMonth.setText(sMonth);
        mDetailExpand.setText(monthOutTotal+"");
        mDetailIncome.setText(monthInTotal+"");
    }
    //获取当前系统时间
    private void date() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        week = c.get(Calendar.DAY_OF_WEEK);
        sYear=year+"年";
        sMonth=month+"月";
    }
    @Override
    public void onResume() {
        initDetail();
        super.onResume();
    }
}