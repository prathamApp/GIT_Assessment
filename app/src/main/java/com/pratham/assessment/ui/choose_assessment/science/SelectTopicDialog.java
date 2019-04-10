package com.pratham.assessment.ui.choose_assessment.science;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.AssessmentToipcsModal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectTopicDialog extends Dialog {

    @BindView(R.id.tv_update_topics)
    TextView updateTopics;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.txt_message)
    TextView topics;
   @BindView(R.id.spinner_topic)
    Spinner spinner_topic;

    Context context;
    List<AssessmentToipcsModal> toipcsModalList;
    List<CheckBox> checkBoxes = new ArrayList<>();
    TopicSelectListener topicSelectListener;

    public SelectTopicDialog(@NonNull Context context, TopicSelectListener topicSelectListener, List tempList) {
        super(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        this.toipcsModalList = tempList;
        this.context = context;
        this.topicSelectListener = topicSelectListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_topic_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        topics.setText("Select Topics");
        String[] topicNames=new String[toipcsModalList.size()];
        for (int i=0;i<toipcsModalList.size();i++){
            topicNames[i]=toipcsModalList.get(i).getTopicname();
        }
        spinner_topic.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,topicNames));
      /*  for (int i = 0; i < villageList.size(); i++) {
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
        }*/
    }


    @OnClick(R.id.btn_close)
    public void closeDialog() {
        dismiss();
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

