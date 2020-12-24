package com.pratham.assessment.ui.login.group_selection.fragment_child_attendance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.Student;

import java.io.File;
import java.util.ArrayList;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

import static com.pratham.assessment.constants.Assessment_Constants.StudentPhotoPath;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildHolder> {
    private ArrayList<Student> datalist;
    private ArrayList<Integer> child_avatar;
    private Context context;
    int finalPos;
    private ContractChildAttendance.attendanceView attendanceView;

    public ChildAdapter(Context context, ArrayList<Student> datalist,
                        ArrayList<Integer> child_avatar, ContractChildAttendance.attendanceView attendanceView) {
        this.context = context;
        this.datalist = datalist;
        this.child_avatar = child_avatar;
        this.attendanceView = attendanceView;
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_child_attendance, parent, false);
        return new ChildHolder(v);
    }

   /* @Override
    public void onBindViewHolder(@NonNull final ChildHolder viewHolder, int pos) {
        pos = viewHolder.getAdapterPosition();
        viewHolder.child_name.setText(datalist.get(pos).getFullName());
        //viewHolder.child_avatar.setAnimation(child_avatar.get(pos));
        viewHolder.child_avatar.setImageResource(child_avatar.get(pos));
//        if (datalist.get(pos).isChecked()) {
//            viewHolder.card_avatar.setCardBackgroundColor(context.getResources().getColor(R.color.green));
//        } else {
//            viewHolder.card_avatar.setCardBackgroundColor(context.getResources().getColor(R.color.white));
//        }
        final int finalPos = pos;
        viewHolder.child_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COSApplication.isTablet)
                    attendanceView.childItemClicked(datalist.get(finalPos), finalPos,viewHolder.itemView);
                else
                    attendanceView.moveToDashboardOnChildClick(datalist.get(finalPos), finalPos, viewHolder.itemView);
            }
        });
    }*/

    @Override
    public void onBindViewHolder(@NonNull final ChildHolder viewHolder, int pos) {
        // pos = viewHolder.getAdapterPosition();
        viewHolder.child_name.setText(Html.fromHtml(datalist.get(pos).getFullName()));
        File file;
        file = new File(StudentPhotoPath + "" + datalist.get(pos).getStudentID() + ".jpg");
        String filepath = file.getPath();
        if (file.exists()) {
            Glide.with(context)
                    .load(filepath)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )

                    .into(viewHolder.child_avatar);

        } else {

            viewHolder.child_avatar.setImageResource(child_avatar.get(pos));
        }

        if (datalist.get(viewHolder.getAdapterPosition()).isChecked()) {
//            viewHolder.child_avatar.setBackground(context.getResources().getDrawable(R.drawable.petal_shape));
//            viewHolder.itemView.setBackground(context.getResources().getDrawable(R.drawable.correct_bg));
            viewHolder.tick.setVisibility(View.VISIBLE);

        } else {
            viewHolder.child_avatar.setBackground(context.getResources().getDrawable(R.drawable.hexagone));
//            viewHolder.itemView.setBackground(context.getResources().getDrawable(R.drawable.ripple_rectangle));
            viewHolder.tick.setVisibility(View.GONE);

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceView.childItemClicked(datalist.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
            }
        });

        viewHolder.iv_camera.setOnClickListener(v -> attendanceView.clickPhoto(datalist.get(pos).getStudentID(), pos));
    }
/*
    @Override
    public void onBindViewHolder(@NonNull ChildHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Student student = (Student) payloads.get(0);
            holder.child_name.setText(student.getFullName());
            if (student.isChecked()) {
                holder.card_avatar.setCardBackgroundColor(context.getResources().getColor(R.color.green));
            } else {
                holder.card_avatar.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attendanceView.childItemClicked(datalist.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            });
        }
    }

    public void updateChildItems(final ArrayList<Student> newStudents) {
        final ChildAttendanceDiffCallback diffCallback = new ChildAttendanceDiffCallback(this.datalist, newStudents);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//        this.datalist.clear();
//        this.datalist.addAll(newStudents);
        this.datalist = newStudents;
        diffResult.dispatchUpdatesTo(this);
    }*/

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ChildHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.child_name)
        TextView child_name;
        /*  @BindView(R.id.child_avatar)
          LottieAnimationView child_avatar; */
//        @BindView(R.id.iv_child)
        ImageView child_avatar;
        //        @BindView(R.id.iv_tick)
        ImageView tick;
        //        @BindView(R.id.iv_camera)
        ImageView iv_camera;
       /* @BindView(R.id.child_card)
        MaterialCardView group_card;*/
//        @BindView(R.id.card_avatar)
//        MaterialCardView card_avatar;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            child_name = itemView.findViewById(R.id.child_name);
            child_avatar = itemView.findViewById(R.id.iv_child);
            tick = itemView.findViewById(R.id.iv_tick);
            iv_camera = itemView.findViewById(R.id.iv_camera);
        }
    }
}
