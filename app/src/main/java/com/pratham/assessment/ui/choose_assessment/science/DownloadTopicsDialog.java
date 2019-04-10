package com.pratham.assessment.ui.choose_assessment.science;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.admin_pannel.PullData.VillageSelectListener;
import com.pratham.assessment.domain.AssessmentToipcsModal;
import com.pratham.assessment.domain.Village;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DownloadTopicsDialog extends Dialog {

    @BindView(R.id.txt_clear_changes)
    TextView clear_changes;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.txt_message)
    TextView topics;
    @BindView(R.id.flowLayout)
    GridLayout flowLayout;

    Context context;
    List<AssessmentToipcsModal> villageList;
    List<CheckBox> checkBoxes = new ArrayList<>();
    TopicSelectListener topicSelectListener;

    public DownloadTopicsDialog(@NonNull Context context, TopicSelectListener topicSelectListener, List tempList) {
        super(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        this.villageList = tempList;
        this.context = context;
        this.topicSelectListener = topicSelectListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_village_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        topics.setText("Select Topics");
        for (int i = 0; i < villageList.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(villageList.get(i).getTopicname());
            checkBox.setTag(villageList.get(i).getTopicid());
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(param);
            flowLayout.addView(checkBox);
            checkBoxes.add(checkBox);
        }
    }


    @OnClick(R.id.btn_close)
    public void closeDialog() {
        dismiss();
    }

    @OnClick(R.id.txt_clear_changes)
    public void clearChanges() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setChecked(false);
        }
    }

    @OnClick(R.id.txt_ok)
    public void ok() {
        ArrayList<String> villageIDList = new ArrayList();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                villageIDList.add(checkBoxes.get(i).getTag().toString());
            }
        }
        topicSelectListener.getSelectedItems(villageIDList);
        dismiss();
    }

}

