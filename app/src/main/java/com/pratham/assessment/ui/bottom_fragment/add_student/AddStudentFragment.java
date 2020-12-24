package com.pratham.assessment.ui.bottom_fragment.add_student;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AvatarModal;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.interfaces.SplashInterface;
import com.pratham.assessment.ui.choose_assessment.ChooseAssessmentActivity;
import com.pratham.assessment.ui.splash_activity.SplashActivity;
import com.pratham.assessment.constants.APIs;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
*/

import static com.pratham.assessment.constants.Assessment_Constants.LANGUAGE;

public class AddStudentFragment extends DialogFragment implements AvatarClickListener {

    //    @BindView(R.id.form_root)
    RelativeLayout homeRoot;

    List<AssessmentLanguages> assessmentLanguagesList;
    ProgressDialog progressDialog;


    //    @BindView(R.id.rv_Avatars)
    RecyclerView recyclerView;

    //    @BindView(R.id.et_studentName)
    EditText et_studentName;

    //    @BindView(R.id.spinner_age)
    Spinner spinner_age;

    //    @BindView(R.id.spinner_app_lang)
    Spinner spinner_app_lang;

/*    @BindView(R.id.spinner_class)
    Spinner spinner_class;*/

    //    @BindView(R.id.rb_male)
    RadioButton rb_male;

    //    @BindView(R.id.rb_female)
    RadioButton rb_female;
    Button btn_add_new_student;
    String gender = "";
    String selectedLang = "";
    String avatarName;
    ArrayList<Integer> avatars;

    public AddStudentFragment() {
        // Required empty public constructor
    }

    ArrayList<AvatarModal> avatarList = new ArrayList<>();
    static SplashInterface splashInterface;
    AvatarAdapter avatarAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        avatars = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            avatars.add(Assessment_Utility.getRandomAvatar(getActivity()));


    }

    public void editorListener(final EditText view) {
        view.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            view.clearFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            return true;
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
    }

    public static AddStudentFragment newInstance(SplashInterface splashInter) {
        AddStudentFragment frag = new AddStudentFragment();
        Bundle args = new Bundle();
        args.putString("title", "Create Profile");
        frag.setArguments(args);
        splashInterface = splashInter;
        return frag;
    }

    @Override
    public void onPause() {
        super.onPause();
        SplashActivity.fragmentAddStudentPauseFlg = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        SplashActivity.fragmentAddStudentOpenFlg = true;
        SplashActivity.fragmentAddStudentPauseFlg = false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        SplashActivity.fragmentAddStudentOpenFlg = false;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());

        ArrayAdapter<String> ageAdapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner, getResources().getStringArray(R.array.age));
        spinner_age.setAdapter(ageAdapter);

        spinner_age.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
/*        spinner_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });*/
        spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

//        ArrayAdapter<String> classAdapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner, getResources().getStringArray(R.array.student_class));
        //spinner_class.setAdapter(classAdapter);
        addAvatarsInList();
        avatarAdapter = new AvatarAdapter(getActivity(), this, avatarList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(avatarAdapter);
        avatarAdapter.notifyDataSetChanged();

        assessmentLanguagesList = new ArrayList<>();

        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getLanguageData();
        } else {
            assessmentLanguagesList = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getAllLangs();
            if (assessmentLanguagesList.size() <= 0) {
//                Toast.makeText(getActivity(), R.string.connect_to_internet_to_download_languages, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Connect to internet to download languages", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                setLanguagesToSpinner();
            }
        }


/*        et_studentName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });*/
    /*    String langId = FastSave.getInstance().getString(LANGUAGE, "1");
        Assessment_Utility.setLocaleByLanguageId(getActivity(),langId);*/

    }

    private void setLanguagesToSpinner() {
        List<String> languages = new ArrayList<>();

        languages.add("Select language");
        for (int i = 0; i < assessmentLanguagesList.size(); i++) {
            languages.add(assessmentLanguagesList.get(i).getLanguagename());
        }
        if (getActivity() != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, languages);
            // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            // attaching data adapter to spinner
            spinner_app_lang.setAdapter(dataAdapter);
        }
        spinner_app_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    TextView lang = (TextView) view;
                    selectedLang = lang.getText().toString();
                    String langId = AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().getLangIdByName(selectedLang);
                    if (langId != null) {
                        Assessment_Constants.SELECTED_LANGUAGE = langId;
                        FastSave.getInstance().saveString(LANGUAGE, langId);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void hideKeyboard(View view) {
        if (view != null)
            if (getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
    }

    private void addAvatarsInList() {
      /*  for (int i = 0; i < 6; i++) {
            AvatarModal avatarModal = new AvatarModal();
            avatarModal.setAvatarName(""+Assessment_Utility.getRandomAvatar(getActivity()));
            avatarModal.setClickFlag(false);
            avatarList.add(avatarModal);
        }*/
        AvatarModal avatarModal = new AvatarModal();
        avatarModal.setAvatarName("g1.png");
        avatarModal.setClickFlag(false);
        avatarList.add(avatarModal);
        AvatarModal avatarModal1 = new AvatarModal();

        avatarModal1.setAvatarName("b1.png");
        avatarModal1.setClickFlag(false);
        avatarList.add(avatarModal1);
        AvatarModal avatarModal2 = new AvatarModal();

        avatarModal2.setAvatarName("g2.png");
        avatarModal2.setClickFlag(false);
        avatarList.add(avatarModal2);
        AvatarModal avatarModal3 = new AvatarModal();

        avatarModal3.setAvatarName("b2.png");
        avatarModal3.setClickFlag(false);
        avatarList.add(avatarModal3);
        AvatarModal avatarModal4 = new AvatarModal();

        avatarModal4.setAvatarName("g3.png");
        avatarModal4.setClickFlag(false);
        avatarList.add(avatarModal4);
        AvatarModal avatarModal5 = new AvatarModal();

        avatarModal5.setAvatarName("b3.png");
        avatarModal5.setClickFlag(false);
        avatarList.add(avatarModal5);

        Collections.shuffle(avatarList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_student, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        ButterKnife.bind(this, view);


        recyclerView = view.findViewById(R.id.rv_Avatars);
        et_studentName = view.findViewById(R.id.et_studentName);
        spinner_age = view.findViewById(R.id.spinner_age);
        spinner_app_lang = view.findViewById(R.id.spinner_app_lang);
        rb_male = view.findViewById(R.id.rb_male);
        rb_female = view.findViewById(R.id.rb_female);
        btn_add_new_student = view.findViewById(R.id.btn_add_new_student);
        rb_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";

            }
        });
        rb_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";

            }
        });

        btn_add_new_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNewClick();
            }
        });

        editorListener(et_studentName);
        return view;
    }

/*    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
       if(activity.getCurrentFocus()!=null && activity.getCurrentFocus().getWindowToken() != null)
           inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }*/

    /*public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }*/

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

    /* @OnClick(R.id.rb_male)
     public void maleGenderClicked() {
         //ButtonClickSound.start();
         // rb_male.setBackground(getResources().getDrawable(R.drawable.correct_bg));
         // rb_female.setBackground(getResources().getDrawable(R.drawable.ripple_rectangle));
         gender = "Male";
     }

     @OnClick(R.id.rb_female)
     public void femaleGenderClicked() {
         //ButtonClickSound.start();
         //rb_female.setBackground(getResources().getDrawable(R.drawable.correct_bg));
         //rb_male.setBackground(getResources().getDrawable(R.drawable.ripple_rectangle));
         gender = "Female";
     }
 */
//    @OnClick(R.id.btn_add_new_student)
    public void onAddNewClick() {
        //ButtonClickSound.start();
        if (assessmentLanguagesList.size() <= 0) {
            if (!AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
//                Toast.makeText(getActivity(), R.string.connect_to_internet_to_download_languages, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Connect to internet to download languages", Toast.LENGTH_SHORT).show();
            else {
                getLanguageData();
//                Toast.makeText(getActivity(), R.string.select_language, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Select language", Toast.LENGTH_SHORT).show();

            }
        } else if (et_studentName.getText().toString().equalsIgnoreCase("") ||
                /*spinner_class.getSelectedItem().toString().equalsIgnoreCase("select class") ||*/
                spinner_age.getSelectedItem().toString().equalsIgnoreCase("select age") ||
                gender.equalsIgnoreCase("") || avatarName == null ||
                selectedLang.equalsIgnoreCase("Select language") || selectedLang.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), R.string.please_fil_all_the_details, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Please fill all the details..", Toast.LENGTH_SHORT).show();
        } else {
            Student student = new Student();
            student.setStudentID(AssessmentApplication.getUniqueID().toString());
            student.setFullName(et_studentName.getText().toString());
            student.setAge(Integer.parseInt(spinner_age.getSelectedItem().toString()));
            student.setStud_Class(/*spinner_class.getSelectedItem().toString()*/"");
            student.setGender(gender);
            student.setAvatarName(avatarName);
            student.setGroupId("PS");
            student.setIsniosstudent("0");
//            student.setStudentUID("PS");
            /*if (gender.equalsIgnoreCase("male"))
                student.setAvatarName("b1");
            else
                student.setAvatarName("g3");
*/
            student.setDeviceId(Assessment_Utility.getDeviceId(getActivity()));
            AppDatabase.getDatabaseInstance(getActivity()).getStudentDao().insert(student);
            BackupDatabase.backup(getActivity());
//            Toast.makeText(getActivity(), R.string.profile_created_successfully, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Profile created Successfully..", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getActivity(), ChooseLevelActivity.class));
            splashInterface.onChildAdded();
            dismiss();

//            Bundle bundle = new Bundle();
//            bundle.putString("", "");
//            COS_Utility.showFragment(getActivity(), new StudentsFragment(), R.id.student_frame,
//                    bundle, StudentsFragment.class.getSimpleName());

        }
    }


    /*
                    <LinearLayout
        android:id="@+id/ll_class"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_margin="@dimen/_6sdp"
        android:layout_weight="1"
        android:orientation="horizontal">

                    <TextView
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="2"
        android:gravity="center_vertical"
        android:text="Class : "
        android:textAlignment="center"
        android:textColor="@color/colorBlack"
        android:textSize="@dimen/_15sdp"
        android:textStyle="bold" />

                    <Spinner
        android:id="@+id/spinner_class"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="5"
        android:background="@drawable/custom_spinner"
        android:entries="@array/age"
        android:popupBackground="@drawable/choose_level_bg" />
                </LinearLayout>
    */
    @Override
    public void onAvatarClick(int position, String StudentName) {
        avatarName = StudentName;

        for (int i = 0; i < avatarList.size(); i++)
            avatarList.get(i).setClickFlag(false);
        avatarList.get(position).setClickFlag(true);
        avatarAdapter.notifyDataSetChanged();
//        avatarName = AssessmentApplication.pradigiPath + "/.LLA/English/LLA_Thumbs/" + StudentName;

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

    private void getLanguageData() {
//        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentLanguageAPI)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (getActivity() != null && progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();

                            for (int i = 0; i < response.length(); i++) {
                                AssessmentLanguages assessmentLanguages = new AssessmentLanguages();
                                assessmentLanguages.setLanguageid(response.getJSONObject(i).getString("languageid"));
                                assessmentLanguages.setLanguagename(response.getJSONObject(i).getString("languagename"));
                                assessmentLanguagesList.add(assessmentLanguages);
                            }
                            if (getActivity() != null)
                                AppDatabase.getDatabaseInstance(getActivity()).getLanguageDao().insertAllLanguages(assessmentLanguagesList);
                            setLanguagesToSpinner();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        Toast.makeText(getActivity(), R.string.error_in_loading_check_internet_connection, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
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

}
