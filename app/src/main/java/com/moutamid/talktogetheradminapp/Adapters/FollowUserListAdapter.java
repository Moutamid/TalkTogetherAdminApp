package com.moutamid.talktogetheradminapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.talktogetheradminapp.ItemClickListener;
import com.moutamid.talktogetheradminapp.Model.User;
import com.moutamid.talktogetheradminapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowUserListAdapter extends RecyclerView.Adapter<FollowUserListAdapter.UserListViewHolder> {

    private Context context;
    private List<User> userList;
    private ItemClickListener itemClickListener;

    public FollowUserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User model = userList.get(position);
        holder.nameTxt.setText(model.getFname()+" " + model.getLname());
        if (model.getImageUrl().equals("")){
            Picasso.with(context)
                    .load(R.drawable.profile)
                    .into(holder.profileImg);
        }else{
            Picasso.with(context)
                    .load(model.getImageUrl())
                    .into(holder.profileImg);
        }

    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileImg;
        private TextView nameTxt;
        private ImageView menuImg;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.user_img);
            nameTxt = itemView.findViewById(R.id.user_name);
            menuImg = itemView.findViewById(R.id.menu);
            menuImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(getAdapterPosition(),view);
                    }
                }
            });
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
