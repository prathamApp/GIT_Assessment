package com.pratham.assessment.custom;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.async.DownloadingTask;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Modal_Download;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by User on 16/11/15.
 */
public class ZipDownloader {

    private static final String TAG = ZipDownloader.class.getSimpleName();
    String filename;
    Context context;

    public ZipDownloader(Context context) {
        this.context = context;
    }

    public void initialize(Context context, String url,
                           String foldername, String f_name, ContentTable contentDetail,
                           ArrayList<ContentTable> levelContents) {
        this.filename = f_name;
//        if (!PrathamApplication.contentExistOnSD) {
        createFolderAndStartDownload(url, foldername, f_name, contentDetail, context, levelContents);
        /*} else {
            createOverSdCardAndStartDownload(url, foldername, f_name, contentDetail, contentPresenter, levelContents);
        }*/
    }

    /*Creating folder in internal.
     *That internal might be of android internal or sdcard internal (if available and writable)
     * */
    private void createFolderAndStartDownload(String url, String foldername, String f_name,
                                              ContentTable contentDetail,
                                              Context context,
                                              ArrayList<ContentTable> levelContents) {
        File mydir = null;
        mydir = new File(AssessmentApplication.contentSDPath+ "/.Assessment");
        if (!mydir.exists()) mydir.mkdirs();
        mydir = new File(AssessmentApplication.contentSDPath + "/.Assessment/English/");
        if (!mydir.exists()) mydir.mkdirs();
            mydir = new File(AssessmentApplication.contentSDPath + "/.Assessment/English/" + foldername);
        if (!mydir.exists()) mydir.mkdirs();
        if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
            if (foldername.equalsIgnoreCase(Assessment_Constants.GAME)) {
                f_name = f_name.substring(0, f_name.lastIndexOf("."));
                File temp_dir = new File(mydir.getAbsolutePath() + "/" + f_name);
                if (!temp_dir.exists()) temp_dir.mkdirs();
                mydir = temp_dir;
            }
        }
        Log.d("internal_file", mydir.getAbsolutePath());

        Modal_Download modal_download = new Modal_Download();
        modal_download.setUrl(url);
        modal_download.setDir_path(mydir.getAbsolutePath());
        modal_download.setF_name(filename);
        modal_download.setFolder_name(foldername);
        modal_download.setContent(contentDetail);
        modal_download.setLevelContents(levelContents);
        new DownloadingTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, modal_download);
    }


    private void createOverSdCardAndStartDownload(String url, String foldername, String f_name,
                                                  ContentTable contentDetail,
                                                  ArrayList<ContentTable> levelContents) {
        String path = FastSave.getInstance().getString(Assessment_Constants.ext_path, "");
        if (path.isEmpty())
            return;
        DocumentFile documentFile = DocumentFile.fromFile(new File(path));
        if (documentFile.findFile("/.Assessment/English" + foldername) != null)
            documentFile = documentFile.findFile("/.Assessment/English" + foldername);
        else
            documentFile = documentFile.createDirectory("/.Assessment/English" + foldername);
        if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
            if (foldername.equalsIgnoreCase(Assessment_Constants.GAME)) {
                f_name = f_name.substring(0, f_name.lastIndexOf("."));
                if (documentFile.findFile(f_name) != null)
                    documentFile = documentFile.findFile(f_name);
                else
                    documentFile = documentFile.createDirectory(f_name);
            }
        }
        Modal_Download modal_download = new Modal_Download();
        modal_download.setUrl(url);
        modal_download.setDir_path(FileUtils.getPath(AssessmentApplication.getInstance(), documentFile.getUri()));
        modal_download.setF_name(filename);
        modal_download.setFolder_name(foldername);
        modal_download.setContent(contentDetail);
        modal_download.setLevelContents(levelContents);
        new DownloadingTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, modal_download);
    }

}





