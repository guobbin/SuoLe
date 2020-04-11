package com.bignerdranch.android.myapplication.Account;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.myapplication.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14/014.
 */

public class CategoryFragment extends Fragment implements OnChartValueSelectedListener, View.OnClickListener {
    private int year,month,day,week,WeekOfMonth,numOfMonth;
    private double[] thisWeekData,thisMonthData,thisYearData;
    private PieChart mPieChart;
    private TextView mTextViewWeek;
    private TextView mTextViewMonth;
    private TextView mTextViewYear;
    private double Total,TotalWeek,TotalMonth,TotalYear;
    private ImageView backButton;
    private List<Detail> detailList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_category,container,false);
        mTextViewWeek =(TextView) v.findViewById(R.id.week_category);
        mTextViewMonth =(TextView) v.findViewById(R.id.month_category);
        mTextViewYear =(TextView) v.findViewById(R.id.year_category);
        mTextViewWeek.setOnClickListener(this);
        mTextViewMonth.setOnClickListener(this);
        mTextViewYear.setOnClickListener(this);
        mTextViewWeek.setTextColor(getResources().getColor(R.color.boder_blue));
        mTextViewWeek.setBackground(getResources().getDrawable(R.drawable.textview_border2));
        backButton = (ImageView) v.findViewById(R.id.category_backTo_main);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        initView(v);
        return v;
    }
    //初始化View
    private void initView(View v) {
        //饼状图
        mPieChart = (PieChart) v.findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText("总支出\n"+Total);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);
        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);
        mPieChart.setHoleRadius(55f);
        mPieChart.setTransparentCircleRadius(58f);
        mPieChart.setDrawCenterText(true);
        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);
        //变化监听
        mPieChart.setOnChartValueSelectedListener(this);

        //模拟数据
        initDate();
        mPieChart.setCenterText("总支出\n"+Total);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        String []s=new String[]{"通讯","购物","旅行","生活","其他"};
        for(int i=0;i<5;i++){
            if(thisWeekData[i]!=0)
            entries.add(new PieEntry((int)thisWeekData[i], s[i]));
        }
        //设置数据
        setData(entries);
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
    }

    //设置中间文字
//    private SpannableString generateCenterSpannableText() {
//        SpannableString s = new SpannableString("总支出\n"+Total);
////        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
////        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
////        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
////       s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
////        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
////        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
//        return s;
//    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "消费报告");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        for (IDataSet<?> set : mPieChart.getData().getDataSets())
              set.setDrawValues(true);
        //刷新
        mPieChart.invalidate();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void restartTextView() {

        // TextView Text置为白色
        mTextViewWeek.setTextColor(getResources().getColor(R.color.white));
        mTextViewWeek.setBackground(getResources().getDrawable(R.drawable.textview_border));
        mTextViewMonth.setTextColor(getResources().getColor(R.color.white));
        mTextViewMonth.setBackground(getResources().getDrawable(R.drawable.textview_border));
        mTextViewYear.setTextColor(getResources().getColor(R.color.white));
        mTextViewYear.setBackground(getResources().getDrawable(R.drawable.textview_border));
    }

    private void initDate() {
        thisWeekData = new double[5];
        thisMonthData = new double[5];
        thisYearData = new double[5];
        TotalMonth=0;
        TotalYear=0;
        TotalWeek=0;
        date();
        detailList = DataSupport.findAll(Detail.class);
        Collections.reverse(detailList);
        for(Detail detail:detailList){
            if(detail.getYear()==year&&detail.getMonth()==month) {
                if (detail.isIncome()) {
                } else {
                    TotalMonth += detail.getFee();
                    if(detail.getTitle().equals("通讯"))
                    {
                        thisMonthData[0]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("购物")||detail.getTitle().equals("日用")||detail.getTitle().equals("蔬菜")||detail.getTitle().equals("水果")||detail.getTitle().equals("零食")||detail.getTitle().equals("服饰")||detail.getTitle().equals("烟酒")||detail.getTitle().equals("数码")||detail.getTitle().equals("书籍")||detail.getTitle().equals("办公"))
                    {
                        thisMonthData[1]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("旅行")||detail.getTitle().equals("交通"))
                    {
                        thisMonthData[2]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("餐饮")||detail.getTitle().equals("运动")||detail.getTitle().equals("娱乐")||detail.getTitle().equals("住房")||detail.getTitle().equals("居家")||detail.getTitle().equals("社交"))
                    {
                        thisMonthData[3]+=detail.getFee();
                    }
                    else{
                        thisMonthData[4]+=detail.getFee();
                    }
                }
            }
            if(detail.getYear()==year&&detail.getMonth()==month&&detail.getWeekOfMonth()==WeekOfMonth) {
                if (detail.isIncome()) {
                }  else {
                    TotalWeek += detail.getFee();
                    if(detail.getTitle().equals("通讯"))
                    {
                        thisWeekData[0]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("购物")||detail.getTitle().equals("日用")||detail.getTitle().equals("蔬菜")||detail.getTitle().equals("水果")||detail.getTitle().equals("零食")||detail.getTitle().equals("服饰")||detail.getTitle().equals("烟酒")||detail.getTitle().equals("数码")||detail.getTitle().equals("书籍")||detail.getTitle().equals("办公"))
                    {
                        thisWeekData[1]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("旅行")||detail.getTitle().equals("交通"))
                    {
                        thisWeekData[2]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("餐饮")||detail.getTitle().equals("运动")||detail.getTitle().equals("娱乐")||detail.getTitle().equals("住房")||detail.getTitle().equals("居家")||detail.getTitle().equals("社交"))
                    {
                        thisWeekData[3]+=detail.getFee();
                    }
                    else{
                        thisWeekData[4]+=detail.getFee();
                    }
                }
            }
            if(detail.getYear()==year){
                if(detail.isIncome()){
                } else {
                    TotalYear += detail.getFee();
                    if(detail.getTitle()=="通讯")
                    {
                        thisYearData[0]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("购物")||detail.getTitle().equals("日用")||detail.getTitle().equals("蔬菜")||detail.getTitle().equals("水果")||detail.getTitle().equals("零食")||detail.getTitle().equals("服饰")||detail.getTitle().equals("烟酒")||detail.getTitle().equals("数码")||detail.getTitle().equals("书籍")||detail.getTitle().equals("办公"))
                    {
                        thisYearData[1]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("旅行")||detail.getTitle().equals("交通"))
                    {
                        thisYearData[2]+=detail.getFee();
                    }
                    else if(detail.getTitle().equals("餐饮")||detail.getTitle().equals("运动")||detail.getTitle().equals("娱乐")||detail.getTitle().equals("住房")||detail.getTitle().equals("居家")||detail.getTitle().equals("社交"))
                    {
                        thisYearData[3]+=detail.getFee();
                    }
                    else{
                        thisYearData[4]+=detail.getFee();
                    }
                }

            }
        }
        Total = TotalWeek;
    }
    //获取当前系统时间
    private void date() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        week = c.get(Calendar.DAY_OF_WEEK);
        WeekOfMonth = c.get(Calendar.WEEK_OF_MONTH);
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {}

    @Override
    public void onNothingSelected() {}

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.week_category:
                restartTextView();
                mTextViewWeek.setTextColor(getResources().getColor(R.color.boder_blue));
                mTextViewWeek.setBackground(getResources().getDrawable(R.drawable.textview_border2));
                //模拟数据
                initDate();
                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                String []s=new String[]{"通讯","购物","旅行","生活","其他"};
                for(int i=0;i<5;i++){
                    if(thisWeekData[i]!=0)
                        entries.add(new PieEntry((int)thisWeekData[i], s[i]));
                }
                Total = 0;
                Total=TotalWeek;
                mPieChart.setCenterText("总支出\n"+Total);
                //设置数据
                setData(entries);
                mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                break;
            case R.id.month_category:
                restartTextView();
                mTextViewMonth.setTextColor(getResources().getColor(R.color.boder_blue));
                mTextViewMonth.setBackground(getResources().getDrawable(R.drawable.textview_border2));
                //模拟数据
                initDate();
                ArrayList<PieEntry> entries1 = new ArrayList<PieEntry>();
                String []s1=new String[]{"通讯","购物","旅行","生活","其他"};
                for(int i=0;i<5;i++){
                    if(thisWeekData[i]!=0)
                        entries1.add(new PieEntry((int)thisWeekData[i], s1[i]));
                }
                Total = 0;
                Total = TotalMonth;
                mPieChart.setCenterText("总支出\n"+Total);
                //设置数据
                setData(entries1);
                mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                break;
            case R.id.year_category:
                restartTextView();
                mTextViewYear.setTextColor(getResources().getColor(R.color.boder_blue));
                mTextViewYear.setBackground(getResources().getDrawable(R.drawable.textview_border2));
                //模拟数据
                initDate();
                mPieChart.setCenterText("总支出\n"+Total);
                ArrayList<PieEntry> entries2 = new ArrayList<PieEntry>();
                String []s2=new String[]{"通讯","购物","旅行","生活","其他"};
                for(int i=0;i<5;i++){
                    if(thisWeekData[i]!=0)
                        entries2.add(new PieEntry((int)thisWeekData[i], s2[i]));
                }
                Total = 0;
                Total = TotalYear;
                mPieChart.setCenterText("总支出\n"+Total);
                //设置数据
                setData(entries2);
                mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                break;
        }
    }
    @Override
    public void onResume() {
        mPieChart.invalidate();
        super.onResume();
    }
}

