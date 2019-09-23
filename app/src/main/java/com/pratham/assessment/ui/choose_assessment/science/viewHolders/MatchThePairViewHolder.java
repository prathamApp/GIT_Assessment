package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.adapters.DragDropAdapter;
import com.pratham.assessment.ui.choose_assessment.science.ItemMoveCallback;
import com.pratham.assessment.ui.choose_assessment.science.adapters.MatchPairAdapter;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.StartDragListener;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchThePairViewHolder extends RecyclerView.ViewHolder implements StartDragListener {
    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;
    @BindView(R.id.rl_ans_options1)
    RecyclerView recyclerView1;
    @BindView(R.id.rl_ans_options2)
    RecyclerView recyclerView2;
    ScienceQuestion scienceQuestion;
    Context context;
    QuestionTypeListener questionTypeListener;
    AssessmentAnswerListener assessmentAnswerListener;

    ScienceAdapter scienceAdapter;


    ItemTouchHelper touchHelper;

    public MatchThePairViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        questionTypeListener = scienceAdapter;
        this.scienceAdapter = scienceAdapter;
        assessmentAnswerListener = (ScienceAssessmentActivity) context;
    }

    public void setMatchPairQuestion(ScienceQuestion scienceQuestion1, int pos) {
        this.scienceQuestion = scienceQuestion1;

        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().
                    load(/*Assessment_Constants.loadOnlineImagePath + */scienceQuestion.getPhotourl()).apply(new RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable bd = new BitmapDrawable(resource);
                            questionImage.setImageDrawable(bd);
                        }
                    });
        } else questionImage.setVisibility(View.GONE);


        List<ScienceQuestionChoice> pairList = new ArrayList<>();
        List<ScienceQuestionChoice> shuffledList = new ArrayList<>();

        pairList.clear();
//        pairList = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        pairList = scienceQuestion.getLstquestionchoice();
        Log.d("wwwwwwwwwww", pairList.size() + "");
        if (!pairList.isEmpty()) {
          /*  for (int p = 0; p < pairList.size(); p++) {
                list1.add(pairList.get(p).getChoicename());
                list2.add(pairList.get(p).getMatchingname());
            }*/
            MatchPairAdapter matchPairAdapter = new MatchPairAdapter(pairList, context);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context.getApplicationContext());
            recyclerView1.setLayoutManager(linearLayoutManager);
            recyclerView1.setAdapter(matchPairAdapter);
            if (scienceQuestion.getMatchingNameList() == null) {
                shuffledList.clear();
                shuffledList.addAll(pairList);
                Collections.shuffle(shuffledList);
            } else {
                shuffledList = scienceQuestion.getMatchingNameList();
            }

         /*   DragDropAdapter dragDropAdapter = new DragDropAdapter(this, shuffledList, context, scienceAdapter);
            ItemTouchHelper.Callback callback =
                    new ItemMoveCallback(dragDropAdapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(null);
            touchHelper.attachToRecyclerView(recyclerView2);

            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context.getApplicationContext());
            recyclerView2.setLayoutManager(linearLayoutManager1);
            recyclerView2.setAdapter(dragDropAdapter);
            Log.d("wwwwwwwwwww", pairList.size() + "");*/
        }
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
}
