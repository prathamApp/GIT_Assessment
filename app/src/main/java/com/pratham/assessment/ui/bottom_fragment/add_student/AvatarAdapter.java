package com.pratham.assessment.ui.bottom_fragment.add_student;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.AvatarModal;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.ArrayList;

/**
 * Created by Anki on 10/30/2018.
 */

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.MyViewHolder> {
    /* List<String> studentAvatarList;*/
    ArrayList<AvatarModal> avatarList;
    Context context;
    AvatarClickListener avatarClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.iv_avatar);
           /* word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/
        }
    }

    public AvatarAdapter(Context context, Fragment fragment, ArrayList<AvatarModal> avatarList) {
        this.avatarList = avatarList;
        this.context = context;
        this.avatarClickListener = (AvatarClickListener) fragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_avatar_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AvatarModal studentAvatar = avatarList.get(position);
//        holder.avatar.setImageResource(Integer.parseInt(avatarList.get(position).toString()));
        //Glide.with(context).load(studentAvatar).into(holder.avatar);

     /*   int imgId = Integer.parseInt(studentAvatar.getAvatarName());
        holder.avatar.setImageResource(imgId);
*/

        switch (studentAvatar.getAvatarName()) {
            case "b1.png":
                holder.avatar.setImageResource(R.drawable.b1);
                break;
            case "b2.png":
                holder.avatar.setImageResource(R.drawable.b2);
                break;
            case "b3.png":
                holder.avatar.setImageResource(R.drawable.b3);
                break;
            case "g1.png":
                holder.avatar.setImageResource(R.drawable.g1);
                break;
            case "g2.png":
                holder.avatar.setImageResource(R.drawable.g2);
                break;
            case "g3.png":
                holder.avatar.setImageResource(R.drawable.g3);
                break;
        }


        if (studentAvatar.getClickFlag())
            holder.avatar.setBackground(context.getResources().getDrawable(R.drawable.ripple_rectangle_transparent));
        else
            holder.avatar.setBackground(context.getResources().getDrawable(R.drawable.custom_edit_text));


        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarClickListener.onAvatarClick(position, studentAvatar.getAvatarName());
                /*}  else
                    holder.avatar.setBackground(context.getResources().getDrawable(R.drawable.ripple_rectangle));
*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return avatarList.size();
    }


}

