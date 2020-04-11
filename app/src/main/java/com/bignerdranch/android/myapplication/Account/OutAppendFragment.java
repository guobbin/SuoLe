package com.bignerdranch.android.myapplication.Account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.bignerdranch.android.myapplication.R;
/**
 * Created by Administrator on 2017/9/17/017.
 */
public class OutAppendFragment extends Fragment {
    private List<Detail> detailList = new ArrayList<>();
    private int year,month,day,week,weekOfMonth;
    public void onCreate(Bundle saveInstanceState){super.onCreate(saveInstanceState);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_append_out,container,false);
        initDetail();

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.append_recycler_list_out);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        AppendAdapter adapter = new AppendAdapter(detailList);

        adapter.setOnItemClickListener(new AppendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,final int position) {
                final EditText et=new EditText(getContext());
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                new AlertDialog.Builder(getContext()).setTitle(detailList.get(position).getTitle()).setView(et).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1){
                                date();
                                String s=et.getText().toString();
                                    Detail detail = new Detail();
                                    detail.setFee(Double.parseDouble(s));
                                    detail.setTitle(detailList.get(position).getTitle());
                                    detail.setIncome(false);
                                    detail.setYear(year);
                                    detail.setMonth(month);
                                    detail.setDay(day);
                                    detail.setWeek(week);
                                    detail.setWeekOfMonth(weekOfMonth);
                                    detail.save();
                                    getActivity().finish();
                            }
                        }).setNegativeButton("取消",null).show();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        recyclerView.setAdapter(adapter);
        return v;
    }
    private void initDetail() {
        String []s =new String[31];
        s[0]="餐饮";s[1]="购物";s[2]="日用";s[3]="交通";
        s[4]="蔬菜";s[5]="水果";s[6]="零食";s[7]="运动";
        s[8]="娱乐";s[9]="通讯";s[10]="服饰";s[11]="美容";
        s[12]="住房";s[13]="居家";s[14]="孩子";s[15]="长辈";
        s[16]="社交";s[17]="旅行";s[18]="烟酒";s[19]="数码";
        s[20]="汽车";s[21]="医疗";s[22]="书籍";s[23]="学习";s[24]="宠物";
        s[25]="礼金";s[26]="办公";s[27]="维修";s[28]="捐赠";s[29]="彩票";s[30]="其他";
            for(int i=0; i<31; i++) {
                Detail detail = new Detail();
                detail.setTitle(s[i]);
                detail.setIncome(false);
                detailList.add(detail);
        }
    }
    private void date() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        week = c.get(Calendar.DAY_OF_WEEK);
        weekOfMonth = c.get(Calendar.WEEK_OF_MONTH);
    }
}
