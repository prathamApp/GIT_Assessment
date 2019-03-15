package com.pratham.assessment.ui.choose_assessment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.domain.ContentTable;

import java.util.List;

import static com.pratham.assessment.utilities.Assessment_Utility.dpToPx;

public class ChooseAssessmentActivity extends BaseActivity implements
        ChoseAssessmentClicked{

    private RecyclerView recyclerView;
    List<ContentTable> contentTableList;
    ChooseAssessmentAdapter chooseAssessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_assessment);

        recyclerView = (RecyclerView) findViewById(R.id.choose_assessment_recycler);
        chooseAssessAdapter = new ChooseAssessmentAdapter(this, contentTableList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10,this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseAssessAdapter);
    }

    @Override
    public void assessmentClicked(int position, String nodeId) {

    }
}
