package com.pratham.assessment.async;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.domain.EventMessage;
import com.pratham.assessment.constants.Assessment_Constants;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import static com.pratham.assessment.BaseActivity.appDatabase;
import static com.pratham.assessment.database.AppDatabase.DB_NAME;


public class CopyDbToOTG extends AsyncTask {
    String actPhotoPath;
    DocumentFile mediaFolder;
    DocumentFile newMediaFolder;
    int totalActivityFolders;
    File[] files;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            actPhotoPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/";
            Assessment_Constants.TransferredImages = 0;
            Uri treeUri = (Uri) objects[0];
            DocumentFile rootFile = DocumentFile.fromTreeUri(AssessmentApplication.getInstance(), treeUri);
            DocumentFile fca_backup_file = rootFile.findFile("Assessment_Backup_Data");
            if (fca_backup_file == null)
                fca_backup_file = rootFile.createDirectory("Assessment_Backup_Data");

            String thisdeviceFolderName = "DeviceId_" + appDatabase.getStatusDao().getValue("DeviceId");
            DocumentFile thisTabletFolder = fca_backup_file.findFile(thisdeviceFolderName);
            if (thisTabletFolder == null)
                thisTabletFolder = fca_backup_file.createDirectory(thisdeviceFolderName);

            String media_Folder = "Assessment_Media";
            DocumentFile mediaFolder = thisTabletFolder.findFile(media_Folder);
            if (mediaFolder == null)
                mediaFolder = thisTabletFolder.createDirectory(media_Folder);

            //copy db files
//            File activityPhotosFile = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH);
            File currentDB = AssessmentApplication.getInstance().getDatabasePath(DB_NAME);
            File parentPath = currentDB.getParentFile();

            for (File f : parentPath.listFiles()) {
                DocumentFile file = thisTabletFolder.findFile(f.getName());
                if (file != null) file.delete();
                file = thisTabletFolder.createFile("image", f.getName());
                OutputStream out = AssessmentApplication.getInstance().getContentResolver().openOutputStream(file.getUri());
                FileInputStream in = new FileInputStream(f.getAbsolutePath());
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                // You have now copied the file
                out.flush();
                out.close();
            }

            File activityPhotosFile = new File(actPhotoPath);
            files = activityPhotosFile.listFiles();
            if (files != null)
                totalActivityFolders = files.length;

            copyActivityData(activityPhotosFile, mediaFolder);


/*            if (activityPhotosFile.exists()) {
                File[] files = activityPhotosFile.listFiles();
                Assessment_Constants.TransferredImages = files.length;
                for (int i = 0; i < files.length; i++) {
                    Log.d("Files", "FileName:" + files[i].getName());
//                    DocumentFile file = mediaFolder.findFile(files[i].getName());
//                    if (file != null) file.delete();
                    DocumentFile file = mediaFolder.createFile("image", files[i].getName());
                    OutputStream out = AssessmentApplication.getInstance().getContentResolver().openOutputStream(file.getUri());
                    FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    // You have now copied the file
                    out.flush();
                    out.close();
                }
            }*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private void copyActivityData(File activityPhotosFile, DocumentFile parentFolder) {
        try {
            Log.d("!!!", "inside copyActivityData");

            DocumentFile currentFolder = parentFolder.findFile(activityPhotosFile.getName());
            if (currentFolder == null)
                currentFolder = parentFolder.createDirectory(activityPhotosFile.getName());
            File[] files = activityPhotosFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    Log.d("Files", "\nDirectory : " + file.getName());//CanonicalPath());
                    copyActivityData(file, currentFolder);
                } else {
                    Log.d("Files", "\nFile : " + file.getName());//CanonicalPath());
                    DocumentFile dFile = currentFolder.createFile("image", file.getName());
                    OutputStream out = AssessmentApplication.getInstance().getContentResolver().openOutputStream(dFile.getUri());
                    FileInputStream in = new FileInputStream(file.getAbsolutePath());
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    out.flush();
                    out.close();
                    Assessment_Constants.TransferredImages++;
                }
            }
            Log.d("!!!", "inside copyActivityData for");

        } catch (Exception e) {
            Log.d("!!!", "qqqqqqq");

            e.printStackTrace();
        }
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        EventMessage message = new EventMessage();
        if ((boolean) o) message.setMessage(Assessment_Constants.BACKUP_DB_COPIED);
        else message.setMessage(Assessment_Constants.BACKUP_DB_NOT_COPIED);
        EventBus.getDefault().post(message);
    }
}
