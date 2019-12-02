package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.AssessmentSubjectsExpandable;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.ExpandableRecyclerAdapter;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.SubjectAdapter;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CertificateSubjectsFragment extends Fragment implements SubjectContract.SubjectView {

    @BindView(R.id.rv_subject)
    RecyclerView rv_subject;
    @BindView(R.id.spinner_certificate_lang)
    Spinner spinner_lang;
    String selectedLang = "english";
    SubjectContract.SubjectPresenter presenter;

    public CertificateSubjectsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_certificate_subjects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new SubjectPresenter(getActivity(), this);

        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            presenter.pullCertificates();
        } else {
            setSubjectToSpinner();
        }

    }

    @Override
    public void setSubjectToSpinner() {
        List<String> languageIds = AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperForPushDao().getAssessmentPapersByUniqueLang(Assessment_Constants.currentStudentID);
        List<String> languages = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getLangList(languageIds);
        if (languages.size() > 0 && languageIds.size() > 0) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, languages);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            spinner_lang.setAdapter(dataAdapter);

            spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (view != null) {
                        TextView lang = (TextView) view;
                        selectedLang = lang.getText().toString();
                        presenter.getSubjectsFromDB(selectedLang);
                    } else presenter.getSubjectsFromDB(selectedLang);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    presenter.getSubjectsFromDB(selectedLang);
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Certificates..", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void setSubjects(final List<AssessmentSubjectsExpandable> subjects) {
        SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(), subjects);
        subjectAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
            }

            @Override
            public void onListItemCollapsed(int position) {
            }
        });

        rv_subject.setAdapter(subjectAdapter);
        rv_subject.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
