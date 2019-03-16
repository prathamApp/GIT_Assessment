package com.pratham.assessment.admin_pannel.PushOrAssign;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.ui.login.MainActivity;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;

import butterknife.ButterKnife;

public class PushDataActivity extends BaseActivity implements PushDataContract.PushDataView {
    PushDataPresenterImpl presenter;
    boolean isConnectedToRasp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_data);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new PushDataPresenterImpl(this);
        checkConnectivity();
       /* if (isConnectedToRasp) presenter.pushRaspData();
        else*/
        presenter.createJsonForTransfer();

    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*@OnClick(R.id.btn_self_push)
      public void onSelfPushClick(){

    }*/
    public void checkConnectivity() {
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
            //callOnlineContentAPI(contentList, parentId);
        } else if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                //  if (FastSave.getInstance().getString(PD_Constant.FACILITY_ID, "").isEmpty())
                isConnectedToRasp = checkConnectionForRaspberry();
                //callKolibriAPI(contentList, parentId);
            } else {
                isConnectedToRasp = false;
                //callOnlineContentAPI(contentList, parentId);
            }
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkConnectionForRaspberry() {
        boolean isRaspberry = false;
        if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                try {
                    isRaspberry = true;
                    /*JSONObject object = new JSONObject();
                    object.put("username", "pratham");
                    object.put("password", "pratham");
                    new PD_ApiRequest(context, ContentPresenterImpl.this)
                            .getacilityIdfromRaspberry(PD_Constant.FACILITY_ID, PD_Constant.RASP_IP + "/api/session/", object);*/
                } catch (Exception e) {
                    isRaspberry = false;
                    e.printStackTrace();
                }
            }
        } else isRaspberry = false;
        return isRaspberry;
    }
}
