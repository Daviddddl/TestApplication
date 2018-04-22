package com.example.didonglin.testapplication;

/**
 * @author ymc
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @Date 2018年04月08日 15:10
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MsgAdapter extends BaseAdapter {

    private List<Msg> mMsgList;
    private Context context;

    public MsgAdapter(Context context, List<Msg> mMsgList) {
        this.context = context;
        this.mMsgList = mMsgList;
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMsgList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Msg msg = mMsgList.get(position);
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.msg_item, null, false);
            holder = new ViewHolder();
            holder.left_image = view.findViewById(R.id.left_image);
            holder.right_image = view.findViewById(R.id.right_image);
            holder.leftMsg = view.findViewById(R.id.left_msg);
            holder.rightMsg = view.findViewById(R.id.right_msg);
            holder.time_left = view.findViewById(R.id.time_left);
            holder.time_right = view.findViewById(R.id.time_right);
            holder.leftLayout = view.findViewById(R.id.left_layout);
            holder.rightLayout = view.findViewById(R.id.right_layout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getText());
            holder.time_left.setText(msg.getTime());

        } else if (msg.getType() == Msg.TYPE_SEND) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getText());
            holder.time_right.setText(msg.getTime());
        }
        return view;
    }

    static class ViewHolder {

        RelativeLayout leftLayout;
        RelativeLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        TextView time_left;
        TextView time_right;
        ImageView left_image;
        ImageView right_image;
    }

    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
    }

    public void refresh(List<Msg> msgs){
        this.mMsgList = msgs;
        this.notifyDataSetChanged();
    }

}

