package com.pratham.assessment.ui.login.qr_scan;

import android.content.Context;
import android.widget.Toast;

public class QRScanPresenter implements QRScanContract.QRScanPresenter {

    Context mContext;
    QRScanContract.QRScanView startMenuView;

    public QRScanPresenter(Context context, QRScanContract.QRScanView startMenu) {
        mContext = context;
        startMenuView = startMenu;
    }

    @Override
    public void displayToast() {
        Toast.makeText(mContext, "In presenter displayToast", Toast.LENGTH_SHORT).show();
    }

}
