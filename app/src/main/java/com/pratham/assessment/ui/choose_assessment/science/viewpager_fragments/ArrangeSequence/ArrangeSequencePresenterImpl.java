package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.ArrangeSequence;

import android.content.Context;
import android.util.Log;

import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EBean
public class ArrangeSequencePresenterImpl implements ArrangeSequenceContract.ArrangeSequencePresenter {
    private Context context;
    private ArrangeSequenceContract.ArrangeSequenceView arrangeSequenceView;

  /*  public ArrangeSequencePresenterImpl(Context context) {
        this.context = context;
        arrangeSequenceView = (ArrangeSequenceContract.ArrangeSequenceView) context;
    }*/

  /*  @Override
    public void getShuffledList(ScienceQuestion scienceQuestion) {

        List<ScienceQuestionChoice> AnswerList = new ArrayList<>();

        if (!scienceQuestion.getUserAnswer().equalsIgnoreCase("")) {
            String[] ansIds = scienceQuestion.getUserAnswer().split(",");
            for (int i = 0; i < ansIds.length; i++) {
                if (ansIds[i].equalsIgnoreCase(scienceQuestion.getMatchingNameList().get(i).getQcid()))
                    AnswerList.add(scienceQuestion.getMatchingNameList().get(i));
            }
        }
        List<ScienceQuestionChoice> shuffledList = new ArrayList<>();
        List<ScienceQuestionChoice> pairList = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        Log.d("wwwwwwwwwww", pairList.size() + "");
        if (!pairList.isEmpty()) {
          *//*  for (int p = 0; p < pairList.size(); p++) {
                list1.add(pairList.get(p).getChoicename());
            }*//*

            if (scienceQuestion.getMatchingNameList() == null) {
                shuffledList.clear();

                shuffledList.addAll(pairList);
                Collections.shuffle(shuffledList);
                Collections.shuffle(shuffledList);
            } else {
                if (AnswerList.size() > 0)
                    shuffledList.addAll(AnswerList);
                else {
                    shuffledList = scienceQuestion.getMatchingNameList();
                    Collections.shuffle(shuffledList);
                }

            }
            arrangeSequenceView.setShuffledList(shuffledList);
        }
    }*/
}
