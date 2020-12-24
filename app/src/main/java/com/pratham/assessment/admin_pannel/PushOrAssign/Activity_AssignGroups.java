package com.pratham.assessment.admin_pannel.PushOrAssign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.Village;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity_;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;



@EActivity(R.layout.activity_assign_groups)
public class Activity_AssignGroups extends BaseActivity {

    @ViewById(R.id.spinner_SelectState)
    Spinner spinner_SelectState;
    @ViewById(R.id.spinner_SelectBlock)
    Spinner spinner_SelectBlock;
    @ViewById(R.id.spinner_selectVillage)
    Spinner spinner_selectVillage;

    @ViewById(R.id.assignGroup1)
    LinearLayout assignGroup1;
    @ViewById(R.id.assignGroup2)
    LinearLayout assignGroup2;
    @ViewById(R.id.LinearLayoutGroups)
    LinearLayout LinearLayoutGroups;

    @ViewById(R.id.allocateGroups)
    Button allocateGroups;

    Boolean isAssigned = false;

    List<String> Blocks;
    private List<Groups> dbgroupList;
    private int vilID, cnt = 0;
    public String checkBoxIds[], group1 = "0", group2 = "0", group3 = "0", group4 = "0", group5 = "0";
    private ProgressDialog progress;


    @AfterViews
    public void init(){
        initializeStatesSpinner();

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_groups);
        ButterKnife.bind(this);

        initializeStatesSpinner();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        showProgramwiseSpinners();
    }

    // Show/Hide Spinners according to Program
    private void showProgramwiseSpinners() {
        // Hide Village Spinner based on HLearning / RI
        String programID = AppDatabase.getDatabaseInstance(this).getStatusDao().getValue("programId");

        if (programID.equals("1") || programID.equals("3") || programID.equals("10")) {
            spinner_selectVillage.setVisibility(View.VISIBLE);
        } else if (programID.equals("2")) // RI
        {
            spinner_selectVillage.setVisibility(View.GONE);
        } else {
            spinner_selectVillage.setVisibility(View.VISIBLE);
        }

    }

    // Populate States Spinner
    private void initializeStatesSpinner() {
        //Get Villages Data for States AllSpinners
        List<String> States = new ArrayList<>();
//        States.add("Select State");
//        States = BaseActivity.villageDao.getAllStates();
        States = AppDatabase.getDatabaseInstance(this).getVillageDao().getAllStates();
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> StateAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, States);
        // Hint for AllSpinners
        spinner_SelectState.setPrompt("Select State");
        spinner_SelectState.setAdapter(StateAdapter);

        spinner_SelectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedState = spinner_SelectState.getSelectedItem().toString();
                populateBlock(selectedState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Populate Blocks
    public void populateBlock(String selectedState) {
        spinner_SelectBlock = (Spinner) findViewById(R.id.spinner_SelectBlock);
        //Get Villages Data for Blocks AllSpinners
        Blocks = new ArrayList<>();
//        Blocks.add("Select Block");
        //Blocks = BaseActivity.villageDao.GetStatewiseBlock(selectedState);
        Blocks = AppDatabase.getDatabaseInstance(this).getVillageDao().GetStatewiseBlock(selectedState);
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> BlockAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, Blocks);
        // Hint for AllSpinners
        spinner_SelectBlock.setPrompt("Select Block");
        spinner_SelectBlock.setAdapter(BlockAdapter);

        spinner_SelectBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBlock = spinner_SelectBlock.getSelectedItem().toString();
                //  String programID = BaseActivity.statusDao.getValue("programId");
                String programID = AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().getValue("programId");
                if (programID.equals("1") || programID.equals("3") || programID.equals("10")) // H Learning
                {
                    populateVillage(selectedBlock);
                } else if (programID.equals("2")) // RI
                {
                    populateRIVillage(selectedBlock);
                } else {
                    populateVillage(selectedBlock);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    // Populate Villages
    public void populateVillage(String selectedBlock) {

        //Get Villages Data for Villages filtered by block for Spinners
        List<Village> BlocksVillages = new ArrayList<Village>();
//        BlocksVillages.add(new Modal_Village(0, "--Select Village--"));
//        BlocksVillages = BaseActivity.villageDao.GetVillages(selectedBlock);
        BlocksVillages = AppDatabase.getDatabaseInstance(this).getVillageDao().GetVillages(selectedBlock);

        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<Village> VillagesAdapter = new ArrayAdapter<Village>(this, R.layout.custom_spinner, BlocksVillages);
        // Hint for AllSpinners
        spinner_selectVillage.setPrompt("Select Village");
        spinner_selectVillage.setAdapter(VillagesAdapter);
        spinner_selectVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Village village = (Village) parent.getItemAtPosition(position);
                vilID = village.getVillageId();
                try {
                    populateGroups(vilID);  //Populate groups According to JSON & DB in Checklist instead of using spinner
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Populate RI Villages (Block Not present in Groups)
    public void populateRIVillage(String selectedBlock) {

        // vilID = BaseActivity.villageDao.GetVillageIDByBlock(selectedBlock);
        vilID = AppDatabase.getDatabaseInstance(this).getVillageDao().GetVillageIDByBlock(selectedBlock);
        try {
            populateGroups(vilID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Populate Groups
    private void populateGroups(int vilID) throws JSONException {
        String programID = AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().getValue("programId");

        //String programID = BaseActivity.statusDao.getValue("programId");
        // Check Spinner Emptyness
        int VillagesSpinnerValue = spinner_selectVillage.getSelectedItemPosition();

//        Toast.makeText(this, ""+vilID, Toast.LENGTH_SHORT).show();

        // if (VillagesSpinnerValue > 0 || programID.equals("2")) {

        // Showing Groups from Database
        checkBoxIds = null;

//            dbgroupList = BaseActivity.groupDao.GetGroups(vilID);
        dbgroupList = AppDatabase.getDatabaseInstance(this).getGroupsDao().GetGroups(vilID);

//            groupList.remove(0);
        assignGroup1.removeAllViews();
        assignGroup2.removeAllViews();

        checkBoxIds = new String[dbgroupList.size()];
        int half = Math.round(dbgroupList.size() / 2);

        for (int i = 0; i < dbgroupList.size(); i++) {

            Groups grp = dbgroupList.get(i);
            String groupName = grp.getGroupName();
            String groupId = grp.getGroupId();

            TableRow row = new TableRow(Activity_AssignGroups.this);
            //row.setId(groupId);
            checkBoxIds[i] = groupId;

            //dynamically create checkboxes. i.e no. of students in group = no. of checkboxes
            row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            CheckBox checkBox = new CheckBox(Activity_AssignGroups.this);

            try {
                checkBox.setId(i);
                checkBox.setTag(groupId);
                checkBox.setText(groupName);
            } catch (Exception e) {

            }
            checkBox.setTextSize(20);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setBackgroundColor(Color.LTGRAY);

            row.addView(checkBox);
            if (i >= half)
                assignGroup2.addView(row);
            else
                assignGroup1.addView(row);
        }
        // Animation Effect on Groups populate
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        LinearLayoutGroups.startAnimation(animation1);

        allocateGroups.setVisibility(View.VISIBLE);
       /* } else {
            assignGroup1.removeAllViews();
            assignGroup2.removeAllViews();
        }*/

    }

    // Delete Groups with Students
    private void deleteGroupsWithStudents() {
        // Delete Records of Deleted Students
        //List<Groups> deletedGroupsList = BaseActivity.groupDao.GetAllDeletedGroups();
        List<Groups> deletedGroupsList = AppDatabase.getDatabaseInstance(this).getGroupsDao().GetAllDeletedGroups();

        // Delete students for all deleted groups
        for (int i = 0; i < deletedGroupsList.size(); i++) {
            //Delete Group
            //BaseActivity.groupDao.deleteGroupByGrpID(deletedGroupsList.get(i).GroupId);
            AppDatabase.getDatabaseInstance(this).getGroupsDao().deleteGroupByGrpID(deletedGroupsList.get(i).GroupId);
            // Delete Student
            //BaseActivity.studentDao.deleteDeletedGrpsStdRecords(deletedGroupsList.get(i).GroupId);
            AppDatabase.getDatabaseInstance(this).getStudentDao().deleteDeletedGrpsStdRecords(deletedGroupsList.get(i).GroupId);
        }
    }

    // Assign Groups
    @Click(R.id.allocateGroups)
    public void assignButtonClick() {
        try {
            group1 = group2 = group3 = group4 = group5 = "0";
            cnt = 0;
            for (int i = 0; i < checkBoxIds.length; i++) {
                CheckBox checkBox = (CheckBox) findViewById(i);

                if (checkBox.isChecked() && group1.equals("0")) {
                    group1 = (String) checkBox.getTag();
                    cnt++;
                } else if (checkBox.isChecked() && group2.equals("0")) {
                    cnt++;
                    group2 = (String) checkBox.getTag();
                } else if (checkBox.isChecked() && group3.equals("0")) {
                    cnt++;
                    group3 = (String) checkBox.getTag();
                } else if (checkBox.isChecked() && group4.equals("0")) {
                    cnt++;
                    group4 = (String) checkBox.getTag();
                } else if (checkBox.isChecked() && group5.equals("0")) {
                    cnt++;
                    group5 = (String) checkBox.getTag();
                } else if (checkBox.isChecked()) {
                    cnt++;
                }

            }

            if (cnt < 1) {
                Toast.makeText(Activity_AssignGroups.this, R.string.please_select_at_least_one_group, Toast.LENGTH_SHORT).show();
            } else if (cnt >= 1 && cnt <= 5) {
                try {
                    //   MultiPhotoSelectActivity.dilog.showDilog(context, "Assigning Groups");
                    progress = new ProgressDialog(Activity_AssignGroups.this);
                    progress.setMessage(getString(R.string.please_wait));
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();

//                    Toast.makeText(Activity_AssignGroups.this, "grp1 : " + group1 + "\ngrp2 : " + group2 + "\ngrp3 : " + group3 + "\ngrp4 : " + group4 + "\ngrp5 : " + group5, Toast.LENGTH_SHORT).show();

                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            // Delete Groups where Device ID is deleted & also delete associated students & update status table
                            deleteGroupsWithStudents();
                            // delete deleted Students
                         /*   BaseActivity.studentDao.deleteDeletedStdRecords();
                            // Update Groups in Status
                            BaseActivity.statusDao.updateValue(PD_Constant.GROUPID1, group1);
                            BaseActivity.statusDao.updateValue(PD_Constant.GROUPID2, group2);
                            BaseActivity.statusDao.updateValue(PD_Constant.GROUPID3, group3);
                            BaseActivity.statusDao.updateValue(PD_Constant.GROUPID4, group4);
                            BaseActivity.statusDao.updateValue(PD_Constant.GROUPID5, group5);
                            BaseActivity.statusDao.updateValue("village", Integer.toString(vilID));
                            BaseActivity.statusDao.updateValue("DeviceId", PD_Utility.getDeviceID());
                            BaseActivity.statusDao.updateValue("ActivatedDate", PD_Utility.getCurrentDateTime());
                            BaseActivity.statusDao.updateValue("ActivatedForGroups", group1 + "," + group2 + "," + group3 + "," + group4 + "," + group5);*/


                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStudentDao().deleteDeletedStdRecords();
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue(Assessment_Constants.GROUPID1, group1);
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue(Assessment_Constants.GROUPID2, group2);
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue(Assessment_Constants.GROUPID3, group3);
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue(Assessment_Constants.GROUPID4, group4);
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue(Assessment_Constants.GROUPID5, group5);

                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue("village", Integer.toString(vilID));
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue("DeviceId", "" + Settings.Secure.getString(Activity_AssignGroups.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue("ActivatedDate", Assessment_Utility.getCurrentDateTime());
                            AppDatabase.getDatabaseInstance(Activity_AssignGroups.this).getStatusDao().updateValue("ActivatedForGroups", group1 + "," + group2 + "," + group3 + "," + group4 + "," + group5);

                            //  MultiPhotoSelectActivity.dilog.dismissDilog();
                            Activity_AssignGroups.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    isAssigned = true;
                                    Toast.makeText(Activity_AssignGroups.this, R.string.groups_assigned_successfully, Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                   /* FragmentManager fm = getSupportFragmentManager();
                                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                        fm.popBackStack();
                                    }*/
                                    startActivity(new Intent(Activity_AssignGroups.this, SelectGroupActivity_.class));
                                    finish();
                                    onBackPressed();
                                }
                            });
                        }
                    };
                    mThread.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(Activity_AssignGroups.this, R.string.you_can_select_maximum_5_groups, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (isAssigned) {
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            finish();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}
