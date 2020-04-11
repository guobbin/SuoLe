package com.bignerdranch.android.myapplication.Account;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import com.bignerdranch.android.myapplication.R;
/**
 * Created by Administrator on 2017/9/14/014.
 */

public class GraphFragment extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener, View.OnClickListener{
    private int year,month,day,week,WeekOfMonth,numOfMonth;
    private double[] thisWeekData,thisMonthData,thisYearData;
    private LineChart mLineChar;
    private LineDataSet set1;
    private TextView mTextViewWeek;
    private TextView mTextViewMonth;
    private TextView mTextViewYear;
    private XAxis xAxis ;
    private ImageView backButton;
    private List<Detail> detailList = new ArrayList<>();
    @Override
    public void onCreate(Bundle saveInstanceState){super.onCreate(saveInstanceState);}
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_graph,container,false);
        mTextViewWeek =(TextView) v.findViewById(R.id.week_graph);
        mTextViewMonth =(TextView) v.findViewById(R.id.month_graph);
        mTextViewYear =(TextView) v.findViewById(R.id.year_graph);
        mTextViewWeek.setOnClickListener(this);
        mTextViewMonth.setOnClickListener(this);
        mTextViewYear.setOnClickListener(this);
        mTextViewWeek.setTextColor(getResources().getColor(R.color.boder_blue));
        mTextViewWeek.setBackground(getResources().getDrawable(R.drawable.textview_border2));
        backButton = (ImageView) v.findViewById(R.id.graph_backTo_main);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        initView(v);
        return v;
    }
    private int getNumOfMonth()
    {
        int numOfMonth0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numOfMonth = 30;
                break;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    numOfMonth = 29;
                } else {
                    numOfMonth = 28;
                }
                break;
        }
        numOfMonth0 = numOfMonth;
        return numOfMonth0;
    }
    private void initDate() {
        thisWeekData = new double[7];
        thisMonthData = new double[31];
        thisYearData = new double[12];
        date();
        detailList = DataSupport.findAll(Detail.class);
        Collections.reverse(detailList);
        for(Detail detail:detailList){
            if(detail.getYear()==year&&detail.getMonth()==month){
                if(detail.isIncome()){
                }
                else {
                    getNumOfMonth();
                    for (int i = 0; i < numOfMonth; i++) {
                        if (day - detail.getDay() == day - i - 1) {
                            thisMonthData[i] += detail.getFee();
                        }
                    }
                }
            }
            if(detail.getYear()==year&&detail.getMonth()==month&&detail.getWeekOfMonth()==WeekOfMonth) {
                if (detail.isIncome()) {
                } else {
                    for(int i = 0; i<7;i++){
                        if(week-detail.getWeek()==week-i-1)
                        {
                            thisWeekData[i]+=detail.getFee();
                        }
                    }
                }
            }
            if(detail.getYear()==year){
                if(detail.isIncome()){
                }else {
                    for(int i = 0; i<12;i++){
                        if(month-detail.getMonth()==month-i-1){
                            thisYearData[i]+=detail.getFee();
                        }
                    }
                }

            }
        }
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
    //传递数据集
    private void setData(ArrayList<Entry> values) {
        if (mLineChar.getData() != null && mLineChar.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mLineChar.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLineChar.getData().notifyDataChanged();
            mLineChar.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values, "消费报告");
            // 在这里设置线
            //   set1.enableDashedLine(0f, 0f, 0f);  //设置折线的虚实
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.rgb(0X63,0XB8,0XFF));
            set1.setCircleColor(Color.rgb(0X63,0XB8,0XFF));
            set1.setLineWidth(1f);
            set1.setCircleRadius(4f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false); //设置折线下方是否填充
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            // 设置平滑曲线
            set1.setMode(LineDataSet.Mode.LINEAR);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            //添加数据集
            dataSets.add(set1);
            //创建一个数据集的数据对象
            LineData data = new LineData(dataSets);
            //谁知数据
            mLineChar.setData(data);
            // mLineChar.invalidate();//刷新
        }
    }
    //初始化View
    private void initView(View v) {
        mLineChar = (LineChart) v.findViewById(R.id.mLineChar);
        //设置手势滑动事件
        mLineChar.setOnChartGestureListener(this);
        //设置数值选择监听
        mLineChar.setOnChartValueSelectedListener(this);
        //后台绘制
        mLineChar.setDrawGridBackground(false);
        //设置描述文本
        mLineChar.getDescription().setEnabled(false);
        //设置支持触控手势
        mLineChar.setTouchEnabled(true);
        //设置缩放
        mLineChar.setDragEnabled(true);
        //设置推动
        mLineChar.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        mLineChar.setPinchZoom(true);
        //x轴
        LimitLine llXAxis = new LimitLine(10f, "标记");//设置线宽
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        xAxis = mLineChar.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);//则下面绘制的Grid不会有 "竖的线(与X轴有关)"
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new MyXValueFormatterWeek(true));

        YAxis leftAxis = mLineChar.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMinimum(0f);   //设置Y轴最小值
        leftAxis.setDrawZeroLine(false);// 限制数据(而不是背后的线条勾勒出了上面)
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setValueFormatter(new MyYValueFormatter(true));

        mLineChar.getAxisRight().setEnabled(false);
        //默认一周七个数据
        initDate();
        ArrayList<Entry> values = new ArrayList<Entry>();
        for(int i = 1;i < 8;i++)
        {
            values.add(new Entry(i, (int)thisWeekData[i-1]));
        }
        //设置数据
        setData(values);
        mLineChar.animateX(2500);//默认动画
        //刷新
        //mChart.invalidate();
        // 得到这个文字
        Legend l = mLineChar.getLegend();
        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);
    }
    class MyXValueFormatterWeek implements IAxisValueFormatter{
        private boolean drawValue;
        public MyXValueFormatterWeek(boolean drawValue){
            this.drawValue = drawValue;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (drawValue){
                if (value==1){
                    return "星期天";
                }else if (value==2){
                    return "星期一";
                }else if (value==3){
                    return "星期二";
                }else if (value==4){
                    return "星期三";
                }else if (value==5){
                    return "星期四";
                }else if (value==6){
                    return "星期五";
                }
                else {
                    return "星期六";
                }
            }else {
                return "-";
            }
        }
        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
    class MyXValueFormatterMonth implements IAxisValueFormatter{
        private boolean drawValue;
        public MyXValueFormatterMonth(boolean drawValue){
            this.drawValue = drawValue;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (drawValue){
                if (value>=0){
                    return (int)value+"";
                }
                else {
                    return "-";
                }
            }else {
                return "-";
            }
        }
        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
    class MyXValueFormatterYear  implements IAxisValueFormatter{
        private boolean drawValue;
        public MyXValueFormatterYear(boolean drawValue){
            this.drawValue = drawValue;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (drawValue){
                if (value>=0){
                    return (int)value+"月";
                }
                else {
                    return "-";
                }
            }else {
                return "-";
            }
        }
        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
    class MyYValueFormatter implements IAxisValueFormatter{
        private boolean drawValue;
        public MyYValueFormatter(boolean drawValue){
            this.drawValue = drawValue;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (drawValue){
                if (value>=0){
                    return value+"元";
                }
                else {
                    return "-";
                }
            }else {
                return "-";
            }
        }
        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.week_graph:
                restartTextView();
                mTextViewWeek.setTextColor(getResources().getColor(R.color.boder_blue));
                mTextViewWeek.setBackground(getResources().getDrawable(R.drawable.textview_border2));
                xAxis.setValueFormatter(new MyXValueFormatterWeek(true));
                //这里添加数据
                initDate();
                ArrayList<Entry> values = new ArrayList<Entry>();
                for(int i = 1;i < 8;i++)
                {
                    values.add(new Entry(i, (int)thisWeekData[i-1]));
                }
                //设置数据
                setData(values);
                mLineChar.animateX(2500);
                break;
            case R.id.month_graph:
                restartTextView();
                mTextViewMonth.setTextColor(getResources().getColor(R.color.boder_blue));
                mTextViewMonth.setBackground(getResources().getDrawable(R.drawable.textview_border2));
                xAxis.setValueFormatter(new MyXValueFormatterMonth(true));
                //这里添加数据
                initDate();
                ArrayList<Entry> values1 = new ArrayList<Entry>();
                for(int i = 1;i<getNumOfMonth()+1;i++)
                {
                    values1.add(new Entry(i,(int)thisMonthData[i-1]));
                }
                //设置数据
                setData(values1);
                mLineChar.animateX(2500);
                break;
            case R.id.year_graph:
                restartTextView();
                mTextViewYear.setTextColor(getResources().getColor(R.color.boder_blue));
                mTextViewYear.setBackground(getResources().getDrawable(R.drawable.textview_border2));
                xAxis.setValueFormatter(new MyXValueFormatterYear(true));
                //这里添加数据
                initDate();
                ArrayList<Entry> values0 = new ArrayList<Entry>();
               for(int i=1;i<13;i++){
                   values0.add(new Entry(i,(int)thisYearData[i-1]));
               }
                //设置数据
                setData(values0);
                mLineChar.animateX(2500);
                break;
        }
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
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        // 完成之后停止晃动
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mLineChar.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {}

    @Override
    public void onChartDoubleTapped(MotionEvent me) {}

    @Override
    public void onChartSingleTapped(MotionEvent me) {}

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {}

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {}

    @Override
    public void onValueSelected(Entry e, Highlight h) {}

    @Override
    public void onNothingSelected() {}
    @Override
    public void onResume() {
        mLineChar.invalidate();
        super.onResume();
    }

}

