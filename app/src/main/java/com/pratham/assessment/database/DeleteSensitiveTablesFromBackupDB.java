package com.pratham.assessment.database;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;

public class DeleteSensitiveTablesFromBackupDB {
   public static void deleteTables() {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    SQLiteDatabase db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrathamBackups" + "/assessment_database", null, SQLiteDatabase.OPEN_READWRITE);
                    if (db != null) {

                        db.execSQL("drop table if exists ScienceQuestion");
                        db.execSQL("drop table if exists ScienceQuestionChoice");
                        db.execSQL("drop table if exists TempScienceQuestion");
//                        db.execSQL("drop table if exists Score");
//                        db.execSQL("drop table if exists DownloadMedia");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }

        }.execute();
    }

}

