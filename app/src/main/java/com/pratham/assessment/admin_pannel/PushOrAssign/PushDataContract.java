package com.pratham.assessment.admin_pannel.PushOrAssign;

/**
 * Created by Anki on 12/10/2018.
 */

public interface PushDataContract {
    interface PushDataPresenter {
        void createJsonForTransfer();

    }

    interface PushDataView {
        void finishActivity();
    }
}
