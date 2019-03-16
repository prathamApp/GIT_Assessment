package com.pratham.assessment.ui.certificate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.domain.CertificateModelClass;
import com.pratham.assessment.ui.content_player.WebViewActivity;
import com.pratham.assessment.utilities.Assessment_Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pratham.assessment.utilities.Assessment_Utility.dpToPx;


public class CertificateActivity extends BaseActivity implements CertificateContract.CertificateView , CertificateClicked, AdapterView.OnItemSelectedListener {

/*
    @BindView(R.id.btn_english)
    Button btn_english;
    @BindView(R.id.btn_hindi)
    Button btn_hindi;
    @BindView(R.id.btn_marathi)
    Button btn_marathi;
*/
    @BindView(R.id.lang_certi_spinner)
    Spinner lang_certi_spinner;
    @BindView(R.id.iv_photo)
    ImageView iv_photo;

    @BindView(R.id.iv_certificate)
    ImageView iv_certificate;
    @BindView(R.id.rl_supervisedby)
    RelativeLayout rl_supervisedby;

    @BindView(R.id.tv_studentName)
    TextView tv_studentName;
    @BindView(R.id.tv_level)
    TextView tv_certi_level;
    @BindView(R.id.tv_supervisor_name)
    TextView tv_supervisor_name;

    CertificateContract.CertificatePresenter presenter;

    CertificateAdapter certificateAdapter;
    private RecyclerView recyclerView;
    List<CertificateModelClass> ContentTableList;
    JSONArray certiData;
    int clicked_Pos = 0;
    static String certificateLanguage;
    String level_lbl = "", certificate_lbl = "", supervisorName_lbl, supervisorPhoto;
    String allCodes[], nodeId, CertiTitle, certiMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nodeId = getIntent().getStringExtra("nodeId");
        CertiTitle = getIntent().getStringExtra("CertiTitle");
        certiMode = getIntent().getStringExtra("display");
        tv_certi_level.setText(CertiTitle);

        certificateLanguage = "English";

        ContentTableList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.assessment_recycler);
        certificateAdapter = new CertificateAdapter(this, ContentTableList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10,this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(certificateAdapter);
        presenter = new CertificatePresenter(CertificateActivity.this, this);

        presenter.getStudentName();

/*        if (Assessment_Constants.supervisedAssessment)
            presenter.getSupervisorData(certiMode);
        else {*/
            rl_supervisedby.setVisibility(View.GONE);
//        }

        certiData = presenter.fetchAssessmentList();

        if (!certiMode.equalsIgnoreCase("display")) {
             presenter.proceed(certiData,nodeId);
        } else {
            if (CertiTitle.equalsIgnoreCase("0"))
                tv_certi_level.setText("Beginner");
            else if (CertiTitle.equalsIgnoreCase("1"))
                tv_certi_level.setText("Intermediate");
            else if (CertiTitle.equalsIgnoreCase("2"))
                tv_certi_level.setText("Expert");

/*            if(!assessmentProfile.getDeviceIDa().equalsIgnoreCase("na")){
                rl_supervisedby.setVisibility(View.VISIBLE);
                presenter.getSupervisorData(certiMode);
            }else
                rl_supervisedby.setVisibility(View.GONE);

            presenter.fillAdapter(assessmentProfile, certiData);*/
        }
/*        if(Assessment_Constants.GROUP_LOGIN){

        }*/

        lang_certi_spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, getResources().getStringArray(R.array.certificate_Languages));
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        lang_certi_spinner.setAdapter(dataAdapter);

    }

    @Override
    public void setStudentName(String studName) {
        tv_studentName.setText("" + studName);
    }

    @Override
    public void addContentToViewList(CertificateModelClass contentTable) {
        ContentTableList.add(contentTable);
    }

    @Override
    public void doubleQuestionCheck() {
        for(int i=0;i<ContentTableList.size(); i++){
            for(int j =i+1 ; j<ContentTableList.size(); j++){
                if(ContentTableList.get(i).getCertiCode().equalsIgnoreCase(ContentTableList.get(j).getCertiCode())) {
                    ContentTableList.get(i).setCodeCount(ContentTableList.get(i).getCodeCount()+1);
                }
            }
        }
    }

    @Override
    public void initializeTheIndex() {
        certificateAdapter.initializeIndex();
    }

    @Override
    public void notifyAdapter() {
        certificateAdapter.notifyDataSetChanged();
    }

/*    @Override
    public void setSupervisorData(String sName, String sImage) {
        supervisorName_lbl = sName;
        supervisorPhoto = sImage;
        Bitmap[] bitmap = new Bitmap[]{BitmapFactory.decodeFile(supervisorPhoto)};
        iv_photo.setImageBitmap(bitmap[0]);
        tv_supervisor_name.setText(supervisorName_lbl);

    }*/

/*    public float[] getAllPercentages() {
        float starRatings[];
        starRatings = new float[certificateModelClassList.size()];
        for (int i = 0; i < certificateModelClassList.size(); i++) {
            starRatings[i] = getStarRating(((float) certificateModelClassList.get(i).getScoredMarks() / (float) certificateModelClassList.get(i).getTotalMarks()) * 100);
        }
        return starRatings;
    }
    public String[] getAllCodes() {
        String allCodes[];
        allCodes = new String[certificateModelClassList.size()];
        for (int i = 0; i < certificateModelClassList.size(); i++) {
            allCodes[i] = certificateModelClassList.get(i).getCertiCode();
        }
        return allCodes;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        boolean testGiven = true;

        if (!certiMode.equalsIgnoreCase("display")) {
            JSONObject jsonObjectAssessment = new JSONObject();
            for (int i = 0; i < ContentTableList.size(); i++) {
                try {
                    if (ContentTableList.get(i).isAsessmentGiven()) {
                        jsonObjectAssessment.put("CertCode"+i+"_" + ContentTableList.get(i).getCertiCode(), "" + ContentTableList.get(i).getStudentPercentage());
                    } else {
                        testGiven = false;
                        break;
                    }
                    //question
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (testGiven) {
                presenter.recordTestData(jsonObjectAssessment,CertiTitle);
            }
        }
    }

    @Override
    public void onCertificateClicked(int position, String nodeId) { }

    @Override
    public void onCertificateOpenGame(int position, String nodeId) {
        String resPath = null;
        clicked_Pos = position;
        String gameID = ContentTableList.get(position).getResourceId();
        //TODO WebViewGame Level
      //  gameLevel = ContentTableList.get(position).getNodeAge();
//        if (Assessment_Constants.SMART_PHONE && !Assessment_Constants.SD_CARD_Content)
            resPath = AssessmentApplication.assessPath+ Assessment_Constants.GAME_PATH+ ContentTableList.get(position).getResourcePath();
//        else if (Assessment_Constants.SMART_PHONE && Assessment_Constants.SD_CARD_Content)
//            resPath = Assessment_Constants.ext_path + Assessment_Constants.GAME_PATH + ContentTableList.get(position).getResourcePath();
        File file = new File(resPath);
        Uri path = Uri.fromFile(file);

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("resPath", path.toString());
        intent.putExtra("resId", gameID);
        intent.putExtra("mode", "test");
        intent.putExtra("gameLevel", ContentTableList.get(position).getNodeAge());

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String cCode = data.getStringExtra("cCode");
                int tMarks = data.getIntExtra("tMarks", 0);
                int sMarks = data.getIntExtra("sMarks", 0);

                try {
                    if (cCode.equalsIgnoreCase(ContentTableList.get(clicked_Pos).getCertiCode())) {
                        ContentTableList.get(clicked_Pos).setAsessmentGiven(true);
                        ContentTableList.get(clicked_Pos).setTotalMarks(tMarks);
                        ContentTableList.get(clicked_Pos).setScoredMarks(sMarks);
                        float perc = ((float) sMarks / (float) tMarks) * 100;
                        ContentTableList.get(clicked_Pos).setStudentPercentage("" + perc);
                        ContentTableList.get(clicked_Pos).setCertificateRating(presenter.getStarRating(perc));

                        certificateAdapter.notifyItemChanged(clicked_Pos, ContentTableList.get(clicked_Pos));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        certificateLanguage = parent.getItemAtPosition(position).toString();
        level_lbl = "Level ";
        iv_certificate.setImageResource(R.drawable.certifcate_eng);
        certificateAdapter.initializeIndex();
        certificateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        iv_certificate.setImageResource(R.drawable.certifcate_eng);
        certificateLanguage = "English";
        certificateAdapter.initializeIndex();
        certificateAdapter.notifyDataSetChanged();
    }

}