package com.pratham.assessment.ui.display_english_list;

/**
 * Created by Ameya on 23-Nov-17.
 */

public interface TestClicked {

    public void onTestClicked(int position, String nodeId);

    public void onTestDownloadClicked(int position, String nodeId, String nodeName);
}
