package com.bignerdranch.android.myapplication.Account;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.myapplication.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class AppendAdapter extends RecyclerView.Adapter<AppendAdapter.ViewHolder>{

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    private List<Detail> mDetailList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mAppendImageView;
        TextView mAppendTextView;
        public ViewHolder(View view){
            super(view);
            mAppendTextView = (TextView)view.findViewById(R.id.append_item_text_view);
            mAppendImageView = (ImageView)view.findViewById(R.id.append_item_image_view);
        }
    }
    public AppendAdapter(List<Detail> detailList){
        mDetailList = detailList;
    }
    @Override
    public AppendAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.append_item,null,false);
        final AppendAdapter.ViewHolder holder = new AppendAdapter.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final AppendAdapter.ViewHolder holder, int position) {
        Detail detail = mDetailList.get(position);
        holder.mAppendTextView.setText(detail.getTitle());
        if(!detail.isIncome()) {
            switch (position) {
                case 0:
                    holder.mAppendImageView.setImageResource(R.drawable.dinner);
                    break;
                case 1:
                    holder.mAppendImageView.setImageResource(R.drawable.shopping);
                    break;
                case 2:
                    holder.mAppendImageView.setImageResource(R.drawable.daily_use);
                    break;
                case 3:
                    holder.mAppendImageView.setImageResource(R.drawable.traffic);
                    break;
                case 4:
                    holder.mAppendImageView.setImageResource(R.drawable.vegetable);
                    break;
                case 5:
                    holder.mAppendImageView.setImageResource(R.drawable.fruit);
                    break;
                case 6:
                    holder.mAppendImageView.setImageResource(R.drawable.snacks);
                    break;
                case 7:
                    holder.mAppendImageView.setImageResource(R.drawable.sport);
                    break;
                case 8:
                    holder.mAppendImageView.setImageResource(R.drawable.amusement);
                    break;
                case 9:
                    holder.mAppendImageView.setImageResource(R.drawable.communication);
                    break;
                case 10:
                    holder.mAppendImageView.setImageResource(R.drawable.costume);
                    break;
                case 11:
                    holder.mAppendImageView.setImageResource(R.drawable.beauty);
                    break;
                case 12:
                    holder.mAppendImageView.setImageResource(R.drawable.house);
                    break;
                case 13:
                    holder.mAppendImageView.setImageResource(R.drawable.living);
                    break;
                case 14:
                    holder.mAppendImageView.setImageResource(R.drawable.child);
                    break;
                case 15:
                    holder.mAppendImageView.setImageResource(R.drawable.elder);
                    break;
                case 16:
                    holder.mAppendImageView.setImageResource(R.drawable.social);
                    break;
                case 17:
                    holder.mAppendImageView.setImageResource(R.drawable.traval);
                    break;
                case 18:
                    holder.mAppendImageView.setImageResource(R.drawable.cig_wine);
                    break;
                case 19:
                    holder.mAppendImageView.setImageResource(R.drawable.digital);
                    break;
                case 20:
                    holder.mAppendImageView.setImageResource(R.drawable.car);
                    break;
                case 21:
                    holder.mAppendImageView.setImageResource(R.drawable.medical);
                    break;
                case 22:
                    holder.mAppendImageView.setImageResource(R.drawable.books);
                    break;
                case 23:
                    holder.mAppendImageView.setImageResource(R.drawable.study);
                    break;
                case 24:
                    holder.mAppendImageView.setImageResource(R.drawable.pet);
                    break;
                case 25:
                    holder.mAppendImageView.setImageResource(R.drawable.cash_gift);
                    break;
                case 26:
                    holder.mAppendImageView.setImageResource(R.drawable.work);
                    break;
                case 27:
                    holder.mAppendImageView.setImageResource(R.drawable.maintain);
                    break;
                case 28:
                    holder.mAppendImageView.setImageResource(R.drawable.donate);
                    break;
                case 29:
                    holder.mAppendImageView.setImageResource(R.drawable.lottery);
                    break;
                case 30:
                    holder.mAppendImageView.setImageResource(R.drawable.others);
                    break;
                default:
                    break;
            }
        }else {
            switch (position) {
                case 0:
                    holder.mAppendImageView.setImageResource(R.drawable.salary);
                    break;
                case 1:
                    holder.mAppendImageView.setImageResource(R.drawable.parttime_job);
                    break;
                case 2:
                    holder.mAppendImageView.setImageResource(R.drawable.manage_money);
                    break;
                case 3:
                    holder.mAppendImageView.setImageResource(R.drawable.cash_gift);
                    break;
                case 4:
                    holder.mAppendImageView.setImageResource(R.drawable.others);
                    break;
                default:
                    break;
            }
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDetailList.size();
    }

}
