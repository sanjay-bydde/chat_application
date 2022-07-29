package com.example.techchatty.Adapter;

import static com.example.techchatty.Activity.chatActivity.rImage;
import static com.example.techchatty.Activity.chatActivity.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techchatty.Activity.chatActivity;
import com.example.techchatty.ModelClass.Messages;
import com.example.techchatty.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdater extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;


    public MessagesAdater(Context context,ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SendViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.reciver_layout_item,parent,false);
            return new ReciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages=messagesArrayList.get(position);

        if(holder.getClass()==SendViewHolder.class)
        {
            SendViewHolder viewHolder=(SendViewHolder) holder;
            viewHolder.sendtxtmessage.setText(messages.getMessage());

            Picasso.get().load(sImage).into(viewHolder.circleImageView);
        }
        else
        {
            ReciverViewHolder viewHolder=(ReciverViewHolder) holder;
            viewHolder.recivetxtmessage.setText(messages.getMessage());

            Picasso.get().load(rImage).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIVE;
        }
    }

    class SendViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView sendtxtmessage;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            sendtxtmessage=itemView.findViewById(R.id.sendtxtMessages);
        }
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView recivetxtmessage;

         public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

             circleImageView=itemView.findViewById(R.id.profile_image);
             recivetxtmessage=itemView.findViewById(R.id.recivetxtMessages);
        }
    }
}
