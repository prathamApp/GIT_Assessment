package com.pratham.assessment.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pratham.assessment.dao.AssessmentDao;
import com.pratham.assessment.dao.AssessmentPaperForPushDao;
import com.pratham.assessment.dao.AssessmentPaperPatternDao;
import com.pratham.assessment.dao.AssessmentPatternDetailsDao;
import com.pratham.assessment.dao.AssessmentTestDao;
import com.pratham.assessment.dao.AttendanceDao;
import com.pratham.assessment.dao.ContentTableDao;
import com.pratham.assessment.dao.CrlDao;
import com.pratham.assessment.dao.DownloadMediaDao;
import com.pratham.assessment.dao.GroupDao;
import com.pratham.assessment.dao.LanguageDao;
import com.pratham.assessment.dao.LogDao;
import com.pratham.assessment.dao.ScienceQuestionChoiceDao;
import com.pratham.assessment.dao.ScienceQuestionDao;
import com.pratham.assessment.dao.ScoreDao;
import com.pratham.assessment.dao.SessionDao;
import com.pratham.assessment.dao.StatusDao;
import com.pratham.assessment.dao.StudentDao;
import com.pratham.assessment.dao.SubjectDao;
import com.pratham.assessment.dao.SupervisorDataDao;
import com.pratham.assessment.dao.VillageDao;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.domain.AssessmentPatternDetails;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.AssessmentToipcsModal;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Status;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.domain.Village;


@Database(entities = {Crl.class, Student.class, Score.class, Session.class, Attendance.class,
        Status.class, Village.class, Groups.class, Assessment.class, Modal_Log.class,
        ContentTable.class, AssessmentToipcsModal.class, ScienceQuestion.class,
        ScienceQuestionChoice.class, AssessmentSubjects.class, AssessmentLanguages.class,
        AssessmentTest.class, AssessmentPaperForPush.class,
        AssessmentPaperPattern.class, AssessmentPatternDetails.class,
        SupervisorData.class, DownloadMedia.class}, version = 5/*,exportSchema = false*/)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase appDatabase;

    public static final String DB_NAME = "assessment_database";

    public abstract CrlDao getCrlDao();

    public abstract StudentDao getStudentDao();

    public abstract ScoreDao getScoreDao();

    public abstract AssessmentDao getAssessmentDao();

    public abstract SessionDao getSessionDao();

    public abstract AttendanceDao getAttendanceDao();

    public abstract VillageDao getVillageDao();

    public abstract GroupDao getGroupsDao();

    public abstract LogDao getLogsDao();

    public abstract ContentTableDao getContentTableDao();

    public abstract StatusDao getStatusDao();

//    public abstract AssessmentTopicDao getAssessmentTopicDao();

    public abstract ScienceQuestionDao getScienceQuestionDao();

    public abstract ScienceQuestionChoiceDao getScienceQuestionChoicesDao();

    public abstract SubjectDao getSubjectDao();

    public abstract LanguageDao getLanguageDao();

    public abstract AssessmentTestDao getTestDao();

    public abstract AssessmentPaperForPushDao getAssessmentPaperForPushDao();

    public abstract AssessmentPaperPatternDao getAssessmentPaperPatternDao();

    public abstract AssessmentPatternDetailsDao getAssessmentPatternDetailsDao();

    public abstract SupervisorDataDao getSupervisorDataDao();

    public abstract DownloadMediaDao getDownloadMediaDao();



   /* public static AppDatabase getDatabaseInstance(Context context) {
        if(appDatabase!=null) {
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, AppDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }*/

    public static AppDatabase getDatabaseInstance(Context context) {
        Log.d("$$$", "getDatabaseInstance");
        try {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "assessment_database")
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5)
                        .allowMainThreadQueries().build();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appDatabase;
    }


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {


        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            Log.d("$$$", "MIGRATION_1_2");

            database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion1 text");
            database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion2 text");
            database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion3 text");
            Log.d("$$$", "MIGRATION_1_2After");
//            database.execSQL("ALTER TABLE Score add COLUMN certificateQuestion1");
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_2_3");
            try {

                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question1Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question2Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question3Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN FullName text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN Gender text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN Age INTEGER not null DEFAULT 0");
                Log.d("$$$", "MIGRATION_2_3After");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_3_4");
            try {

                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question4Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question5Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion4 text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion5 text");
                Log.d("$$$", "MIGRATION_3_3After");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_4_5");
            try {

                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question6Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question7Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question8Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question9Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question10Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion6 text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion7 text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion8 text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion9 text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion10 text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN IsRandom BIT default 'false'");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN noofcertificateq text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN exammode text");
                Log.d("$$$", "MIGRATION_4_5After");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

}
