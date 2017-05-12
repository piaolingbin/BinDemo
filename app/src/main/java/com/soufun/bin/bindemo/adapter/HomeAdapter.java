package com.soufun.bin.bindemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soufun.bin.bindemo.R;
import com.soufun.bin.bindemo.utils.Utils;
import com.soufun.bin.bindemo.view.CircleImageView;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements View.OnClickListener{

    private static final int NORMAL_TYPLE = 0;
    private static final int TIME_TYPLE = 1;
    private OnItemClickListener onItemClickListener = null;

    public static interface OnItemClickListener{
        void onItemClick(View view , int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch(viewType){
            case NORMAL_TYPLE:
                itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
                break;
            case TIME_TYPLE:
                itemView = mInflater.inflate(R.layout.recyclerview_time_item , parent , false);
                break;
            default:
                itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        }
        itemView.setOnClickListener(this);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case NORMAL_TYPLE:
                holder.tv_title.setText("hello recyclerview" + position );
                break;
            case TIME_TYPLE:
                holder.tv_time_title.setText(Utils.getWeek(position / 4));
                break;
        }
        // 将position放入itemview的tag，以便点击的时候获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return 28;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position % 4 == 0){
            return TIME_TYPLE;
        }
        return NORMAL_TYPLE;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(v , (int) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_header;
        CircleImageView circleImageView;
        TextView tv_title;
        TextView tv_time_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_header = (LinearLayout) itemView.findViewById(R.id.ll_header);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            tv_time_title = (TextView) itemView.findViewById(R.id.tv_time_title);
        }
    }
}
