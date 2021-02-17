package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.AssessmentSubjectsExpandable;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.ExpandableRecyclerAdapter;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.SubjectAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_certificate_subjects)
public class CertificateSubjectsFragment extends Fragment implements SubjectContract.SubjectView {

    @ViewById(R.id.rv_subject)
    RecyclerView rv_subject;
    @ViewById(R.id.spinner_certificate_lang)
    Spinner spinner_lang;
    String selectedLang = "english";
    @Bean(SubjectPresenter.class)
    SubjectContract.SubjectPresenter presenter;

    @AfterViews
    public void init() {
//        presenter = new SubjectPresenter(getActivity(), this);
        presenter.setView(this);
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            presenter.pullCertificates();
        } else {
            setSubjectToSpinner();
        }
    }

    public CertificateSubjectsFragment() {
    }


    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_certificate_subjects, container, false);
    }*/

    /*  @Override
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
  */
    @Override
    public void setSubjectToSpinner() {
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");

        List<String> distinctExamIds = AppDatabase.getDatabaseInstance(getActivity()).getCertificateKeywordRatingDao().getQuestionsByExamIdSubId(currentStudentID);
        List<String> examIdsForPracticeMode = new ArrayList<>();

        for (int i = 0; i < distinctExamIds.size(); i++) {
            AssessmentPaperPattern pattern = AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperPatternDao()
                    .getAssessmentPaperPatternsByExamId(distinctExamIds.get(i));
            if (pattern != null)
//                if (pattern.getExammode() != null)
//                    if (pattern.getExammode().equalsIgnoreCase(Assessment_Constants.PRACTICE)) {
                examIdsForPracticeMode.add(pattern.getExamid());
//                    }
        }
        List<String> languageIds = new ArrayList<>();

        for (int j = 0; j < examIdsForPracticeMode.size(); j++) {
            List<String> langId = AppDatabase.getDatabaseInstance(getActivity())
                    .getCertificateKeywordRatingDao()
                    .getDistinctLangByExamIdStudentId(currentStudentID, examIdsForPracticeMode.get(j));

            if (langId != null && langId.size() > 0 && !langId.contains(langId))
                languageIds.addAll(langId);
        }

//        languageIds = AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperForPushDao().getAssessmentPapersByUniqueLangCertificatequestionsNotNull(currentStudentID);
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
                    }
                    presenter.getSubjectsFromDB(selectedLang);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    presenter.getSubjectsFromDB(selectedLang);
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.no_certificates, Toast.LENGTH_SHORT).show();
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
