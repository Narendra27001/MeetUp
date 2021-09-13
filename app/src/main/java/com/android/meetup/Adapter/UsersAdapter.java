package com.android.meetup.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.meetup.Model.Users;
import com.android.meetup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Users> mUsers;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users=mUsers.get(position);
        holder.name.setText(users.getUsername());
        if(!users.getImageURL().equals("default"))
            Picasso.get().load(users.getImageURL()).error(R.drawable.ic_default).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final ImageView image;

        public ViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.chat_name);
            image=view.findViewById(R.id.image_profile);
        }

    }
    public UsersAdapter(Context context, ArrayList<Users> mUsers){
            this.context=context;
            this.mUsers=mUsers;
    }
}
