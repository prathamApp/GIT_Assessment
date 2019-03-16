package com.pratham.assessment.ui.display_english_list;

import com.pratham.assessment.domain.ContentTable;

import java.util.List;

/**
 * Created by Ameya on 23-Nov-17.
 */

public interface TestDiaplayContract {

    interface TestDisplayView{
        void clearContentList();
        void addContentToViewList(List<ContentTable> contentTable);
        void notifyAdapter();
        void showNoDataDownloadedDialog();
        void showDownloaded(int pos);
    }

    interface TestDisplayPresenter {
        void getListData();

        void downloadResource(String downloadNodeId);

        void addNodeIdToList(String nodeId);

        boolean removeLastNodeId();

        void starContentEntry(String nodeId, String s);

        void fetchDownloadedJsonData(String jsonName, int pos);

        void endTestSession();
    }

}
