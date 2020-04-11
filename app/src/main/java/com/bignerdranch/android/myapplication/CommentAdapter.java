package com.bignerdranch.android.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by T540p on 2017/11/7.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<Comment> mCommentList;
    private static int TYPE_ITEM=0;
    private MyItemClickListener mItemClickListener=null;
    private Context mContext;

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mPersonalIcon;
        TextView mWriter;
        TextView mText;
        TextView mTime;
        TextView mAnswer;
        TextView mAnswerText;


        public ItemViewHolder(View view){
            super(view);

            mWriter=(TextView)view.findViewById(R.id.tv_petName_mingle_recycler);
            mText=(TextView)view.findViewById(R.id.tv_text_mingle_recycler);
            mPersonalIcon=(ImageView)view.findViewById(R.id.iv_personal_icon_mingle);
            mTime=(TextView)view.findViewById(R.id.tv_time_mingle_recycler);
            mAnswer=(TextView)view.findViewById(R.id.tv_answer_person);
            mAnswerText=(TextView)view.findViewById(R.id.tv_answer);
        }
    }


    public CommentAdapter(Context context,List<Comment> comments){
        mCommentList=comments;
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType==TYPE_ITEM){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);

            view.setOnClickListener(this);

            return new ItemViewHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position){
        if(holder instanceof ItemViewHolder){
            Comment comment=mCommentList.get(position);
            ((ItemViewHolder) holder).mWriter.setText(comment.getObserver());
            ((ItemViewHolder) holder).mText.setText(comment.getContent());
            ((ItemViewHolder) holder).mTime.setText(comment.getMmTime());
            ((ItemViewHolder) holder).mAnswer.setText(comment.getAuthor());
            if (comment.getAuthor()==null){
                ((ItemViewHolder) holder).mAnswerText.setVisibility(View.GONE);
            }else{
                ((ItemViewHolder) holder).mAnswerText.setVisibility(View.VISIBLE);
            }
            Glide.with(mContext).load(mCommentList.get(position).getObPhoto()).error(R.mipmap.person).into(((ItemViewHolder) holder).mPersonalIcon);
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
        return mCommentList.size();
    }

    @Override
    public int getItemViewType(int position) {
            return TYPE_ITEM;
    }
}
