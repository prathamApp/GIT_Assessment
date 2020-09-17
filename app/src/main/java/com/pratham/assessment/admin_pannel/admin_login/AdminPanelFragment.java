package com.pratham.assessment.admin_pannel.admin_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.pratham.assessment.R;
import com.pratham.assessment.admin_pannel.PullData.PullDataFragment;
import com.pratham.assessment.admin_pannel.PullData.PullDataFragment_;
import com.pratham.assessment.admin_pannel.PushOrAssign.PushOrAssignFragment;
import com.pratham.assessment.admin_pannel.PushOrAssign.PushOrAssignFragment_;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

/**
 * Created by PEF on 19/11/2018.
 */
@EFragment(R.layout.admin_panel_login)
public class AdminPanelFragment extends Fragment implements AdminPanelContract.AdminPanelView {
    AdminPanelContract.AdminPanelPresenter adminPanelPresenter;
    @ViewById(R.id.userName)
    android.support.design.widget.TextInputEditText userNameET;

    @ViewById(R.id.password)
    android.support.design.widget.TextInputEditText passwordET;

    @AfterViews
    public void init() {
        Assessment_Utility.HideInputKeypad(getActivity());
       /*userNameET.setText("pravinthorat");
        passwordET.setText("pratham123");*/
        /*  userNameET.setText("admin");
        passwordET.setText("admin");*/
       /* userNameET.setText("mh_reena");
        passwordET.setText("pratham");*/
        userNameET.setText("");
        passwordET.setText("");
        adminPanelPresenter = new AdminPanelPresenter(getActivity(), this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /*  @Nullable
      @Override
      public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          return inflater.inflate(R.layout.admin_panel_login, container, false);
      }

      @Override
      public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          ButterKnife.bind(this, view);
          Assessment_Utility.HideInputKeypad(getActivity());
         *//* userNameET.setText("pravinthorat");
        passwordET.setText("pratham123");*//*
     *//*  userNameET.setText("admin");
        passwordET.setText("admin");*//*
        userNameET.setText("mh_reena");
        passwordET.setText("pratham");
      *//*  userNameET.setText("");
        passwordET.setText("");*//*
        adminPanelPresenter = new AdminPanelPresenter(getActivity(), this);
    }
*/
    @Click(R.id.btn_login)
    public void loginCheck() {
        adminPanelPresenter.checkLogin(getUserName(), getPassword());
        userNameET.getText().clear();
        passwordET.getText().clear();
    }

    @Click(R.id.btn_clearData)
    public void clearData() {
        /*AlertDialog clearDataDialog = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Clear Data")
                .setMessage("Are you sure you want to clear everything ?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        adminPanelPresenter.clearData();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        clearDataDialog.show();
        clearDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
*/
        userNameET.getText().clear();
        passwordET.getText().clear();

    }

    @Override
    public String getUserName() {
        String userName = userNameET.getText().toString();
        return userName.trim();
    }

    @Override
    public String getPassword() {
        String password = passwordET.getText().toString();
        return password.trim();
    }

    @Override
    public void openPullDataFragment() {
        Assessment_Utility.showFragment(getActivity(), new PullDataFragment_(), R.id.frame_attendance,
                null, PullDataFragment.class.getSimpleName());
    }

    @Override
    public void onLoginFail() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Invalid Credentials");
        alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userNameET.setText("");
                passwordET.setText("");
                userNameET.requestFocus();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onLoginSuccess() {
        /* if (!startAssessment)*/
        Assessment_Utility.showFragment(getActivity(), new PushOrAssignFragment_(), R.id.frame_attendance,
                null, PushOrAssignFragment.class.getSimpleName());
        // else startActivity(new Intent(getActivity(), MenuActivity.class));
            /*Assessment_Utility.showFragment(getActivity(), new PushOrAssignFragment(), R.id.frame_attendance,
                    null, PushOrAssignFragment.class.getSimpleName());
*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void onDataClearToast() {
        userNameET.setText("");
        passwordET.setText("");
        Toast.makeText(getActivity(), R.string.data_cleared_successfully, Toast.LENGTH_SHORT).show();
    }
}
