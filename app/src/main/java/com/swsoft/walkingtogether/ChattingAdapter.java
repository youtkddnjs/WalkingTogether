package com.swsoft.walkingtogether;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingAdapter extends BaseAdapter {

    Context context;
    ArrayList<ChattingItem> chattingItems;

    public ChattingAdapter(Context context, ArrayList<ChattingItem> chattingItems) {
        this.context = context;
        this.chattingItems = chattingItems;
    }

    @Override
    public int getCount() {
        return chattingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chattingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChattingItem item = chattingItems.get(position);

        View itemView = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        
        //채팅을 보내는 닉네임에 의해 뷰를 선택
        if( item.name.equals(LoginUserInfo.nickname) ){
            itemView = inflater.inflate(R.layout.mymessagebox, parent, false);
        }else{
            itemView = inflater.inflate(R.layout.usermessagebox, parent, false);
        }

        CircleImageView civ = itemView.findViewById(R.id.civ);
        TextView tvName =itemView.findViewById(R.id.tv_name);
        TextView tvMsg =itemView.findViewById(R.id.tv_msg);
        TextView tvTime =itemView.findViewById(R.id.tv_time);

        tvName.setText(item.name);
        tvMsg.setText(item.message);
        tvTime.setText(item.time);
        Glide.with(context).load(item.profile).into(civ);

        return itemView;
    }
}
