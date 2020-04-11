package com.bignerdranch.android.myapplication;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by T540p on 2017/9/7.
 */


public class MingleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<mingle_social> mMingleSocials;
    private static int TYPE_ITEM=0;
    private static int TYPE_FOOTER=1;
    private MyItemClickListener mItemClickListener=null;
    private Context mContext;

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mPersonalIcon;
        TextView mWriter;
        TextView mGood;
        TextView mText;
        TextView mComment;
        TextView mTime;


        public ItemViewHolder(View view){
            super(view);

            mWriter=(TextView)view.findViewById(R.id.tv_petName_mingle);
//            mGood=(TextView)view.findViewById(R.id.tv_good_number);
            mText=(TextView)view.findViewById(R.id.tv_text_mingle);
            mPersonalIcon=(ImageView)view.findViewById(R.id.iv_personal_icon_mingle);
//            mComment=(TextView)view.findViewById(R.id.tv_mingle_comment);
            mTime=(TextView)view.findViewById(R.id.tv_time_mingle);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    public MingleAdapter(Context context,List<mingle_social> mingle_socials){
        mMingleSocials=mingle_socials;
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType==TYPE_FOOTER){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
            return new FootViewHolder(view);
        }else if(viewType==TYPE_ITEM){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mingle_item,parent,false);

            view.setOnClickListener(this);

            return new ItemViewHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position){
        if(holder instanceof ItemViewHolder){
            mingle_social mingle_social=mMingleSocials.get(position);
            ((ItemViewHolder) holder).mWriter.setText(mingle_social.getWriter());
            ((ItemViewHolder) holder).mText.setText(mingle_social.getText());
//            ((ItemViewHolder) holder).mGood.setText(mingle_social.getGood());
            ((ItemViewHolder) holder).mTime.setText(mingle_social.getTime());
            Glide.with(mContext).load(mMingleSocials.get(position).getWriterPersonalIcon()).error(R.mipmap.person).into(((ItemViewHolder) holder).mPersonalIcon);
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
        return mMingleSocials.size()==0?0:mMingleSocials.size()+1;
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

