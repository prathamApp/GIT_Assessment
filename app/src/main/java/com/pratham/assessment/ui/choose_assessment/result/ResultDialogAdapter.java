package com.pratham.assessment.ui.choose_assessment.result;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;

public class ResultDialogAdapter extends RecyclerView.Adapter<ResultDialogAdapter.MyViewHolder> {
    Context context;
    List<ScienceQuestionChoice> scienceQuestionChoices;
    String type = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_text);
            image = itemView.findViewById(R.id.iv_choice_image);
        }
    }


    public ResultDialogAdapter(Context context, List<ScienceQuestionChoice> scienceQuestionChoices, String type) {
        this.context = context;
        this.scienceQuestionChoices = scienceQuestionChoices;
        this.type = type;
    }

    @NonNull
    @Override
    public ResultDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_simple_text_row, viewGroup, false);
        return new ResultDialogAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ScienceQuestionChoice scienceQuestionChoice = scienceQuestionChoices.get(i);
        if (type.equalsIgnoreCase("que")) {
            if (!scienceQuestionChoice.getChoiceurl().equalsIgnoreCase("")) {
                myViewHolder.image.setVisibility(View.VISIBLE);
                myViewHolder.text.setVisibility(View.GONE);

                Glide.with(context).asBitmap().
                        load(Assessment_Constants.loadOnlineImagePath + scienceQuestionChoice.getChoiceurl()).apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                        .into(myViewHolder.image);
            } else myViewHolder.text.setText(scienceQuestionChoice.getChoicename());
        } else {
            if (type.equalsIgnoreCase("ans")) {
                if (!scienceQuestionChoice.getMatchingurl().equalsIgnoreCase("")) {
                    myViewHolder.image.setVisibility(View.VISIBLE);
                    myViewHolder.text.setVisibility(View.GONE);

                    Glide.with(context).asBitmap().
                            load(Assessment_Constants.loadOnlineImagePath + scienceQuestionChoice.getMatchingurl()).apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                            .into(myViewHolder.image);
                } else myViewHolder.text.setText(scienceQuestionChoice.getMatchingname());
            }
        }
    }


    @Override
    public int getItemCount() {
        return scienceQuestionChoices.size();
    }
}
