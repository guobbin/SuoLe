package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by T540p on 2017/7/28.
 */



public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<News> mNewsList;
    private static int TYPE_ITEM=0;
    private static int TYPE_FOOTER=1;
    private MyItemClickListener mItemClickListener=null;
    private Context mContext;


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsTime;
        ImageView photo;

        public ItemViewHolder(View view){
            super(view);

            photo=(ImageView)view.findViewById(R.id.news_image);
            newsTime=(TextView)view.findViewById(R.id.news_time);
            newsTitle=(TextView)view.findViewById(R.id.news_title);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }


    public NewsAdapter(Context context,List<News> newses){
        mNewsList=newses;
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType==TYPE_FOOTER){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
            return new FootViewHolder(view);
        }else if(viewType==TYPE_ITEM){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);

            view.setOnClickListener(this);

            return new ItemViewHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position){
        if(holder instanceof ItemViewHolder){
            News news=mNewsList.get(position);
            ((ItemViewHolder) holder).newsTime.setText(news.getNewsTime());
            ((ItemViewHolder) holder).newsTitle.setText(news.getNewsTitle());
            if(news.getPtoUrl()!=null){
                Glide.with(mContext).load(news.getPtoUrl()).into(((ItemViewHolder) holder).photo);
            }
            holder.itemView.setTag(position);
        }

    }


    @Override
    public void onClick(View v){
        if(mItemClickListener!=null){
            mItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setItemClickListener(MyItemClickListener myItemClickListener){
        this.mItemClickListener=myItemClickListener;
    }

    @Override
    public int getItemCount(){
        return mNewsList.size()==0?0:mNewsList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
       if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
}
