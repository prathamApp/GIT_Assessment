package com.pratham.assessment.ui.choose_assessment.science.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.bottomFragment.BottomQuestionFragment;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTrackerListener;

import java.util.List;

public class QuestionTrackerAdapter extends RecyclerView.Adapter<QuestionTrackerAdapter.MyViewHolder> {
    List<ScienceQuestion> scienceQuestionList;
    Context context;
    QuestionTrackerListener questionTrackerListener;
    BottomQuestionFragment bottomQuestionFragment;

    public QuestionTrackerAdapter(BottomQuestionFragment bottomQuestionFragment, Context context, List<ScienceQuestion> scienceQuestions) {
        this.scienceQuestionList = scienceQuestions;
        this.context = context;
        questionTrackerListener = (QuestionTrackerListener) context;
        this.bottomQuestionFragment=bottomQuestionFragment;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
//        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_question_no);
//            imageView = itemView.findViewById(R.id.iv_choice_image);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_question_tracker_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        ScienceQuestion scienceQuestion = scienceQuestionList.get(i);
        final int queNo = i + 1;
        myViewHolder.text.setText(queNo + "");
        if (scienceQuestion.getIsAttempted())
//            myViewHolder.text.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            myViewHolder.text.setBackground(context.getResources().getDrawable(R.drawable.ripple_round_attempted));
        else
            myViewHolder.text.setBackground(context.getResources().getDrawable(R.drawable.ripple_round_not_attempted));
//            myViewHolder.text.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        myViewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionTrackerListener.onQuestionClick(queNo);
                bottomQuestionFragment.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return scienceQuestionList.size();
    }
}
