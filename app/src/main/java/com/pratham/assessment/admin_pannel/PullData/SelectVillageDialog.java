package com.pratham.assessment.admin_pannel.PullData;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.pratham.assessment.R;
import com.pratham.assessment.domain.Village;


import java.util.ArrayList;
import java.util.List;


public class SelectVillageDialog extends Dialog {

   /* @BindView(R.id.txt_clear_changes)
    TextView clear_changes;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.txt_message)
    TextView txt_message_village;
    @BindView(R.id.flowLayout)
    GridLayout flowLayout;*/

    Context context;
    List<Village> villageList;
    List<CheckBox> checkBoxes = new ArrayList<>();
    VillageSelectListener villageSelectListener;


    public SelectVillageDialog(@NonNull Context context, VillageSelectListener villageSelectListener, List tempList) {
        super(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        this.villageList = tempList;
        this.context = context;
        this.villageSelectListener = villageSelectListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_village_dialog);
        TextView clear_changes = findViewById(R.id.txt_clear_changes);
        ImageButton btn_close = findViewById(R.id.btn_close);
        TextView txt_message_village = findViewById(R.id.txt_message);
        GridLayout flowLayout = findViewById(R.id.flowLayout);
        TextView txt_save = findViewById(R.id.txt_save);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        clear_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < checkBoxes.size(); i++) {
                    checkBoxes.get(i).setChecked(false);
                }
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> villageIDList = new ArrayList();
                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isChecked()) {
                        villageIDList.add(checkBoxes.get(i).getTag().toString());
                    }
                }
                if (villageIDList.size() > 0) {
                    villageSelectListener.getSelectedItems(villageIDList);
                    dismiss();
                } else Toast.makeText(context, R.string.please_select_village, Toast.LENGTH_SHORT).show();
            }
        });


        setCanceledOnTouchOutside(false);
        setCancelable(false);
        txt_message_village.setText("Select Village");
        for (int i = 0; i < villageList.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(villageList.get(i).getVillageName());
            checkBox.setTag(villageList.get(i).getVillageId());
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(param);
            flowLayout.addView(checkBox);
            checkBoxes.add(checkBox);
        }
    }


  /*  @OnClick(R.id.btn_close)
    public void closeDialog() {
        dismiss();
    }*/

   /* @OnClick(R.id.txt_clear_changes)
    public void clearChanges() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setChecked(false);
        }
    }*/

   /* @OnClick(R.id.txt_save)
    public void ok() {
        ArrayList<String> villageIDList = new ArrayList();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                villageIDList.add(checkBoxes.get(i).getTag().toString());
            }
        }
        if (villageIDList.size() > 0) {
            villageSelectListener.getSelectedItems(villageIDList);
            dismiss();
        } else Toast.makeText(context, R.string.please_select_village, Toast.LENGTH_SHORT).show();
    }*/

}

