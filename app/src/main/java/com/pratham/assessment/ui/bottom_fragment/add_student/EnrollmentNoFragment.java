package com.pratham.assessment.ui.bottom_fragment.add_student;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.interfaces.SplashInterface;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnrollmentNoFragment extends DialogFragment {

    @BindView(R.id.et_enrollment_no)
    EditText enrollmentNo;

    @BindView(R.id.tv_enrolled_student_name)
    TextView tv_enrolled_student_name;
    @BindView(R.id.tv_enrolled_student_age)
    TextView tv_enrolled_student_age;
    @BindView(R.id.tv_enrolled_student_class)
    TextView tv_enrolled_student_class;
    @BindView(R.id.tv_enrolled_student_gender)
    TextView tv_enrolled_student_gender;
    @BindView(R.id.tv_enrolled_student_grp_id)
    TextView tv_enrolled_student_grp_id;
    @BindView(R.id.tv_enrolled_student_grp_name)
    TextView tv_enrolled_student_grp_name;
    @BindView(R.id.tv_enrolled_student_village_id)
    TextView tv_enrolled_student_village_id;
    @BindView(R.id.tv_enrolled_student_village_name)
    TextView tv_enrolled_student_village_name;

    @BindView(R.id.rl_enroll_no_details_outer)
    RelativeLayout rl_enroll_no_details;

    @BindView(R.id.rl_enroll_no_not_found)
    RelativeLayout rl_enroll_no_not_found;

    Student newEnrolledStudent;

    static SplashInterface splashInterface;

    public EnrollmentNoFragment() {

        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }


    public static EnrollmentNoFragment newInstance(SplashInterface splashInter) {
        EnrollmentNoFragment frag = new EnrollmentNoFragment();
        Bundle args = new Bundle();
        args.putString("title", "Create Profile");
        splashInterface = splashInter;
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enrollment_no, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this, view);
        rl_enroll_no_details.setVisibility(View.GONE);
        rl_enroll_no_not_found.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_check_enrollment_no)
    public void checkNo() {
        if (!enrollmentNo.getText().toString().trim().equalsIgnoreCase("")) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                getStudentByEnrollmentNo(enrollmentNo.getText().toString().trim());
            } else {
                newEnrolledStudent = AppDatabase.getDatabaseInstance(getActivity()).getStudentDao().getStudent(enrollmentNo.getText().toString().trim());
                setResponse(newEnrolledStudent);
            }
        } else {
            Toast.makeText(getActivity(), R.string.enter_enrollment_number, Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.btn_add_new_student_enroll)
    public void onAddNewClick() {
        //ButtonClickSound.start();
        if (newEnrolledStudent.getGender() != null) {
            if (newEnrolledStudent.getGender().trim().equalsIgnoreCase("male"))
                newEnrolledStudent.setAvatarName(Assessment_Utility.getRandomMaleAvatarName(getActivity()));
            else if (newEnrolledStudent.getGender().trim().equalsIgnoreCase("female"))
                newEnrolledStudent.setAvatarName(Assessment_Utility.getRandomFemaleAvatarName(getActivity()));
            else
                newEnrolledStudent.setAvatarName(Assessment_Utility.getRandomAvatarName(getActivity()));
        }
        newEnrolledStudent.setDeviceId(Assessment_Utility.getDeviceId(getActivity()));
        newEnrolledStudent.setStudentUID("NIOS");
        newEnrolledStudent.setIsniosstudent("1");
        Student student = AppDatabase.getDatabaseInstance(getActivity()).getStudentDao().getStudent(newEnrolledStudent.getStudentID());
        if (student != null) {
            Toast.makeText(getActivity(), R.string.profile_is_already_saved, Toast.LENGTH_SHORT).show();
        } else {
            AppDatabase.getDatabaseInstance(getActivity()).getStudentDao().insert(newEnrolledStudent);
            BackupDatabase.backup(getActivity());
            Toast.makeText(getActivity(), R.string.profile_created_successfully, Toast.LENGTH_SHORT).show();
            splashInterface.onChildAdded();
            dismiss();
        }
    }


    private void getStudentByEnrollmentNo(String enrollmentNo) {
        try {
            newEnrolledStudent = new Student();
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            AndroidNetworking.get(APIs.pullStudentByEnrollmentNoAPI + enrollmentNo)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response.length() > 0) {
                                try {
                                    newEnrolledStudent.setStudentID(response.getJSONObject(0).getString("StudentId"));
                                    newEnrolledStudent.setFullName(response.getJSONObject(0).getString("FullName"));
                                    newEnrolledStudent.setAge(response.getJSONObject(0).getInt("Age"));
                                    newEnrolledStudent.setStud_Class(response.getJSONObject(0).getString("Class"));
                                    newEnrolledStudent.setGender(response.getJSONObject(0).getString("Gender"));
                                    newEnrolledStudent.setGroupId(response.getJSONObject(0).getString("GroupId"));
                                    newEnrolledStudent.setGroupName(response.getJSONObject(0).getString("GroupName"));
//                                    newEnrolledStudent.setVillageId(response.getJSONObject(0).getString("villageid"));
                                    newEnrolledStudent.setVillageName(response.getJSONObject(0).getString("VillageName"));

                                    setResponse(newEnrolledStudent);
                                    progressDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();

                                }
                            } else
                                setResponse(newEnrolledStudent);
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setResponse(Student student) {
        try {
            if (student != null) {
                if (student.getFullName() != null) {

                    tv_enrolled_student_name.setText(student.getFullName());
                    tv_enrolled_student_age.setText(student.getAge() + "");
                    tv_enrolled_student_class.setText(student.getStud_Class());
                    tv_enrolled_student_gender.setText(student.getGender());
                    tv_enrolled_student_grp_id.setText(student.getGroupId());
                    tv_enrolled_student_grp_name.setText(student.getGroupName());
    //            tv_enrolled_student_village_id.setText(student.get);
                    tv_enrolled_student_village_name.setText(student.getVillageName());

                    rl_enroll_no_details.setVisibility(View.VISIBLE);
                    rl_enroll_no_not_found.setVisibility(View.GONE);
                } else {
                    rl_enroll_no_details.setVisibility(View.GONE);
                    rl_enroll_no_not_found.setVisibility(View.VISIBLE);
                }
            } else {
                if (!AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                    Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                rl_enroll_no_details.setVisibility(View.GONE);
                rl_enroll_no_not_found.setVisibility(View.VISIBLE);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


}
