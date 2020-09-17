package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.match_the_pair;

import android.content.Context;

import org.androidannotations.annotations.EBean;

@EBean
public class MatchThePairPresenter implements MatchThePairContract.MatchThePairPresenter {
    Context context;
    MatchThePairContract.MatchThePairView matchThePairView;

    public MatchThePairPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void setView(MatchThePairContract.MatchThePairView view) {
        matchThePairView = view;
    }


}
