package com.pratham.assessment.admin_pannel.PullData;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.R;
import com.pratham.assessment.admin_pannel.admin_login.AdminPanelFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PEF on 19/11/2018.
 */


public class PullDataFragment extends Fragment implements PullDataContract.PullDataView, VillageSelectListener {
    @BindView(R.id.rg_programs)
    RadioGroup radioGroupPrograms;

    @BindView(R.id.stateSpinner)
    Spinner stateSpinner;

    @BindView(R.id.blockSpinner)
    Spinner blockSpinner;

    @BindView(R.id.save_button)
    Button save_button;

    PullDataContract.PullDataPresenter pullDataPresenter;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pull_data_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        pullDataPresenter = new PullDataPresenterImp(getActivity(), this);
        pullDataPresenter.loadSpinner();
        radioGroupPrograms.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pullDataPresenter.clearLists();
            }
        });
        //pullDataPresenter.checkConnectivity();
    }

    @Override
    public void showStatesSpinner(String[] states) {
        ArrayAdapter arrayStateAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, states);
        arrayStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(arrayStateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                disableSaveButton();
                if (pos <= 0) {
                    clearBlockSpinner();

                } else {
                    int selectedRadioButtonId = radioGroupPrograms.getCheckedRadioButtonId();
                    if (selectedRadioButtonId == -1) {
                        Toast.makeText(getContext(), "Please Select Program", Toast.LENGTH_SHORT).show();
                    } else {
                        RadioButton radioButton = radioGroupPrograms.findViewById(selectedRadioButtonId);
                        String selectedProgram = radioButton.getText().toString();
                        pullDataPresenter.loadBlockSpinner(pos, selectedProgram);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    @Override
    public void shoConfermationDialog(int crlListCnt, int studentListcnt, int groupListCnt, int villageListCnt) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Data Preview");
        dialogBuilder.setMessage("CRLList : " + crlListCnt + "\nstudentList : " + studentListcnt + "\ngroupsList : " + groupListCnt + "\nvillageList : " + villageListCnt);
        dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                pullDataPresenter.saveData();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                pullDataPresenter.clearLists();
            }
        });
        dialogBuilder.show();
    }

    @Override
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void clearBlockSpinner() {
        blockSpinner.setSelection(0);
        blockSpinner.setEnabled(false);
    }

    @Override
    public void clearStateSpinner() {
        stateSpinner.setSelection(0);
    }

    @Override
    public void showBlocksSpinner(List blocks) {
        blockSpinner.setEnabled(true);
        ArrayAdapter arrayBlockAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, blocks);
        arrayBlockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockSpinner.setAdapter(arrayBlockAdapter);
        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                disableSaveButton();
                if (pos > 0) {
                    //open Village Dialog
                    String block = adapterView.getSelectedItem().toString();
                    pullDataPresenter.proccessVillageData(block);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void showVillageDialog(List villageList) {
        Dialog villageDialog = new SelectVillageDialog(getActivity(), this, villageList);
        villageDialog.show();
    }

    @Override
    public void disableSaveButton() {
        save_button.setEnabled(false);
    }

    @Override
    public void enableSaveButton() {
        save_button.setEnabled(true);
    }

    @Override
    public void showErrorToast() {
        Toast.makeText(getActivity(), "Connect to internet ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openLoginActivity() {
        getActivity().getSupportFragmentManager().beginTransaction().
                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_attendance)).commit();
        Assessment_Utility.showFragment(getActivity(), new AdminPanelFragment(), R.id.frame_attendance,
                null, AdminPanelFragment.class.getSimpleName());
        // getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showNoConnectivity() {
        Toast.makeText(getActivity(), "No Internet Connection..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getSelectedItems(ArrayList<String> villageIDList) {
        pullDataPresenter.downloadStudentAndGroup(villageIDList);
    }

    @OnClick(R.id.save_button)
    public void saveData() {
        pullDataPresenter.onSaveClick();
    }
}
