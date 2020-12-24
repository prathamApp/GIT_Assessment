package com.pratham.assessment.admin_pannel.PullData;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.async.API_Content;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.ModalProgram;
import com.pratham.assessment.domain.RaspCrl;
import com.pratham.assessment.domain.RaspGroup;
import com.pratham.assessment.domain.RaspProgram;
import com.pratham.assessment.domain.RaspStudent;
import com.pratham.assessment.domain.RaspVillage;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.Village;
import com.pratham.assessment.interfaces.API_Content_Result;
import com.pratham.assessment.constants.APIs;
import com.pratham.assessment.constants.Assessment_Constants;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by PEF on 20/11/2018.
 */
@EBean
public class PullDataPresenterImp implements PullDataContract.PullDataPresenter, API_Content_Result {
    Context context;
    PullDataContract.PullDataView pullDataView;
    String selectedBlock;
    String selectedProgram;
    int count = 0;
    int groupCount = 0;
    ArrayList<Village> villageList = new ArrayList<>();
    ArrayList stateCodes;
//    ArrayList<RaspVillage> raspVillageList = new ArrayList<>();

    List<Crl> crlList = new ArrayList<>();
    List<Student> studentList = new ArrayList();
    List<Groups> groupList = new ArrayList();
    List<String> villageIDList = new ArrayList();
    Boolean isConnectedToRasp = false;
    API_Content api_content;

    public PullDataPresenterImp(Context context) {
        this.context = context;
        api_content = new API_Content(context, this);
    }

    List<ModalProgram> prgrmList = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    public void loadPrgramsSpinner() {
        if (isConnectedToRasp) {
            AndroidNetworking.get(Assessment_Constants.URL.DATASTORE_RASPBERY_PROGRAM_STATE_URL.toString())
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham")).build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            prgrmList.clear();
                            Type listType = new TypeToken<List<RaspProgram>>() {
                            }.getType();
                            List<RaspProgram> prgm = gson.fromJson(response.toString(), listType);
                            if (prgm != null) {
                                for (RaspProgram prg : prgm) {
                                    ModalProgram mp = new ModalProgram();
                                    mp.setProgramId(prg.getData().getKolibriProgramId());
                                    mp.setProgramName(prg.getData().getKolibriProgramName());
                                    prgrmList.add(mp);
                                }
                                ModalProgram mp = new ModalProgram();
                                mp.setProgramId("-1");
                                mp.setProgramName("Select Program");
                                LinkedHashSet hs = new LinkedHashSet(prgrmList);//to remove redundant values
                                prgrmList.clear();
                                prgrmList.addAll(hs);
                                prgrmList.add(0, mp);
                                pullDataView.showProgram(prgrmList);
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        } else {
            AndroidNetworking.get(Assessment_Constants.URL.PULL_PROGRAMS.toString())
                    .addHeaders("Content-Type", "application/json").build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            prgrmList.clear();
                            Type listType = new TypeToken<List<ModalProgram>>() {
                            }.getType();
                            prgrmList = gson.fromJson(response.toString(), listType);
                            if (prgrmList != null) {
                                ModalProgram modalProgram = new ModalProgram();
                                modalProgram.setProgramId("-1");
                                modalProgram.setProgramName("Select Program");
                                LinkedHashSet hs = new LinkedHashSet(prgrmList);//to remove redundant values
                                prgrmList.clear();
                                prgrmList.addAll(hs);
                                prgrmList.add(0, modalProgram);
                                pullDataView.showProgram(prgrmList);
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void loadSpinner(String selectedProgram) {
//        String[] states = context.getResources().getStringArray(R.array.india_states);
        pullStates(selectedProgram);
    }

    public void pullStates(String selectedProgram) {
        ArrayList states = new ArrayList();
        stateCodes = new ArrayList();
        states.add("SELECT STATE");
        stateCodes.add("SELECT STATE");
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading subjects");
        AndroidNetworking.get(APIs.pullStateAPI + selectedProgram)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                states.add(response.getJSONObject(i).getString("StateName"));
                                stateCodes.add(response.getJSONObject(i).getString("StateCode"));
                            }
                            if (states.size() > 0) {
                                pullDataView.showStatesSpinner(states);
                            } else
                                Toast.makeText(context, R.string.no_states, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, R.string.error_in_loading_check_internet_connection, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void setView(PullDataContract.PullDataView pullDataView) {
        this.pullDataView = pullDataView;
    }

    @Override
    public void proccessVillageData(String block) {
        ArrayList<Village> villageName = new ArrayList();
/*
        if (isConnectedToRasp) {
            for (RaspVillage raspVillage : raspVillageList) {
                Village village = raspVillage.getData();
                if (block.equalsIgnoreCase(village.getBlock().trim()))
                    villageName.add(new Village(village.getVillageId(), village.getVillageName()));
            }
        } else {
*/
        for (Village village : villageList) {
            if (block.equalsIgnoreCase(village.getBlock().trim()))
                villageName.add(new Village(village.getVillageId(), village.getVillageName()));
        }
//        }
        if (!villageName.isEmpty()) {
            pullDataView.showVillageDialog(villageName);
        }
    }

    @Override
    public void loadBlockSpinner(int pos, String selectedProgram) {

        pullDataView.showProgressDialog("loading Blocks");

//        String[] statesCodes = context.getResources().getStringArray(R.array.india_states_shortcode);
//        selectedBlock = statesCodes[pos];
        selectedBlock = (String) stateCodes.get(pos);
        this.selectedProgram = selectedProgram;
        String url;
        if (isConnectedToRasp) {
            url = APIs.pullVillagesKolibriURL + selectedProgram + APIs.KOLIBRI_STATE + selectedBlock;
            api_content.pullFromKolibri(Assessment_Constants.KOLIBRI_BLOCK, url);
        } else {
            url = APIs.pullVillagesServerURL + selectedProgram + APIs.SERVER_STATE + selectedBlock;
            api_content.pullFromInternet(Assessment_Constants.SERVER_BLOCK, url);
        }
//        StatusDao statusDao = AppDatabase.getDatabaseInstance(context).getStatusDao();
//        statusDao.updateValue("programId", "" + selectedProgram);

/*        switch (selectedProgram) {
            case APIs.HL:
                if (isConnectedToRasp)
                    url = APIs.RaspHLpullVillagesURL + selectedBlock;
                else
                    url = APIs.HLpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
            case APIs.UP:
                if (isConnectedToRasp)
                    url = APIs.RaspUPpullVillagesURL + selectedBlock;
                else
                    url = APIs.UPpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
            case APIs.ECE:
                url = APIs.ECEpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
            case RI:
                if (isConnectedToRasp)
                    url = APIs.RaspRIpullVillagesURL + selectedBlock;
                else
                    url = APIs.RIpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
            case SC:
                if (isConnectedToRasp)
                    url = APIs.RaspSCpullVillagesURL + selectedBlock;
                else
                    url = APIs.SCpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
            case PI:
                if (isConnectedToRasp)
                    url = APIs.RaspPIpullVillagesURL + selectedBlock;
                else
                    url = APIs.PIpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
        }*/
    }

/*
    private void downloadblock(String url) {
        if (isConnectedToRasp) {

            AndroidNetworking.get(url)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham")).build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response
                            List<String> blockList = new ArrayList<>();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<RaspVillage>>() {
                            }.getType();
                            raspVillageList = gson.fromJson(response.toString(), listType);
                            if (raspVillageList != null) {
                                if (raspVillageList.isEmpty()) {
                                    blockList.add("NO BLOCKS");
                                } else {
                                    blockList.add("Select block");
                                    for (RaspVillage raspVillage : raspVillageList) {
                                        Village village = raspVillage.getData();
                                        blockList.add(village.getBlock());

                                    }

                                }
                                LinkedHashSet hs = new LinkedHashSet(blockList);
                                blockList.clear();
                                blockList.addAll(hs);
                                pullDataView.showBlocksSpinner(blockList);
                            }
                            pullDataView.closeProgressDialog();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            pullDataView.closeProgressDialog();
                            pullDataView.clearBlockSpinner();
                            pullDataView.showErrorToast();
                        }
                    });

        } else {
            AndroidNetworking.get(url)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham")).build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response
                            List<String> blockList = new ArrayList<>();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Village>>() {
                            }.getType();
                            villageList = gson.fromJson(response.toString(), listType);
                            if (villageList != null) {
                                if (villageList.isEmpty()) {
                                    blockList.add("NO BLOCKS");
                                } else {
                                    blockList.add("Select block");
                                    for (Village village : villageList) {
                                        blockList.add(village.getBlock());
                                    }
                                }
                                LinkedHashSet hs = new LinkedHashSet(blockList);
                                blockList.clear();
                                blockList.addAll(hs);
                                pullDataView.showBlocksSpinner(blockList);
                            }
                            pullDataView.closeProgressDialog();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            pullDataView.closeProgressDialog();
                            pullDataView.clearBlockSpinner();
                            pullDataView.showErrorToast();
                        }
                    });
        }
    }
*/


    private String getAuthHeader(String ID, String pass) {
        String encoded = Base64.encodeToString((ID + ":" + pass).getBytes(), Base64.NO_WRAP);
        String returnThis = "Basic " + encoded;
        return returnThis;
    }

    @Override
    public void downloadStudentAndGroup(ArrayList<String> villageIDList1) {
        //download Student groups and CRL
        // 1 download crl
        pullDataView.showProgressDialog("loading..");
        villageIDList.clear();
        villageIDList.addAll(villageIDList1);
        studentList.clear();
        count = 0;
        String url;
        if (isConnectedToRasp) {
            for (String id : villageIDList) {
                url = APIs.pullStudentsKolibriURL + selectedProgram + APIs.KOLIBRI_VILLAGE + id;
                api_content.pullFromKolibri(Assessment_Constants.KOLIBRI_STU, url);
            }
        } else {
            for (String id : villageIDList) {
                url = APIs.pullStudentsServerURL + selectedProgram + APIs.SERVER_VILLAGE + id;
                api_content.pullFromInternet(Assessment_Constants.SERVER_STU, url);
            }
        }
        /*for (String id : villageIDList) {
            String url;
            switch (selectedProgram) {
                case APIs.HL:
                    if (isConnectedToRasp)
                        url = APIs.RaspHLpullStudentsURL + id;
                    else
                        url = APIs.HLpullStudentsURL + id;
                    loadStudent(url);
                    break;
                case APIs.UP:
                    if (isConnectedToRasp)
                        url = APIs.RaspUPpullStudentsURL + id;
                    else
                        url = APIs.UPpullStudentsURL + id;
                    loadStudent(url);
                    break;
                case APIs.ECE:
                    url = APIs.ECEpullStudentsURL + id;
                    loadStudent(url);
                    break;
                case RI:
                    if (isConnectedToRasp)
                        url = APIs.RaspRIpullStudentsURL + id;
                    else
                        url = APIs.RIpullStudentsURL + id;
                    loadStudent(url);
                    break;
                case SC:
                    if (isConnectedToRasp)
                        url = APIs.RaspSCpullStudentsURL + id;
                    else
                        url = APIs.SCpullStudentsURL + id;
                    loadStudent(url);
                    break;
                case PI:
                    if (isConnectedToRasp)
                        url = APIs.RaspPIpullStudentsURL + id;
                    else
                        url = APIs.PIpullStudentsURL + id;
                    loadStudent(url);
                    break;
            }

        }*/
    }

    private void loadStudent(String url) {
        if (isConnectedToRasp) {
            AndroidNetworking.get(url).addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham")).build().getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    count++;
                    String json = response.toString();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<RaspStudent>>() {
                    }.getType();
                    List<RaspStudent> studentListTemp = gson.fromJson(json, listType);
                    for (RaspStudent raspStudent : studentListTemp) {
                        for (Student student : raspStudent.getData()) {
                            studentList.add(student);
                        }
                    }
                    loadGroups();
                    //dismissDialog();
                }

                @Override
                public void onError(ANError error) {
                    studentList.clear();
                    pullDataView.closeProgressDialog();
                    pullDataView.showErrorToast();
                    // dismissDialog();
                }
            });
        } else {
            AndroidNetworking.get(url).addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham")).build().getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    count++;
                    String json = response.toString();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Student>>() {
                    }.getType();
                    List<Student> studentListTemp = gson.fromJson(json, listType);
                    if (studentListTemp != null) {
                        studentList.addAll(studentListTemp);
                    }
                    loadGroups();
                    //dismissDialog();
                }

                @Override
                public void onError(ANError error) {
                    studentList.clear();
                    pullDataView.closeProgressDialog();
                    pullDataView.showErrorToast();
                    // dismissDialog();
                }
            });
        }
    }

    private void loadGroups() {
        if (count >= villageIDList.size()) {
            groupCount = 0;
            groupList.clear();
            String urlgroup;
            if (isConnectedToRasp) {
                for (String id : villageIDList) {
                    urlgroup = APIs.pullGroupsKolibriURL + selectedProgram + APIs.KOLIBRI_VILLAGE + id;
                    api_content.pullFromKolibri(Assessment_Constants.KOLIBRI_GRP, urlgroup);
                }
            } else {
                for (String id : villageIDList) {
                    urlgroup = APIs.pullGroupsServerURL + selectedProgram + APIs.SERVER_VILLAGE + id;
                    api_content.pullFromInternet(Assessment_Constants.SERVER_GRP, urlgroup);
                }
            }
/*            for (String id : villageIDList) {
                switch (selectedProgram) {
                    case APIs.HL:
                        if (isConnectedToRasp)
                            urlgroup = APIs.RaspHLpullGroupsURL + id;
                        else
                            urlgroup = APIs.HLpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                    case APIs.UP:
                        if (isConnectedToRasp)
                            urlgroup = APIs.RaspUPpullGroupsURL + id;
                        else
                            urlgroup = APIs.UPpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                    case APIs.ECE:
                        urlgroup = APIs.ECEpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                    case RI:
                        if (isConnectedToRasp)
                            urlgroup = APIs.RaspRIpullGroupsURL + id;
                        else
                            urlgroup = APIs.RIpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                    case SC:
                        if (isConnectedToRasp)
                            urlgroup = APIs.RaspSCpullGroupsURL + id;
                        else
                            urlgroup = APIs.SCpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                    case PI:
                        if (isConnectedToRasp)
                            urlgroup = APIs.RaspPIpullGroupsURL + id;
                        else
                            urlgroup = APIs.PIpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                }
            }*/
        }
    }

    private void downloadGroups(String url) {
        if (isConnectedToRasp) {
            AndroidNetworking.get(url)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham"))
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            groupCount++;
                            String json = response.toString();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<RaspGroup>>() {
                            }.getType();
                            List<RaspGroup> groupListTemp = gson.fromJson(json, listType);
                            for (RaspGroup raspGroup : groupListTemp) {
                                for (Groups modal_groups : raspGroup.getData()) {
                                    groupList.add(modal_groups);
                                }

                            }
                            loadCRL();
                        }

                        @Override
                        public void onError(ANError error) {
                            studentList.clear();
                            pullDataView.closeProgressDialog();
                            pullDataView.showErrorToast();
                            // dismissDialog();
                        }
                    });
        } else {
            AndroidNetworking.get(url).addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham")).build().getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    groupCount++;
                    String json = response.toString();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Groups>>() {
                    }.getType();
                    List<Groups> groupListTemp = gson.fromJson(json, listType);
                    if (groupListTemp != null) {
                        groupList.addAll(groupListTemp);
                    }
                    loadCRL();
                }

                @Override
                public void onError(ANError error) {
                    studentList.clear();
                    pullDataView.closeProgressDialog();
                    pullDataView.showErrorToast();
                    // dismissDialog();
                }
            });
        }
    }

    private void loadCRL() {
        if (groupCount >= villageIDList.size()) {
            String crlURL;
            if (crlList != null) {
                crlList.clear();
            }
            if (isConnectedToRasp) {
                crlURL = APIs.pullCrlsKolibriURL + selectedProgram + APIs.KOLIBRI_STATE + selectedBlock;
                api_content.pullFromKolibri(Assessment_Constants.KOLIBRI_CRL, crlURL);
            } else {
                crlURL = APIs.pullCrlsServerURL + selectedProgram + APIs.SERVER_STATECODE + selectedBlock;
                api_content.pullFromInternet(Assessment_Constants.SERVER_CRL, crlURL);
            }
/*            switch (selectedProgram) {
                case APIs.HL:
                    if (isConnectedToRasp)
                        crlURL = APIs.RaspHLpullCrlsURL + selectedBlock;
                    else
                        crlURL = APIs.HLpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
                case APIs.UP:
                    if (isConnectedToRasp)
                        crlURL = APIs.RaspUPpullCrlsURL + selectedBlock;
                    else
                        crlURL = APIs.UPpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
                case APIs.ECE:
                    crlURL = APIs.ECEpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
                case RI:
                    if (isConnectedToRasp)
                        crlURL = APIs.RaspRIpullCrlsURL + selectedBlock;
                    else
                        crlURL = APIs.RIpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
                case SC:
                    if (isConnectedToRasp)
                        crlURL = APIs.RaspSCpullCrlsURL + selectedBlock;
                    else
                        crlURL = APIs.SCpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
                case PI:
                    if (isConnectedToRasp)
                        crlURL = APIs.RaspPIpullCrlsURL + selectedBlock;
                    else
                        crlURL = APIs.PIpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
            }*/
        }
    }

    private void downloadCRL(String url) {
        if (isConnectedToRasp) {
            AndroidNetworking.get(url)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham"))
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<RaspCrl>>() {
                            }.getType();
                            ArrayList<RaspCrl> crlListTemp = gson.fromJson(response.toString(), listType);
                            crlList.clear();
                            for (RaspCrl raspCrl : crlListTemp) {
                                for (Crl modal_crl : raspCrl.getData()) {
                                    crlList.add(modal_crl);
                                }
                            }
                            pullDataView.closeProgressDialog();
                            pullDataView.enableSaveButton();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            pullDataView.closeProgressDialog();
                            pullDataView.showErrorToast();
                        }
                    });
        } else {
            AndroidNetworking.get(url).build().getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    // do anything with response
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Crl>>() {
                    }.getType();
                    ArrayList<Crl> crlListTemp = gson.fromJson(response.toString(), listType);
                    crlList.clear();
                    crlList.addAll(crlListTemp);
                    pullDataView.closeProgressDialog();
                    pullDataView.enableSaveButton();
                }

                @Override
                public void onError(ANError error) {
                    // handle error
                    pullDataView.closeProgressDialog();
                    pullDataView.showErrorToast();
                }
            });
        }
    }

    @Override
    public void saveData() {
       /* BaseActivity.crLdao.insertAllCRL(crlList);
        BaseActivity.studentDao.insertAllStudents(studentList);
        BaseActivity.groupDao.insertAllGroups(groupList);
        BaseActivity.villageDao.insertAllVillages(villageList.get(0).getData());
       */

        AppDatabase.getDatabaseInstance(context).getCrlDao().insertAll(crlList);
        Iterator<Student> i = studentList.iterator();
        while (i.hasNext()) {
            Student stu = i.next(); // must be called before you can call i.remove()
            stu.setIsniosstudent("0");
            if (stu.getGender().equalsIgnoreCase("deleted"))
                i.remove();
        }
        AppDatabase.getDatabaseInstance(context).getStudentDao().insertAll(studentList);
        Iterator<Groups> gi = groupList.iterator();
        while (gi.hasNext()) {
            Groups stu = gi.next(); // must be called before you can call i.remove()
            if (stu.getDeviceId().equalsIgnoreCase("deleted"))
                gi.remove();
        }
        AppDatabase.getDatabaseInstance(context).getGroupsDao().insertAllGroups(groupList);
        //if (isConnectedToRasp) {
        saveDownloadedVillages();
        AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", selectedProgram);
        // } else
        // AppDatabase.getDatabaseInstance(context).getVillageDao().insertAllVillages(villageList);
        BackupDatabase.backup(context);
        pullDataView.openLoginActivity();
    }

    private void saveDownloadedVillages() {
//        List<Modal_Village> allStudent = villageList.get(0).getData();
        for (Village vill : villageList) {
            if (villageIDList.contains(String.valueOf(vill.getVillageId())))
                AppDatabase.getDatabaseInstance(context).getVillageDao().insertVillage(vill);
        }
    }


    @Override
    public void clearLists() {
        if (crlList != null) {
            crlList.clear();
        }
        if (studentList != null) {
            studentList.clear();
        }
        if (groupList != null) {
            groupList.clear();
        }
//        if (isConnectedToRasp) {
//            if (raspVillageList != null) {
//                raspVillageList.clear();
//            }
//        } else {
        if (villageList != null) {
            villageList.clear();
        }
//        }
        if (villageIDList != null) {
            villageIDList.clear();
        }
        pullDataView.clearStateSpinner();
        pullDataView.clearBlockSpinner();
        pullDataView.disableSaveButton();
    }

    @Override
    public void onSaveClick() {
//        if (isConnectedToRasp)
//            pullDataView.shoConfermationDialog(crlList.size(), studentList.size(), groupList.size(), raspVillageList.size());
//        else
        if (crlList.size() != 0 && studentList.size() != 0 && groupList.size() != 0 && villageList.size() != 0)
            pullDataView.shoConfermationDialog(crlList.size(), studentList.size(), groupList.size(), villageList.size());
        else Toast.makeText(context, R.string.no_data, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void checkConnectivity() {
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
            //callOnlineContentAPI(contentList, parentId);
        } else if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                //  if (FastSave.getInstance().getString(Assessment_Constants.FACILITY_ID, "").isEmpty())
                isConnectedToRasp = checkConnectionForRaspberry();
                //callKolibriAPI(contentList, parentId);
            } else {
                isConnectedToRasp = false;
                //callOnlineContentAPI(contentList, parentId);
            }
        } else {
            pullDataView.showNoConnectivity();
        }
    }

    public boolean checkConnectionForRaspberry() {
        boolean isRaspberry = false;
        if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                try {
                    isRaspberry = true;
                    /*JSONObject object = new JSONObject();
                    object.put("username", "pratham");
                    object.put("password", "pratham");
                    new PD_ApiRequest(context, ContentPresenterImpl.this)
                            .getacilityIdfromRaspberry(Assessment_Constants.FACILITY_ID, Assessment_Constants.RASP_IP + "/api/session/", object);*/
                } catch (Exception e) {
                    isRaspberry = false;
                    e.printStackTrace();
                }
            }
        } else isRaspberry = false;
        return isRaspberry;
    }

//    List<Village> vilageList = new ArrayList<>();

    @Override
    public void receivedContent(String header, String response) {
        Gson gson = new Gson();
        if (header.equalsIgnoreCase(Assessment_Constants.KOLIBRI_BLOCK)) {
            List<String> blockList = new ArrayList<>();
            Type listType = new TypeToken<List<RaspVillage>>() {
            }.getType();
            List<RaspVillage> raspvilageList = gson.fromJson(response, listType);
            if (raspvilageList != null) {
                if (raspvilageList.isEmpty()) {
                    blockList.add("NO BLOCKS");
                } else {
                    blockList.add("Select block");
                    for (RaspVillage raspVillage : raspvilageList) {
                        //                                    for (Modal_Village village : raspVillage.getData()) {
                        villageList.add(raspVillage.getData());
                        blockList.add(raspVillage.getData().getBlock());
                        //                                    }
                    }
                }
                LinkedHashSet hs = new LinkedHashSet(blockList);
                blockList.clear();
                blockList.addAll(hs);
                LinkedHashSet hs1 = new LinkedHashSet(villageList);
                villageList.clear();
                villageList.addAll(hs1);
                pullDataView.showBlocksSpinner(blockList);
            }
            pullDataView.closeProgressDialog();
        } else if (header.equalsIgnoreCase(Assessment_Constants.SERVER_BLOCK)) {
            List<String> blockList = new ArrayList<>();
            Type listType = new TypeToken<List<Village>>() {
            }.getType();
            villageList = gson.fromJson(response, listType);
            if (villageList != null) {
                if (villageList.isEmpty()) {
                    blockList.add("NO BLOCKS");
                } else {
                    blockList.add("Select block");
                    for (Village vill : villageList) {
                        blockList.add(vill.getBlock());
                    }
                }
                LinkedHashSet hs = new LinkedHashSet(blockList);
                blockList.clear();
                blockList.addAll(hs);
                pullDataView.showBlocksSpinner(blockList);
            }
            pullDataView.closeProgressDialog();
        } else if (header.equalsIgnoreCase(Assessment_Constants.KOLIBRI_STU)) {
            count++;
            String json = response.toString();
            gson = new Gson();
            Type listType = new TypeToken<List<RaspStudent>>() {
            }.getType();
            List<RaspStudent> studentListTemp = gson.fromJson(json, listType);
            for (RaspStudent raspStudent : studentListTemp) {
                for (Student student : raspStudent.getData()) {
                    studentList.add(student);
                }
            }
            loadGroups();
        } else if (header.equalsIgnoreCase(Assessment_Constants.SERVER_STU)) {
            count++;
            String json = response.toString();
            gson = new Gson();
            Type listType = new TypeToken<List<Student>>() {
            }.getType();
            List<Student> studentListTemp = gson.fromJson(json, listType);
            if (studentListTemp != null) {
                studentList.addAll(studentListTemp);
            }
            loadGroups();
        } else if (header.equalsIgnoreCase(Assessment_Constants.KOLIBRI_GRP)) {
            groupCount++;
            String json = response.toString();
            gson = new Gson();
            Type listType = new TypeToken<List<RaspGroup>>() {
            }.getType();
            List<RaspGroup> groupListTemp = gson.fromJson(json, listType);
            for (RaspGroup raspGroup : groupListTemp) {
                for (Groups modal_groups : raspGroup.getData()) {
                    groupList.add(modal_groups);
                }

            }
            loadCRL();
        } else if (header.equalsIgnoreCase(Assessment_Constants.SERVER_GRP)) {
            groupCount++;
            String json = response.toString();
            gson = new Gson();
            Type listType = new TypeToken<List<Groups>>() {
            }.getType();
            List<Groups> groupListTemp = gson.fromJson(json, listType);
            if (groupListTemp != null) {
                groupList.addAll(groupListTemp);
            }
            loadCRL();
        } else if (header.equalsIgnoreCase(Assessment_Constants.KOLIBRI_CRL)) {
            gson = new Gson();
            Type listType = new TypeToken<List<RaspCrl>>() {
            }.getType();
            ArrayList<RaspCrl> crlListTemp = gson.fromJson(response.toString(), listType);
            crlList.clear();
            for (RaspCrl raspCrl : crlListTemp) {
                for (Crl modal_crl : raspCrl.getData()) {
                    crlList.add(modal_crl);
                }
            }
            pullDataView.closeProgressDialog();
            pullDataView.enableSaveButton();
        } else if (header.equalsIgnoreCase(Assessment_Constants.SERVER_CRL)) {
            gson = new Gson();
            Type listType = new TypeToken<List<Crl>>() {
            }.getType();
            ArrayList<Crl> crlListTemp = gson.fromJson(response.toString(), listType);
            crlList.clear();
            crlList.addAll(crlListTemp);
            pullDataView.closeProgressDialog();
            pullDataView.enableSaveButton();
        }
    }

    @Override
    public void receivedError(String header) {

    }
}
