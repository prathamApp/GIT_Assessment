package com.pratham.assessment.ui.bottom_fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.Student;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anki on 10/30/2018.
 */

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {
    List<Student> studentAvatarList;
    ArrayList avatarList;
    StudentClickListener studentClickListener;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        ImageView avatar;
        RelativeLayout rl_card;

        public MyViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.content_title);
            avatar = itemView.findViewById(R.id.content_thumbnail);
            rl_card = itemView.findViewById(R.id.rl_card);
        }
    }

    public StudentsAdapter(Context c, Fragment context, List<Student> studentAvatars, ArrayList avatarList) {
        this.studentAvatarList = studentAvatars;
        this.avatarList = avatarList;
        this.studentClickListener = (StudentClickListener) context;
        this.mContext = c;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Student studentAvatar = studentAvatarList.get(position);
        holder.studentName.setText(Html.fromHtml(studentAvatar.getFullName()));

       /* int imgId= Integer.parseInt(studentAvatar.getAvatarName());
        holder.avatar.setImageResource(imgId);*/

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
        /* Glide.with(mContext).load(*//*AssessmentApplication.pradigiPath*//*"" + "/.LLA/English/LLA_Thumbs/" + studentAvatar.getAvatarName()).into(holder.avatar);
         */
        holder.rl_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentAvatarList.get(position).getStudentID();
                studentClickListener.onStudentClick(studentAvatarList.get(position).getFullName(), studentAvatarList.get(position).getStudentID(), studentAvatarList.get(position).getGroupId(),studentAvatarList.get(position).getIsniosstudent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentAvatarList.size();
    }


}
