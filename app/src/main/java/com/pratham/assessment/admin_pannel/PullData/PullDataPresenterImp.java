package com.pratham.assessment.admin_pannel.PullData;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.APIs;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.Assessment_Constants;
import com.pratham.assessment.R;
import com.pratham.assessment.dao.StatusDao;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.RaspCrl;
import com.pratham.assessment.domain.RaspGroup;
import com.pratham.assessment.domain.RaspStudent;
import com.pratham.assessment.domain.RaspVillage;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.Village;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static com.pratham.assessment.APIs.ECE;
import static com.pratham.assessment.APIs.PI;
import static com.pratham.assessment.APIs.RI;
import static com.pratham.assessment.APIs.SC;
import static com.pratham.assessment.APIs.UP;

/**
 * Created by PEF on 20/11/2018.
 */

public class PullDataPresenterImp implements PullDataContract.PullDataPresenter {
    Context context;
    PullDataContract.PullDataView pullDataView;
    String selectedBlock;
    String selectedProgram;
    int count = 0;
    int groupCount = 0;
    ArrayList<Village> villageList;
    ArrayList<RaspVillage> raspVillageList = new ArrayList<>();

    List<Crl> crlList = new ArrayList<>();
    List<Student> studentList = new ArrayList();
    List<Groups> groupList = new ArrayList();
    List<String> villageIDList = new ArrayList();
    Boolean isConnectedToRasp = false;

    public PullDataPresenterImp(Context context, PullDataContract.PullDataView pullDataView) {
        this.context = context;
        this.pullDataView = pullDataView;
    }

    @Override
    public void loadSpinner() {
        String[] states = context.getResources().getStringArray(R.array.india_states);
        pullDataView.showStatesSpinner(states);
    }

    @Override
    public void proccessVillageData(String block) {
        ArrayList<Village> villageName = new ArrayList();
        if (isConnectedToRasp) {
            for (RaspVillage raspVillage : raspVillageList) {
               Village village = raspVillage.getData();
                    if (block.equalsIgnoreCase(village.getBlock().trim()))
                        villageName.add(new Village(village.getVillageId(), village.getVillageName()));
                }
        } else {
            for (Village village : villageList) {
                if (block.equalsIgnoreCase(village.getBlock().trim()))
                    villageName.add(new Village(village.getVillageId(), village.getVillageName()));
            }
        }
        if (!villageName.isEmpty()) {
            pullDataView.showVillageDialog(villageName);
        }
    }

    @Override
    public void loadBlockSpinner(int pos, String selectedProgram) {
        String[] statesCodes = context.getResources().getStringArray(R.array.india_states_shortcode);
        selectedBlock = statesCodes[pos];
        this.selectedProgram = selectedProgram;
        StatusDao statusDao = AppDatabase.getDatabaseInstance(context).getStatusDao();
        statusDao.updateValue("programId", "" + selectedProgram);

        pullDataView.showProgressDialog("loading Blocks");
        String url;
        checkConnectivity();
        switch (selectedProgram) {
            case APIs.HL:
                if (isConnectedToRasp)
                    url = APIs.RaspHLpullVillagesURL + selectedBlock;
                else
                    url = APIs.HLpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
            case UP:
                if (isConnectedToRasp)
                    url = APIs.RaspUPpullVillagesURL + selectedBlock;
                else
                    url = APIs.UPpullVillagesURL + selectedBlock;
                downloadblock(url);
                break;
            case ECE:
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
        }
    }

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
        for (String id : villageIDList) {
            String url;
            switch (selectedProgram) {
                case APIs.HL:
                    if (isConnectedToRasp)
                        url = APIs.RaspHLpullStudentsURL + id;
                    else
                        url = APIs.HLpullStudentsURL + id;
                    loadStudent(url);
                    break;
                case UP:
                    if (isConnectedToRasp)
                        url = APIs.RaspUPpullStudentsURL + id;
                    else
                        url = APIs.UPpullStudentsURL + id;
                    loadStudent(url);
                    break;
                case ECE:
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

        }
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
            for (String id : villageIDList) {
                switch (selectedProgram) {
                    case APIs.HL:
                        if (isConnectedToRasp)
                            urlgroup = APIs.RaspHLpullGroupsURL + id;
                        else
                            urlgroup = APIs.HLpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                    case UP:
                        if (isConnectedToRasp)
                            urlgroup = APIs.RaspUPpullGroupsURL + id;
                        else
                            urlgroup = APIs.UPpullGroupsURL + id;
                        downloadGroups(urlgroup);
                        break;
                    case ECE:
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
            }
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
            switch (selectedProgram) {
                case APIs.HL:
                    if (isConnectedToRasp)
                        crlURL = APIs.RaspHLpullCrlsURL + selectedBlock;
                    else
                        crlURL = APIs.HLpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
                case UP:
                    if (isConnectedToRasp)
                        crlURL = APIs.RaspUPpullCrlsURL + selectedBlock;
                    else
                        crlURL = APIs.UPpullCrlsURL + selectedBlock;
                    downloadCRL(crlURL);
                    break;
                case ECE:
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
            }
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
        AppDatabase.getDatabaseInstance(context).getStudentDao().insertAll(studentList);
        AppDatabase.getDatabaseInstance(context).getGroupsDao().insertAllGroups(groupList);
        //if (isConnectedToRasp) {
        saveDownloadedVillages();
        // } else
        // AppDatabase.getDatabaseInstance(context).getVillageDao().insertAllVillages(villageList);
        BackupDatabase.backup(context);

        switch (selectedProgram) {
            case APIs.HL:
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", "1");
//                BaseActivity.statusDao.updateValue("programId", "1");
                break;
            case RI:
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", "2");
//                BaseActivity.statusDao.updateValue("programId", "2");
                break;
            case ECE:
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", "8");
                break;
            case UP:
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", "6");
                break;
            case SC:
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", "3");
//                BaseActivity.statusDao.updateValue("programId", "3");
                break;
            case PI:
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", "4");
//                BaseActivity.statusDao.updateValue("programId", "4");
                break;
            default:
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("programId", "1");
//                BaseActivity.statusDao.updateValue("programId", "1");
                break;
        }
        Toast.makeText(context, "Data Pulled Successful !", Toast.LENGTH_SHORT).show();
        pullDataView.openLoginActivity();
    }

    private void saveDownloadedVillages() {
//        List<Modal_Village> allStudent = villageList.get(0).getData();
        if (isConnectedToRasp) {
            for (RaspVillage vill : raspVillageList) {
               Village v = vill.getData();
                    if (villageIDList.contains(String.valueOf(v.getVillageId())))
                        AppDatabase.getDatabaseInstance(context).getVillageDao().insertVillage(v);

            }
        } else {
            for (Village vill : villageList) {
                if (villageIDList.contains(String.valueOf(vill.getVillageId())))
                    AppDatabase.getDatabaseInstance(context).getVillageDao().insertVillage(vill);
            }
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
        if (isConnectedToRasp) {
            if (raspVillageList != null) {
                raspVillageList.clear();
            }
        } else {
            if (villageList != null) {
                villageList.clear();
            }
        }
        if (villageIDList != null) {
            villageIDList.clear();
        }
        pullDataView.clearStateSpinner();
        pullDataView.clearBlockSpinner();
        pullDataView.disableSaveButton();
    }

    @Override
    public void onSaveClick() {
        if (isConnectedToRasp)
            pullDataView.shoConfermationDialog(crlList.size(), studentList.size(), groupList.size(), raspVillageList.size());
        else
            pullDataView.shoConfermationDialog(crlList.size(), studentList.size(), groupList.size(), villageList.size());
    }

    @Override
    public void checkConnectivity() {
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
            //callOnlineContentAPI(contentList, parentId);
        } else if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                //  if (FastSave.getInstance().getString(PD_Constant.FACILITY_ID, "").isEmpty())
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
                            .getacilityIdfromRaspberry(PD_Constant.FACILITY_ID, PD_Constant.RASP_IP + "/api/session/", object);*/
                } catch (Exception e) {
                    isRaspberry = false;
                    e.printStackTrace();
                }
            }
        } else isRaspberry = false;
        return isRaspberry;
    }

}
