
package com.bignerdranch.android.myapplication.Account;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bignerdranch.android.myapplication.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * SwipeMenuListView
 * Created by baoyz on 15/6/29.
 */
public class DetailsFragment extends Fragment {

    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private SwipeMenuItem openItem;
    private SwipeMenuItem deleteItem;

    private DatePickerDialog mMyDatePickerDialog;

    private TextView mDetailMonth;
    private TextView mDetailYear;
    private TextView mDetailExpand;
    private TextView mDetailIncome;
    private double monthOutTotal=0;
    private double monthInTotal=0;
    private double yearTotal;
    private ImageView backButton;
    private int year,month,day,week;
    private String sMonth,sYear,sWeek;

    private List<DetailForSwipeMenu> detailList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.details_fragment,container,false);

        mListView = (SwipeMenuListView) v.findViewById(R.id.detail_listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        mDetailYear = (TextView) v.findViewById(R.id.detail_year);
        mDetailMonth = (TextView) v.findViewById(R.id.detail_date_month);
        mDetailExpand = (TextView) v.findViewById(R.id.detail_expand_money);
        mDetailIncome = (TextView)v.findViewById(R.id.detail_income_money);
        backButton = (ImageView) v.findViewById(R.id.details_backTo_main);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        date();
        init(year,month,day);
        mDetailMonth.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mMyDatePickerDialog = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view , int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        init(year,month,dayOfMonth);
                        mDetailYear.setText(year+"年");
                        mDetailMonth.setText(month+"月"+dayOfMonth+"日");
                    }
                }, year, month, day);
                mMyDatePickerDialog.show();
                //((ViewGroup)((ViewGroup) mMyDatePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                DetailForSwipeMenu item = detailList.get(position);
                switch (index) {
                    case 0:
                        DataSupport.delete(Detail.class,item.getId());
                        List<Detail> details = DataSupport.findAll(Detail.class);
                        Collections.reverse(details);
                        monthOutTotal = 0;
                        monthInTotal = 0;
                        List<DetailForSwipeMenu> detailList1 = new ArrayList<>();
                        for(Detail detail: details) {
                            if (detail.getYear() == year && detail.getMonth() == month) {
                                detailList1.add(new DetailForSwipeMenu(detail.getId(), detail.getTitle(), detail.getFee(), detail.isIncome(), detail.getYear(), detail.getMonth(), detail.getDay(), detail.getWeek(), detail.getWeekOfMonth()));
                                if (detail.isIncome()) {
                                    monthInTotal += detail.getFee();
                                } else {
                                    monthOutTotal += detail.getFee();
                                }
                            }
                        }
                        mDetailExpand.setText(monthOutTotal+"");
                        mDetailIncome.setText(monthInTotal+"");
                        //                  init(year,month);
//                        DataSupport.deleteAll(Detail.class,"type=? and body=?",plansType,item.getBody());
                        detailList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        return v;

    }
    private void init(int year,int month,int day) {
        detailList.clear();
        List<Detail> details = DataSupport.findAll(Detail.class);
        Collections.reverse(details);
        monthOutTotal = 0;
        monthInTotal = 0;
        for(Detail detail: details) {
            if (detail.getYear() == year && detail.getMonth() == month&& detail.getDay() == day) {
                detailList.add(new DetailForSwipeMenu(detail.getId(), detail.getTitle(), detail.getFee(), detail.isIncome(), detail.getYear(), detail.getMonth(), detail.getDay(), detail.getWeek(), detail.getWeekOfMonth()));
                if (detail.isIncome()) {
                    monthInTotal += detail.getFee();
                } else {
                    monthOutTotal += detail.getFee();
                }
            }
        }
        mDetailYear.setText(sYear);
        mDetailMonth.setText(month+"月"+day+"日");
        mDetailExpand.setText(monthOutTotal+"");
        mDetailIncome.setText(monthInTotal+"");
        mAdapter.notifyDataSetChanged();
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return detailList.size();
        }

        @Override
        public DetailForSwipeMenu  getItem(int position) {
            return detailList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.detail_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            DetailForSwipeMenu  item = getItem(position);
            holder.mDetailTextView1.setText(item.getTitle());
            if(item.isIncome()) {
                holder.mDetailTextView2.setText(item.getFee() + "");
            }
            else {
                holder.mDetailTextView2.setText("-"+item.getFee());
            }
            switch (item.getTitle()) {
                case "餐饮":
                    holder.mDetailImageView.setImageResource(R.drawable.dinner);
                    break;
                case "购物":
                    holder.mDetailImageView.setImageResource(R.drawable.shopping);
                    break;
                case "日用":
                    holder.mDetailImageView.setImageResource(R.drawable.daily_use);
                    break;
                case "交通":
                    holder.mDetailImageView.setImageResource(R.drawable.traffic);
                    break;
                case "蔬菜":
                    holder.mDetailImageView.setImageResource(R.drawable.vegetable);
                    break;
                case "水果":
                    holder.mDetailImageView.setImageResource(R.drawable.fruit);
                    break;
                case "零食":
                    holder.mDetailImageView.setImageResource(R.drawable.snacks);
                    break;
                case "运动":
                    holder.mDetailImageView.setImageResource(R.drawable.sport);
                    break;
                case "娱乐":
                    holder.mDetailImageView.setImageResource(R.drawable.amusement);
                    break;
                case "通讯":
                    holder.mDetailImageView.setImageResource(R.drawable.communication);
                    break;
                case "服饰":
                    holder.mDetailImageView.setImageResource(R.drawable.costume);
                    break;
                case "美容":
                    holder.mDetailImageView.setImageResource(R.drawable.beauty);
                    break;
                case "住房":
                    holder.mDetailImageView.setImageResource(R.drawable.house);
                    break;
                case "居家":
                    holder.mDetailImageView.setImageResource(R.drawable.living);
                    break;
                case "孩子":
                    holder.mDetailImageView.setImageResource(R.drawable.child);
                    break;
                case "长辈":
                    holder.mDetailImageView.setImageResource(R.drawable.elder);
                    break;
                case "社交":
                    holder.mDetailImageView.setImageResource(R.drawable.social);
                    break;
                case "旅行":
                    holder.mDetailImageView.setImageResource(R.drawable.traval);
                    break;
                case "烟酒":
                    holder.mDetailImageView.setImageResource(R.drawable.cig_wine);
                    break;
                case "数码":
                    holder.mDetailImageView.setImageResource(R.drawable.digital);
                    break;
                case "汽车":
                    holder.mDetailImageView.setImageResource(R.drawable.car);
                    break;
                case "医疗":
                    holder.mDetailImageView.setImageResource(R.drawable.medical);
                    break;
                case "书籍":
                    holder.mDetailImageView.setImageResource(R.drawable.books);
                    break;
                case "学习":
                    holder.mDetailImageView.setImageResource(R.drawable.study);
                    break;
                case "宠物":
                    holder.mDetailImageView.setImageResource(R.drawable.pet);
                    break;
                case "礼金":
                    holder.mDetailImageView.setImageResource(R.drawable.cash_gift);
                    break;
                case "礼物":
                    holder.mDetailImageView.setImageResource(R.drawable.gift);
                    break;
                case "办公":
                    holder.mDetailImageView.setImageResource(R.drawable.work);
                    break;
                case "维修":
                    holder.mDetailImageView.setImageResource(R.drawable.maintain);
                    break;
                case "捐赠":
                    holder.mDetailImageView.setImageResource(R.drawable.donate);
                    break;
                case "彩票":
                    holder.mDetailImageView.setImageResource(R.drawable.lottery);
                    break;
                case "工资":
                    holder.mDetailImageView.setImageResource(R.drawable.salary);
                    break;
                case "兼职":
                    holder.mDetailImageView.setImageResource(R.drawable.parttime_job);
                    break;
                case "asset":
                    holder.mDetailImageView.setImageResource(R.drawable.manage_money);
                    break;
                case "其他":
                    holder.mDetailImageView.setImageResource(R.drawable.others);
                    break;
                default:
                    break;
            }
            return convertView;
        }

        class ViewHolder {
            ImageView mDetailImageView;
            TextView mDetailTextView1;
            TextView mDetailTextView2;

            public ViewHolder(View view) {
                mDetailTextView1 = (TextView)view.findViewById(R.id.detail_item_text_view_1);
                mDetailTextView2 = (TextView)view.findViewById(R.id.detail_item_text_view_2);
                mDetailImageView = (ImageView)view.findViewById(R.id.detail_item_image_view);
                view.setTag(this);
            }
        }
    }
    public class DetailForSwipeMenu{
        private int id;
        private String mTitle;
        private double mFee;
        private boolean mIncome;
        private int mYear;
        private int mMonth;
        private int mDay;
        private int mWeek;
        private int mWeekOfMonth;
        public DetailForSwipeMenu(int id,String title, double fee, boolean income, int year, int month, int day, int week, int weekOfMonth) {
            this.id = id;
            mTitle = title;
            mFee = fee;
            mIncome = income;
            mYear = year;
            mMonth = month;
            mDay = day;
            mWeek = week;
            mWeekOfMonth = weekOfMonth;
        }
        public int getId() {return id;}
        public void setID(int ID) {this.id = ID;}
        public int getWeekOfMonth() {return mWeekOfMonth;}
        public void setWeekOfMonth(int weekOfMonth) {mWeekOfMonth = weekOfMonth;}
        public int getWeek() {return mWeek;}
        public void setWeek(int week) {mWeek = week;}
        public boolean isIncome() {
            return mIncome;
        }
        public void setIncome(boolean income) {mIncome = income;}
        public int getYear() {
            return mYear;
        }
        public void setYear(int year) {
            mYear = year;
        }
        public int getMonth() {
            return mMonth;
        }
        public void setMonth(int month) {
            mMonth = month;
        }
        public int getDay() {
            return mDay;
        }
        public void setDay(int day) {
            mDay = day;
        }
        public String getTitle() {
            return mTitle;
        }
        public void setTitle(String title) {
            mTitle = title;
        }
        public double getFee() {
            return mFee;
        }
        public void setFee(double fee) {
            mFee = fee;
        }
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
        init(year,month,day);
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
