package com.bignerdranch.android.myapplication.Account;

import com.bignerdranch.android.myapplication.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Detail> mDetailList;
    private double dayInTotal=0;
    private double dayOutTotal=0;
    private int month,year,day,week;
    private String sMonth,sYear,sDay,sWeek;
    public static enum ITEM_TYPE {
        ITEM_TYPE_DETAIL,
        ITEM_TYPE_DAY
    }

    //新建两个内部接口
    public interface OnItemClickListener{void onItemClick(View view, int position);}
    public interface OnItemLongClickListener{void onItemLongClick(View view, int position);}
    public DetailAdapter(List<Detail> detailList){
        mDetailList = detailList;
    }

    class DetailViewHolder extends RecyclerView.ViewHolder{
        ImageView mDetailImageView;
        TextView mDetailTextView1;
        TextView mDetailTextView2;
        public DetailViewHolder(View view){
            super(view);
            mDetailTextView1 = (TextView)view.findViewById(R.id.detail_item_text_view_1);
            mDetailTextView2 = (TextView)view.findViewById(R.id.detail_item_text_view_2);
            mDetailImageView = (ImageView)view.findViewById(R.id.detail_item_image_view);
            mDetailTextView1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Detail detail = mDetailList.get(position);
                    final EditText et = new EditText(v.getContext());
                    new AlertDialog.Builder(v.getContext()).setTitle(detail.getTitle()).
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String s = et.getText().toString();
                                    if (s != null) {
                                        detail.setTitle(s);
                                        detail.save();
                                        mDetailTextView1.setText(s);
                                    }
                                }
                            }).setNegativeButton("取消", null).setView(et).show();
                }
            });
            mDetailTextView2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Detail detail = mDetailList.get(position);
                    final EditText et = new EditText(v.getContext());
                    et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    new AlertDialog.Builder(v.getContext()).setTitle("¥").setView(et).
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String s = et.getText().toString();
                                    if (s != null) {
                                        detail.setFee(Double.parseDouble(s));
                                        detail.save();
                                        if(detail.isIncome())
                                        mDetailTextView2.setText("" + s);
                                        else mDetailTextView2.setText("-" + s);
                                    }
                                }
                            }).setNegativeButton("取消", null).show();
                }
            });
        }
    }

    class TimeViewHolder extends RecyclerView.ViewHolder{
        TextView mDetailDay;
        TextView mDetailDayTotal;
        public TimeViewHolder(View view){
            super(view);
            mDetailDay = (TextView)view.findViewById(R.id.detail_day);
            mDetailDayTotal = (TextView)view.findViewById(R.id.detail_day_total);
            InitData();
            mDetailDay.setText(sYear+sMonth+sDay+" "+sWeek);
            mDetailDayTotal.setText("收入:"+dayInTotal+"  支出:"+dayOutTotal);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
//        if(viewType==ITEM_TYPE.ITEM_TYPE_DETAIL.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item,parent,false);
            return new DetailViewHolder(view);
//        }
//        else  {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_day,parent,false);
//            return new TimeViewHolder(view);
//        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      if(holder instanceof DetailViewHolder) {
      Detail detail = mDetailList.get(position);
      ((DetailViewHolder)holder).mDetailTextView1.setText(detail.getTitle());
      switch (detail.getTitle()) {
        case "餐饮":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.dinner);
            break;
        case "购物":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.shopping);
            break;
        case "日用":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.daily_use);
            break;
        case "交通":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.traffic);
            break;
        case "蔬菜":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.vegetable);
            break;
        case "水果":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.fruit);
            break;
        case "零食":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.snacks);
            break;
        case "运动":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.sport);
            break;
        case "娱乐":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.amusement);
            break;
        case "通讯":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.communication);
            break;
        case "服饰":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.costume);
            break;
        case "美容":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.beauty);
            break;
        case "住房":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.house);
            break;
        case "居家":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.living);
            break;
        case "孩子":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.child);
            break;
        case "长辈":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.elder);
            break;
        case "社交":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.social);
            break;
        case "旅行":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.traval);
            break;
        case "烟酒":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.cig_wine);
            break;
        case "数码":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.digital);
            break;
        case "汽车":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.car);
            break;
        case "医疗":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.medical);
            break;
        case "书籍":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.books);
            break;
        case "学习":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.study);
            break;
        case "宠物":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.pet);
            break;
        case "礼金":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.cash_gift);
            break;
        case "礼物":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.gift);
            break;
        case "办公":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.work);
            break;
        case "维修":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.maintain);
            break;
        case "捐赠":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.donate);
            break;
        case "彩票":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.lottery);
            break;
        case "工资":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.salary);
            break;
        case "兼职":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.parttime_job);
            break;
        case "理财":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.manage_money);
            break;
        case "其他":
            ((DetailViewHolder)holder).mDetailImageView.setImageResource(R.drawable.others);
            break;
        default:
            break;
    }
     if (detail.isIncome()) {
        ((DetailViewHolder)holder).mDetailTextView2.setText("" + detail.getFee());
     } else {
        ((DetailViewHolder)holder).mDetailTextView2.setText("-" + detail.getFee());
     }
     }
    }
//    @Override
//    public int getItemViewType(int position) {
//        if(position==0) {
//            return ITEM_TYPE.ITEM_TYPE_DAY.ordinal();
//        }
//
//        else {
//            int prevIndex = position - 1;
//            boolean isDifferent = !(mDetailList.get(prevIndex).getDay() == mDetailList.get(position).getDay());
//            return isDifferent ? ITEM_TYPE.ITEM_TYPE_DAY.ordinal() : ITEM_TYPE.ITEM_TYPE_DETAIL.ordinal();
//        }
//    }
    @Override
    public int getItemCount() {
        return mDetailList.size();
    }

    public void InitData(){
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        sYear = year+"年";
        month = c.get(Calendar.MONTH)+1;
        sMonth = month+"月";
        day = c.get(Calendar.DAY_OF_MONTH);
        sDay = day+"日";
        week = c.get(Calendar.DAY_OF_WEEK);
        for(Detail detail:mDetailList){
            if(detail.getDay()== day&&detail.getMonth()==month&&detail.getYear()==year) {
                if(detail.isIncome()){
                    dayInTotal+=detail.getFee();
                }else {
                    dayOutTotal+=detail.getFee();
                }
            }
            switch (week)
            {
                case 1:
                    sWeek = "周日";
                    break;
                case 2:
                    sWeek = "周一";
                    break;
                case 3:
                    sWeek = "周二";
                    break;
                case 4:
                    sWeek = "周三";
                    break;
                case 5:
                    sWeek = "周四";
                    break;
                case 6:
                    sWeek = "周五";
                    break;
                case 7:
                    sWeek = "周六";
                    break;
                default:break;
            }
        }
    }

}
