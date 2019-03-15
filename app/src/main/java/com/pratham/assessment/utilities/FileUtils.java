package com.pratham.assessment.utilities;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FileUtils {

    private static final String PRIMARY_VOLUME_NAME = "primary";

    public static boolean isSdcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File isSdcard0Exist() {
        return Environment.getExternalStorageDirectory();
    }

    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (SDCardUtil.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if (PRIMARY_VOLUME_NAME.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (SDCardUtil.isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return SDCardUtil.getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (SDCardUtil.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type))
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                else if ("video".equals(type))
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                else if ("audio".equals(type))
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return SDCardUtil.getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme()))
            return SDCardUtil.getDataColumn(context, uri, null, null);
            // File
        else if ("file".equalsIgnoreCase(uri.getScheme()))
            return uri.getPath();

        return null;
    }


    public static void delFolder(String folderPath) {
        delAllFile(folderPath);
        String filePath = folderPath;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        myFilePath.delete();
    }

    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        int mLength = tempList.length;
        for (int i = 0; i < mLength; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
            }
        }
    }

    public static Uri getUriFromFile(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "Unknown size";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String getPathByFullPath(String fullpath) {
        return fullpath.substring(0, fullpath.lastIndexOf(File.separator));
    }

    public static String getNameByPath(String path) {
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    public static boolean isFileExists(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return false;
            }

        } catch (Exception e) {
            // handle exception
            return false;
        }
        return true;
    }

    public static ArrayList<String> getExtSdCardPaths(Context con) {
        ArrayList<String> paths = new ArrayList<String>();
        File[] files = ContextCompat.getExternalFilesDirs(con, "external");
        File firstFile = files[0];
        for (File file : files) {
            if (file != null && !file.equals(firstFile)) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Log.w("", "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    paths.add(path);
                }
            }
        }
        return paths;
    }

    /*public static List<PictureFolderEntity> getPictureFolderList(Context context) {
        List<PictureFolderEntity> list = new ArrayList<PictureFolderEntity>();

        HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();

        ContentResolver mContentResolver = context.getContentResolver();
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                String path = mCursor.getString(_date);
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                PictureFolderEntity pictureFoldery = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    pictureFoldery = new PictureFolderEntity();
                    pictureFoldery.setDir(dirPath);
                    pictureFoldery.setFirstImagePath(path);
                    list.add(pictureFoldery);
                    // Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, list.indexOf(pictureFoldery));
                } else {
                    pictureFoldery = list.get(tmpDir.get(dirPath));
                }
                pictureFoldery.images.add(new PictureEntity(path));
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
        }
        tmpDir = null;
        return list;
    }

    public static List<MusicEntity> getMusicList(Context context) {
        List<MusicEntity> list = new ArrayList<MusicEntity>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA
                }, null,
                null,
                null);
        while (cursor.moveToNext()) {
            MusicEntity music = new MusicEntity(cursor.getString(5));
            music.setTitle(cursor.getString(0));
            music.setDuration(cursor.getLong(1));
            music.setArtist(cursor.getString(2));
            music.setId(cursor.getInt(3));
            music.setDisplayName(cursor.getString(4));
            // L.i(music.toString());
            if (music.getDuration() < 60 * 1000)
                continue;
            list.add(music);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public static List<VedioEntity> getVedioList(Context context) {
        List<VedioEntity> list = new ArrayList<VedioEntity>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, //
                new String[]{MediaStore.Video.Media._ID,//
                        MediaStore.Video.Media.DATA, //
                        MediaStore.Video.Media.DURATION, //
                        MediaStore.Video.Media.DISPLAY_NAME,//
                        MediaStore.Video.Media.SIZE //
                }, null, // 查询条件，相当于sql中的where语句
                null, // 查询条件中使用到的数据
                null);
        while (cursor.moveToNext()) {
            VedioEntity vedio = new VedioEntity(cursor.getString(1));
            vedio.setId(cursor.getInt(0));
            vedio.setDuration(cursor.getLong(2));
            vedio.setDisplayName(cursor.getString(3));
//            vedio.setSize(cursor.getLong(4));
            list.add(vedio);
        }
        return list;
    }*/

    public static File fileFromAsset(Context context, String assetName) throws IOException {
        File outFile = new File(context.getCacheDir(), assetName + "-pdfview.pdf");
        if (assetName.contains("/")) {
            outFile.getParentFile().mkdirs();
        }
        copy(context.getAssets().open(assetName), outFile);
        return outFile;
    }

    public static void copy(InputStream inputStream, File output) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(output);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }
/*
    public static String getProjectDir() {
        File file = new File(getSDPath() + "/WifiProject");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getProjectPictureDir() {
        File file = new File(getProjectDir() + "/pictures");
        if (!file.exists()) {
            file.mkdirs();
        } else {
            for (File f : file.listFiles()) {
                f.delete();
            }
        }
        return file.getAbsolutePath();
    }*/
}
