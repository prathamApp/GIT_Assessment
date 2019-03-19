package com.pratham.assessment.ui.choose_assessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.ui.display_english_list.TestDisplayActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pratham.assessment.utilities.Assessment_Utility.dpToPx;

public class ChooseAssessmentActivity extends BaseActivity implements
        ChoseAssessmentClicked, ChooseAssessmentContract.ChooseAssessmentView {

    ChooseAssessmentContract.ChooseAssessmentPresenter presenter;

    @BindView(R.id.rl_Profile)
    RelativeLayout rl_Profile;

    private RecyclerView recyclerView;
    List<ContentTable> contentTableList;
    ChooseAssessmentAdapter chooseAssessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_assessment);
        ButterKnife.bind(this);

        rl_Profile.setVisibility(View.GONE);

        presenter = new ChooseAssessmentPresenter(ChooseAssessmentActivity.this, this);
        contentTableList = new ArrayList<>();


        recyclerView = (RecyclerView) findViewById(R.id.choose_assessment_recycler);
        chooseAssessAdapter = new ChooseAssessmentAdapter(this, contentTableList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseAssessAdapter);

        presenter.copyListData();
    }

    @Override
    public void clearContentList() {
        contentTableList.clear();
    }

    @Override
    public void addContentToViewList(ContentTable contentTable) {

        contentTableList.add(contentTable);

        Collections.sort(contentTableList, new Comparator<ContentTable>() {
            @Override
            public int compare(ContentTable o1, ContentTable o2) {
                return o1.getNodeId().compareTo(o2.getNodeId());
            }
        });
        Log.d("sorted", contentTableList.toString());
    }

    @Override
    public void notifyAdapter() {
        chooseAssessAdapter.notifyDataSetChanged();
        /*if (COS_Utility.isDataConnectionAvailable(ChooseLevelActivity.this))
                    getAPIContent(COS_Constants.INTERNET_DOWNLOAD, COS_Constants.INTERNET_DOWNLOAD_API);
                else {
                    levelAdapter.notifyDataSetChanged();
         }*/
    }


    @Override
    public void assessmentClicked(int position, String nodeId) {

        if (nodeId.equalsIgnoreCase("1300")) {
            startActivity(new Intent(ChooseAssessmentActivity.this, ECEActivity.class));
        } else {
            Intent intent = new Intent(ChooseAssessmentActivity.this, TestDisplayActivity.class);
            intent.putExtra("nodeId", nodeId);
            startActivity(intent);

        }
//        Toast.makeText(this, "assessmentClicked : " + position + "  /  " + nodeId, Toast.LENGTH_SHORT).show();
    }
}
