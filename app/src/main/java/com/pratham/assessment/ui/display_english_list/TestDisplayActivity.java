package com.pratham.assessment.ui.display_english_list;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.progress_layout.ProgressLayout;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.EventMessage;
import com.pratham.assessment.domain.Modal_FileDownloading;
import com.pratham.assessment.ui.certificate.CertificateActivity;
import com.pratham.assessment.ui.profile.ProfileActivity;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestDisplayActivity extends BaseActivity implements TestClicked, TestDiaplayContract.TestDisplayView {

    TestDiaplayContract.TestDisplayPresenter presenter;

    @BindView(R.id.rl_Profile)
    RelativeLayout rl_Profile;
    @BindView(R.id.btn_Profile)
    ImageButton btn_Profile;


    String supervisorID;
    Dialog dialog, errorDialog;

    ProgressLayout progressLayout;
    TextView dialog_file_name;
    public Dialog downloadDialog;
    int tempDownloadPos;
    String ResId, resName, downloadNodeId, jsonNodeName;

    private RecyclerView recyclerView;
    TestDisplayAdapter testDisplayAdapter;
    public static List<ContentTable> gameWebViewList;
    static String sdCardPath;
    String nodeId, level;
    ArrayList<ContentTable> pos = new ArrayList<>();
    List<ContentTable> downloadedContentTableList;
    public List<ContentTable> assessmentGameList;
    List<ContentTable> ContentTableList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_assessment);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rl_Profile.setVisibility(View.VISIBLE);
        BackupDatabase.backup(this);
        sdCardPath = Assessment_Constants.ext_path;
        nodeId = getIntent().getStringExtra("nodeId");
       // Assessment_Constants.currentsupervisorID = getIntent().getStringExtra("supervisorID");
        recyclerView = (RecyclerView) findViewById(R.id.choose_assessment_recycler);
        presenter = new TestDisplayPresenter(TestDisplayActivity.this, this);
        gameWebViewList = new ArrayList<>();
        ContentTableList = new ArrayList<>();

        testDisplayAdapter = new TestDisplayAdapter(this, ContentTableList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(15), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(testDisplayAdapter);

        presenter.addNodeIdToList(nodeId);
        presenter.getListData();

    }

    @Override
    public void clearContentList() {
        ContentTableList.clear();
    }

    @Override
    public void addContentToViewList(List<ContentTable> contentTable) {
        ContentTableList.addAll(contentTable);
    }

    @Override
    public void notifyAdapter() {
        Collections.sort(ContentTableList, new Comparator<ContentTable>() {
            @Override
            public int compare(ContentTable o1, ContentTable o2) {
                return o1.getNodeId().compareTo(o2.getNodeId());
            }
        });
        testDisplayAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_Profile, R.id.rl_Profile})
    public void gotoProfileActivity() {
//        ButtonClickSound.start();
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void showDownloaded(int pos) {
        ContentTableList.get(pos).setIsDownloaded("true");
        ContentTableList.get(pos).setOnSDCard(false);
        testDisplayAdapter.notifyItemChanged(pos, ContentTableList.get(pos));
    }

    @Override
    public void showNoDataDownloadedDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView tv_title = dialog.findViewById(R.id.dia_title);
        Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);
        dialog.show();
        tv_title.setText("Connect To Internet");
        exit_btn.setText("BYE");
        restart_btn.setVisibility(View.GONE);

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onTestClicked(int position, final String clickedNodeId) {
        try {
            Log.d("onCardClick", "position: " + position);
            Log.d("onCardClick", "clickedNodeId: " + clickedNodeId);
            level = ContentTableList.get(position).getNodeDesc();
            String CertiTitle = ContentTableList.get(position).getNodeTitle();
            gameWebViewList.clear();
            Intent intent = new Intent(TestDisplayActivity.this, CertificateActivity.class);
            intent.putExtra("nodeId", clickedNodeId);
            intent.putExtra("CertiTitle", CertiTitle);
            intent.putExtra("display", "text");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestDownloadClicked(int position, String nodeId, String nodeName) {
        downloadNodeId = nodeId;
        jsonNodeName = "assessment_" + nodeName;
        resName = ContentTableList.get(position).getNodeTitle();
        tempDownloadPos = position;

        if (Assessment_Utility.isDataConnectionAvailable(TestDisplayActivity.this))
            presenter.downloadResource(downloadNodeId);
        else
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.endTestSession();
                startActivity(new Intent(TestDisplayActivity.this, ChooseLevelActivity.class));
                dialog.dismiss();
                //Assessment_Constants.supervisedAssessment = false;
                finish();
            }
        });
        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.endTestSession();
                Intent i = new Intent(TestDisplayActivity.this, TestTypeActivity.class);
                dialog.dismiss();
                finish();
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageReceived(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(Assessment_Constants.FILE_DOWNLOAD_STARTED)) {
                resourceDownloadDialog(message.getModal_fileDownloading());
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.FILE_DOWNLOAD_UPDATE)) {
                if (progressLayout != null)
                    progressLayout.setCurProgress(message.getModal_fileDownloading().getProgress());
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.FILE_DOWNLOAD_ERROR)) {
                downloadDialog.dismiss();
                showDownloadErrorDialog();
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.FILE_DOWNLOAD_COMPLETE)) {
                if (downloadDialog != null) {
                    downloadDialog.dismiss();
                    resName = "";
                    Log.d("DW_COMPLETE", "messageReceived:JSON_NAME " + jsonNodeName + ".json");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.fetchDownloadedJsonData(jsonNodeName + ".json",tempDownloadPos);
                        }
                    }, (long) (100));
                }
            }

        }
    }

    private void resourceDownloadDialog(Modal_FileDownloading modal_fileDownloading) {
        downloadDialog = new Dialog(this);
        downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        downloadDialog.setContentView(R.layout.dialog_file_downloading);
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.show();
        progressLayout = downloadDialog.findViewById(R.id.dialog_progressLayout);
        dialog_file_name = downloadDialog.findViewById(R.id.dialog_file_name);
        dialog_file_name.setText("" + resName);
        progressLayout.setCurProgress(modal_fileDownloading.getProgress());
    }

    private void showDownloadErrorDialog() {
        errorDialog = new Dialog(this);
        errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.setContentView(R.layout.dialog_file_error_downloading);
        errorDialog.setCanceledOnTouchOutside(false);
        errorDialog.show();
        Button ok_btn = errorDialog.findViewById(R.id.dialog_error_btn);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + avatar) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + avatar) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}