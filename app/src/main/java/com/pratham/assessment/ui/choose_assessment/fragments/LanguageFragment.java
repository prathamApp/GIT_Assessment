package com.pratham.assessment.ui.choose_assessment.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.ui.choose_assessment.ChooseAssessmentActivity;
import com.pratham.assessment.constants.APIs;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

@EFragment(R.layout.fragment_language)
public class LanguageFragment extends Fragment {
    List<AssessmentLanguages> assessmentLanguagesList;

    ProgressDialog progressDialog;

    @ViewById(R.id.rv_choose_lang)
    RecyclerView rvLanguage;

    public LanguageFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        assessmentLanguagesList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());

        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getLanguageData();
        } else {
            assessmentLanguagesList = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getAllLangs();
            if (assessmentLanguagesList.size() <= 0) {
                progressDialog.dismiss();
                ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
//                ((ChooseAssessmentActivity) getActivity()).toggle_btn.setVisibility(View.VISIBLE);

//                getActivity().getSupportFragmentManager().popBackStackImmediate();
                Toast.makeText(getActivity(), R.string.connect_to_internet_to_download_languages, Toast.LENGTH_SHORT).show();
            } else setLanguageRecyclerView();
        }

    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assessmentLanguagesList = new ArrayList<>();
     */
/*   assessmentLanguagesList = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getAllLangs();
        if (assessmentLanguagesList.size() <= 0) {
            getLanguageData();
        } else setLanguageRecyclerView();
*//*

    }
*/

    private void setLanguageRecyclerView() {
        LanguageAdapter adapter = new LanguageAdapter(getActivity(), assessmentLanguagesList);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        if (rvLanguage != null) {
            rvLanguage.setLayoutManager(linearLayoutManager);
            rvLanguage.setAdapter(adapter);
        }
    }

/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language, container, false);
    }*/


    private void getLanguageData() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentLanguageAPI)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            progressDialog.dismiss();

                            for (int i = 0; i < response.length(); i++) {
                                AssessmentLanguages assessmentLanguages = new AssessmentLanguages();
                                assessmentLanguages.setLanguageid(response.getJSONObject(i).getString("languageid"));
                                assessmentLanguages.setLanguagename(response.getJSONObject(i).getString("languagename"));
                                assessmentLanguagesList.add(assessmentLanguages);
                            }
                            AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().insertAllLanguages(assessmentLanguagesList);
                            setLanguageRecyclerView();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), R.string.error_in_loading_check_internet_connection, Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperPatternDao().deletePaperPatterns();
                        ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                        ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
//                        ((ChooseAssessmentActivity) getActivity()).toggle_btn.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager().popBackStackImmediate();

                        progressDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }

  /*  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity());

        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getLanguageData();
        } else {
            assessmentLanguagesList = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getAllLangs();
            if (assessmentLanguagesList.size() <= 0) {
                progressDialog.dismiss();
                ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                ((ChooseAssessmentActivity) getActivity()).toggle_btn.setVisibility(View.VISIBLE);

//                getActivity().getSupportFragmentManager().popBackStackImmediate();
                Toast.makeText(getActivity(), "Connect to internet to download languages", Toast.LENGTH_SHORT).show();
            } else setLanguageRecyclerView();
        }
    }*/
}
