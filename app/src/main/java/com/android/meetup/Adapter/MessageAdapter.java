package com.android.meetup.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.meetup.Model.Chats;
import com.android.meetup.R;
import com.android.meetup.Utility.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Chats> mChats;
    private String imageURL=null;
    FirebaseUser fUser;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MSG_TYPE_LEFT) {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
        } else {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chats Chats=mChats.get(position);
        holder.show_message.setText(Chats.getMessage());
        if(!imageURL.equals("default"))
            Picasso.get().load(imageURL).resize(50,50)
                    .transform(new CircleTransform()).error(R.drawable.ic_default).into(holder.profile_image);    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView show_message;
        private final ImageView profile_image;

        public ViewHolder(View view){
            super(view);
            show_message=view.findViewById(R.id.show_message);
            profile_image=view.findViewById(R.id.profile_image);
        }

    }
    public MessageAdapter(Context context, ArrayList<Chats> mChats, String imageURL){
        this.context=context;
        this.mChats=mChats;
        this.imageURL=imageURL;
    }

    @Override
    public int getItemViewType(int position) {
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if(fUser.getUid().equals(mChats.get(position).getSender())) return MSG_TYPE_RIGHT;
        else return MSG_TYPE_LEFT;
    }
}
