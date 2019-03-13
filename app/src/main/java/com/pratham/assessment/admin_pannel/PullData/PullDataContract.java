package com.pratham.assessment.admin_pannel.PullData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PEF on 19/11/2018.
 */

public interface PullDataContract {
    public interface PullDataView {
        public void showStatesSpinner(String[] states);

        public void showProgressDialog(String msg);

        public void shoConfermationDialog(int crlListCnt, int studentListcnt, int groupListCnt, int villageIDListCnt);

        public void closeProgressDialog();

        public void clearBlockSpinner();

        public void clearStateSpinner();

        public void showBlocksSpinner(List blocks);

        public void showVillageDialog(List villageList);

        public void disableSaveButton();

        public void enableSaveButton();

        public void showErrorToast();

        public void openLoginActivity();


        void showNoConnectivity();
    }

    public interface PullDataPresenter {
        public void loadSpinner();

        public void proccessVillageData(String respnce);

        public void loadBlockSpinner(int pos, String selectedprogram);

        public void downloadStudentAndGroup(ArrayList<String> villageIDList);

        public void saveData();

        public void clearLists();

        public void onSaveClick();

        void checkConnectivity();
    }
}
