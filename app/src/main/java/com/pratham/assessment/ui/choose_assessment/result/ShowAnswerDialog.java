package com.pratham.assessment.ui.choose_assessment.result;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowAnswerDialog extends Dialog {

    @BindView(R.id.rl_ans_options1_result)
    RecyclerView list1;
    @BindView(R.id.rl_ans_options2_result)
    RecyclerView list2;
    @BindView(R.id.ll_multiple_select_result)
    LinearLayout ll_multiple_select;
    @BindView(R.id.ll_match_pair_result)
    LinearLayout ll_match_pair;
    @BindView(R.id.tv_multiple_select_ans)
    TextView multipleSelectAns;
    List<ScienceQuestionChoice> scienceQuestionChoices;

    public ShowAnswerDialog(Context context, List<ScienceQuestionChoice> scienceQuestionChoices) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.layout_answer_dialog);
        this.scienceQuestionChoices = scienceQuestionChoices;
        ButterKnife.bind(this);
        setRecycler();
       /* String[] queArr = new String[scienceQuestionChoices.size()];
        String[] ansArr = new String[scienceQuestionChoices.size()];

        for (int i = 0; i < scienceQuestionChoices.size(); i++) {
            queArr[i] = scienceQuestionChoices.get(i).getChoicename();
            ansArr[i] = scienceQuestionChoices.get(i).getMatchingname();
        }*/
        ll_match_pair.setVisibility(View.VISIBLE);
        ll_multiple_select.setVisibility(View.GONE);
        ResultDialogAdapter adapter = new ResultDialogAdapter(context, scienceQuestionChoices, "que");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        list1.setLayoutManager(linearLayoutManager);
        list1.setAdapter(adapter);
        ResultDialogAdapter adapter1 = new ResultDialogAdapter(context, scienceQuestionChoices, "ans");
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        list2.setLayoutManager(linearLayoutManager1);
        list2.setAdapter(adapter1);

    }

    public ShowAnswerDialog(Context context, List<ScienceQuestionChoice> userAns, String multipleSelect) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.layout_answer_dialog);
        ButterKnife.bind(this);

        ll_match_pair.setVisibility(View.GONE);
        ll_multiple_select.setVisibility(View.VISIBLE);
        StringBuffer ans = new StringBuffer();
        for (int i = 0; i < userAns.size(); i++) {
            ans.append(Html.fromHtml(userAns.get(i).getChoicename()) + ",");
        }
        if (ans.length() > 0)
            ans.setLength(ans.length() - 1);
        Assessment_Utility.setOdiaFont(context, multipleSelectAns);
        multipleSelectAns.setText(ans);

    }

    public ShowAnswerDialog(Context context, String img) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.layout_answer_dialog);
        ButterKnife.bind(this);
        this.dismiss();


    }

    private void setRecycler() {
    }

    public ShowAnswerDialog(Context context, List<ScienceQuestionChoice> userAns, List<ScienceQuestionChoice> scienceQuestionChoice) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.layout_answer_dialog);
        ButterKnife.bind(this);
        ll_match_pair.setVisibility(View.VISIBLE);
        ll_multiple_select.setVisibility(View.GONE);

        ResultDialogAdapter adapter = new ResultDialogAdapter(context, scienceQuestionChoice, "que");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        list1.setLayoutManager(linearLayoutManager);
        list1.setAdapter(adapter);
        ResultDialogAdapter adapter1 = new ResultDialogAdapter(context, userAns, "ans");
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        list2.setLayoutManager(linearLayoutManager1);
        list2.setAdapter(adapter1);

    }

    @OnClick(R.id.btn_ok)
    public void onOk() {
        dismiss();
    }
}
