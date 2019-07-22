package com.pratham.assessment.ui.choose_assessment.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.utilities.APIs;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LanguageFragment extends Fragment {
    List<AssessmentLanguages> assessmentLanguagesList;


    @BindView(R.id.rv_choose_lang)
    RecyclerView rvLanguage;

    public LanguageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assessmentLanguagesList = new ArrayList<>();
     /*   assessmentLanguagesList = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getAllLangs();
        if (assessmentLanguagesList.size() <= 0) {
            getLanguageData();
        } else setLanguageRecyclerView();
*/
    }

    private void setLanguageRecyclerView() {
        LanguageAdapter adapter = new LanguageAdapter(getActivity(), assessmentLanguagesList);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvLanguage.setLayoutManager(linearLayoutManager);
        rvLanguage.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language, container, false);
    }


    private void getLanguageData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..");
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
                        Toast.makeText(getActivity(), "Error in loading..Check internet connection.", Toast.LENGTH_SHORT).show();
                        AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperPatternDao().deletePaperPatterns();

                        progressDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        assessmentLanguagesList = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getAllLangs();
        if (assessmentLanguagesList.size() <= 0) {
            getLanguageData();
        } else setLanguageRecyclerView();

    }
}
