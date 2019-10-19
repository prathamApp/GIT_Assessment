package com.pratham.assessment.admin_pannel.admin_login;

import android.content.Context;

import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.Status;


/**
 * Created by PEF on 19/11/2018.
 */

public class AdminPanelPresenter implements AdminPanelContract.AdminPanelPresenter {
    AdminPanelContract.AdminPanelView adminPanelView;
    Context context;

    public AdminPanelPresenter(Context context, AdminPanelContract.AdminPanelView adminPanelView) {
        this.adminPanelView = adminPanelView;
        this.context = context;
    }

    @Override
    public void checkLogin(String userName, String password) {
        // if user name and password are admin then navigate to Download activity otherWise admin activity

        if (userName.equals("admin") && password.equals("admin")) {
            adminPanelView.openPullDataFragment();
        } else {
            // assign push logic
            Crl loggedCrl = AppDatabase.getDatabaseInstance(context).getCrlDao().checkUserValidation(userName, password);
            if (loggedCrl != null) {
                adminPanelView.onLoginSuccess();
                /*Status status = new Status();
                status.setStatusKey("CRLID");
                status.setValue(crlId);*/
                String crlId = loggedCrl.getCRLId();
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("CRLID", crlId);
            } else {
                //userNAme and password may be wrong
                adminPanelView.onLoginFail();
            }
        }
    }

    @Override
    public void clearData() {
      /*  BaseActivity.villageDao.deleteAllVillages();
        BaseActivity.groupDao.deleteAllGroups();
        BaseActivity.studentDao.deleteAllStudents();
        BaseActivity.crLdao.deleteAllCRLs();*/
        AppDatabase.getDatabaseInstance(context).getVillageDao().deleteAllVillages();
        AppDatabase.getDatabaseInstance(context).getGroupsDao().deleteAllGroups();
        AppDatabase.getDatabaseInstance(context).getStudentDao().deleteAll();
        AppDatabase.getDatabaseInstance(context).getCrlDao().deleteAll();
        adminPanelView.onDataClearToast();

    }
}
