package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.arrange_sequence;

import android.content.Context;
import android.util.Log;

import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_without_options.FIB_WithoutOption_Contract;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EBean
public class ArrangeSeqPresenter implements ArrangeSequenceContract.ArrangeSeqPresenter {
    Context context;
    ArrangeSequenceContract.ArrangeSeqView view;

    public ArrangeSeqPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void setView(ArrangeSequenceContract.ArrangeSeqView view) {
        this.view = view;
    }

    @Override
    public void getShuffledList(ScienceQuestion scienceQuestion) {
        List<ScienceQuestionChoice> AnswerList = new ArrayList<>();


        if (!scienceQuestion.getUserAnswer().equalsIgnoreCase("")) {
            String[] ansIds = scienceQuestion.getUserAnswer().split(",");
            for (int i = 0; i < ansIds.length; i++) {
                if (ansIds[i].equalsIgnoreCase(scienceQuestion.getMatchingNameList().get(i).getQcid()))
                    AnswerList.add(scienceQuestion.getMatchingNameList().get(i));
            }

        }

        List list1 = new ArrayList();
        List<ScienceQuestionChoice> shuffledList = new ArrayList<>();
//        List<ScienceQuestionChoice> pairList = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        List<ScienceQuestionChoice> pairList = scienceQuestion.getLstquestionchoice();
        Log.d("wwwwwwwwwww", pairList.size() + "");
        if (!pairList.isEmpty()) {
/*  for (int p = 0; p < pairList.size(); p++) {
                list1.add(pairList.get(p).getChoicename());
            }*/


            if (scienceQuestion.getMatchingNameList() == null) {
                shuffledList.clear();

                shuffledList.addAll(pairList);
                if (shuffledList.size() > 1)
                    while (shuffledList.equals(pairList)) {
                        Collections.shuffle(shuffledList);
                    }
//                Collections.shuffle(shuffledList);
            } else {
                if (AnswerList.size() > 0)
                    shuffledList.addAll(AnswerList);
                else {
                    shuffledList = scienceQuestion.getMatchingNameList();
                    Collections.shuffle(shuffledList);
                }

            }

        }
        view.setShuffledList(shuffledList);

    }
}