package com.example.mychatroom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder>{
    private static final String TAG = "UserAdapter";

    private Context context;
    private List<MsgModel> msgModelList;

    public MsgAdapter(Context context){
        this.context = context;
        msgModelList = new ArrayList<>();
    }

    public void add(MsgModel msgModel){
        msgModelList.add(msgModel);
        notifyDataSetChanged();
    }

    public void clear(){
        msgModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MsgModel msgModel = msgModelList.get(position);
        holder.msg.setText(msgModel.getMsg());
        if (msgModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.msg.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return msgModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView msg;
        private LinearLayout main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            main = itemView.findViewById(R.id.mainMsgLayout);
        }
    }
}
