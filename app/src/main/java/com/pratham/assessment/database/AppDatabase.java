package com.pratham.assessment.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pratham.assessment.dao.AssessmentDao;
import com.pratham.assessment.dao.AssessmentPaperForPushDao;
import com.pratham.assessment.dao.AssessmentPaperPatternDao;
import com.pratham.assessment.dao.AssessmentPatternDetailsDao;
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
import com.pratham.assessment.dao.AssessmentTopicDao;
import com.pratham.assessment.dao.SubjectDao;
import com.pratham.assessment.dao.AssessmentTestDao;
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
        AssessmentPaperPattern.class, AssessmentPatternDetails.class, SupervisorData.class, DownloadMedia.class}, version = 1)
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

    public abstract AssessmentTopicDao getAssessmentTopicDao();

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
        if (appDatabase == null)
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "assessment_database")/*.addMigrations(MIGRATION_1_2)*/.allowMainThreadQueries().build();
        return appDatabase;
    }


    /* static final Migration MIGRATION_1_2 = new Migration(1, 2) {


       @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE  AssessmentPaperForPush  ( languageId  TEXT,  subjectId  TEXT, " +
                    " examId  TEXT,  paperId  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,  paperStartTime  TEXT," +
                    "  paperEndTime  TEXT,  outOfMarks  TEXT,  totalMarks  TEXT,  studentId  TEXT, " +
                    " CorrectCnt  INTEGER NOT NULL,  wrongCnt  INTEGER NOT NULL,  SkipCnt  INTEGER NOT NULL, " +
                    " sentFlag  INTEGER NOT NULL,  SessionID  TEXT)");

            database.execSQL("CREATE TABLE  SupervisorData  ( sId  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "  supervisorId  TEXT,  assessmentSessionId  TEXT,  supervisorName  TEXT,  supervisorPhoto  TEXT," +
                    "  sentFlag  INTEGER NOT NULL)");

            database.execSQL("drop table ScienceAssesmentAnswer");

            database.execSQL("ALTER TABLE Score add COLUMN isAttempted boolean");
            database.execSQL("ALTER TABLE Score add COLUMN isCorrect boolean");
            database.execSQL("ALTER TABLE Score add COLUMN userAnswer text");
            database.execSQL("ALTER TABLE Score add COLUMN examId text");
        }
    };*/


}
