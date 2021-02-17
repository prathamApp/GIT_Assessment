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
import com.pratham.assessment.dao.AssessmentTopicDao;
import com.pratham.assessment.dao.AttendanceDao;
import com.pratham.assessment.dao.CertificateKeywordRatingDao;
import com.pratham.assessment.dao.CertificateTopicListDao;
import com.pratham.assessment.dao.ContentTableDao;
import com.pratham.assessment.dao.CrlDao;
import com.pratham.assessment.dao.DownloadMediaDao;
import com.pratham.assessment.dao.GroupDao;
import com.pratham.assessment.dao.LanguageDao;
import com.pratham.assessment.dao.LogDao;
import com.pratham.assessment.dao.NIOSExamDao;
import com.pratham.assessment.dao.NIOSExamTopicDao;
import com.pratham.assessment.dao.ScienceQuestionChoiceDao;
import com.pratham.assessment.dao.ScienceQuestionDao;
import com.pratham.assessment.dao.ScoreDao;
import com.pratham.assessment.dao.SessionDao;
import com.pratham.assessment.dao.StatusDao;
import com.pratham.assessment.dao.StudentDao;
import com.pratham.assessment.dao.SubjectDao;
import com.pratham.assessment.dao.SupervisorDataDao;
import com.pratham.assessment.dao.TempScienceQuestionDao;
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
import com.pratham.assessment.domain.CertificateKeywordRating;
import com.pratham.assessment.domain.CertificateTopicList;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.domain.NIOSExam;
import com.pratham.assessment.domain.NIOSExamTopics;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Status;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.domain.TempScienceQuestion;
import com.pratham.assessment.domain.Village;


@Database(entities = {Crl.class, Student.class, Score.class, Session.class, Attendance.class,
        Status.class, Village.class, Groups.class, Assessment.class, Modal_Log.class,
        ContentTable.class, AssessmentToipcsModal.class, ScienceQuestion.class,
        ScienceQuestionChoice.class, AssessmentSubjects.class, AssessmentLanguages.class,
        AssessmentTest.class, AssessmentPaperForPush.class,
        AssessmentPaperPattern.class, AssessmentPatternDetails.class,
        SupervisorData.class, DownloadMedia.class, TempScienceQuestion.class,
        NIOSExam.class, NIOSExamTopics.class, CertificateTopicList.class, CertificateKeywordRating.class}, version = 14/*,exportSchema = false*/)
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

    public abstract CertificateTopicListDao getCertificateTopicListDao();

    public abstract SupervisorDataDao getSupervisorDataDao();

    public abstract DownloadMediaDao getDownloadMediaDao();

    public abstract TempScienceQuestionDao getTempScienceQuestionDao();

    public abstract NIOSExamDao getNiosExamDao();

    public abstract NIOSExamTopicDao getNiosExamTopicDao();

    public abstract CertificateKeywordRatingDao getCertificateKeywordRatingDao();



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
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,
                                MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8,
                                MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12,
                                MIGRATION_12_13,MIGRATION_13_14)
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
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN IsRandom INTEGER not null DEFAULT 0");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN noofcertificateq text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN exammode text");
                Log.d("$$$", "MIGRATION_4_5After");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    static final Migration MIGRATION_5_6 = new Migration(5, 6) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_5_6");
            try {

                database.execSQL("ALTER TABLE ScienceQuestion add COLUMN IsParaQuestion INTEGER not null DEFAULT 0");
                database.execSQL("ALTER TABLE ScienceQuestion add COLUMN RefParaID text");
                Log.d("$$$", "after MIGRATION_5_6");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_6_7");
            try {

                database.execSQL("ALTER TABLE AssessmentPatternDetails add COLUMN paralevel text");
                database.execSQL("ALTER TABLE AssessmentPatternDetails add COLUMN qlevelmarks text");
                Log.d("$$$", "after MIGRATION_6_7");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    static final Migration MIGRATION_7_8 = new Migration(7, 8) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_7_8");
            try {

                database.execSQL("CREATE TABLE `TempScienceQuestion` (\n" +
                        "\t`ansdesc`\tTEXT,\n" +
                        "\t`updatedby`\tTEXT,\n" +
                        "\t`qlevel`\tTEXT,\n" +
                        "\t`addedby`\tTEXT,\n" +
                        "\t`languageid`\tTEXT,\n" +
                        "\t`active`\tTEXT,\n" +
                        "\t`lessonid`\tTEXT,\n" +
                        "\t`qtid`\tTEXT,\n" +
                        "\t`qid`\tTEXT NOT NULL ,\n" +
                        "\t`subjectid`\tTEXT,\n" +
                        "\t`addedtime`\tTEXT,\n" +
                        "\t`updatedtime`\tTEXT,\n" +
                        "\t`photourl`\tTEXT,\n" +
                        "\t`examtime`\tTEXT,\n" +
                        "\t`topicid`\tTEXT,\n" +
                        "\t`answer`\tTEXT,\n" +
                        "\t`outofmarks`\tTEXT,\n" +
                        "\t`qname`\tTEXT,\n" +
                        "\t`hint`\tTEXT,\n" +
                        "\t`examid`\tTEXT,\n" +
                        "\t`pdid`\tTEXT,\n" +
                        "\t`startTime`\tTEXT,\n" +
                        "\t`endTime`\tTEXT,\n" +
                        "\t`marksPerQuestion`\tTEXT,\n" +
                        "\t`userAnswerId`\tTEXT,\n" +
                        "\t`userAnswer`\tTEXT,\n" +
                        "\t`isAttempted`\tINTEGER NOT NULL,\n" +
                        "\t`isCorrect`\tINTEGER NOT NULL,\n" +
                        "\t`IsParaQuestion`\tINTEGER NOT NULL,\n" +
                        "\t`RefParaID`\tTEXT,\n" +
                        "\t`SessionID`\tTEXT,\n" +
                        "\t`StudentID`\tTEXT,\n" +
                        "\t`DeviceID`\tTEXT,\n" +
                        "\t`ScoredMarks`\tINTEGER NOT NULL,\n" +
                        "\t`paperTotalMarks`\tINTEGER NOT NULL,\n" +
                        "\t`paperStartDateTime`\tTEXT,\n" +
                        "\t`paperEndDateTime`\tTEXT,\n" +
                        "\t`Level`\tINTEGER NOT NULL,\n" +
                        "\t`Label`\tTEXT,\n" +
                        "\t`sentFlag`\tINTEGER NOT NULL,\n" +
                        "\t`paperId`\tTEXT NOT NULL ,\n" +
                        "\tPRIMARY KEY(`qid`,`paperId`)\n" +
                        ")");

                Log.d("$$$", "after MIGRATION_7_8");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_8_9");
            try {
                database.execSQL("ALTER TABLE Student add COLUMN isniosstudent text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN isniosstudent text");
                database.execSQL("CREATE TABLE `NIOSExam` (`studentid` TEXT, `subjectname` TEXT, `examname` TEXT, `examid` TEXT  NOT NULL, `languageid` TEXT, `subjectid` TEXT, `languagename` TEXT, PRIMARY KEY(`examid`))");
                database.execSQL("CREATE TABLE `NIOSExamTopics` (`subjectname` TEXT, `topicid` TEXT, `examname` TEXT, `examid` TEXT NOT NULL, `languageid` TEXT, `topicname` TEXT, `subjectid` TEXT, `languagename` TEXT, PRIMARY KEY(`examid`))");
                Log.d("$$$", "after MIGRATION_8_9");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_9_10");
            try {
                database.execSQL("ALTER TABLE AssessmentTest add COLUMN examtype text");
                Log.d("$$$", "after MIGRATION_9_10");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    static final Migration MIGRATION_10_11 = new Migration(10, 11) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_10_11");
            try {

                database.execSQL("CREATE TABLE IF NOT EXISTS `NIOSExam_NEW` (`studentid` TEXT NOT NULL," +
                        " `subjectname` TEXT, `examname` TEXT, `examid` TEXT NOT NULL, `languageid` TEXT," +
                        " `subjectid` TEXT, `languagename` TEXT," +
                        " PRIMARY KEY(`studentid`,`examid`))");
                database.execSQL("INSERT INTO NIOSExam_NEW (studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename) SELECT studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename FROM NIOSExam");
                database.execSQL("DROP TABLE NIOSExam");
                database.execSQL("ALTER TABLE NIOSExam_NEW RENAME TO NIOSExam");


             /*   database.execSQL("ALTER TABLE NIOSExamTopics add COLUMN studentid TEXT NOT NULL");

                database.execSQL("CREATE TABLE IF NOT EXISTS `NIOSExamTopics_NEW` (`studentid` TEXT NOT NULL," +
                        " `subjectname` TEXT, `examname` TEXT, `examid` TEXT NOT NULL, `languageid` TEXT," +
                        " `subjectid` TEXT, `languagename` TEXT," + " `topicid` TEXT, `topicname` TEXT," +
                        " PRIMARY KEY(`studentid`,`examid`))");
                database.execSQL("INSERT INTO NIOSExamTopics_NEW (studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename,topicid,topicname) SELECT studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename,topicid,topicname FROM NIOSExamTopics");
                database.execSQL("DROP TABLE NIOSExamTopics");
                database.execSQL("ALTER TABLE NIOSExamTopics_NEW RENAME TO NIOSExamTopics");


                Log.d("$$$", "after MIGRATION_10_11");*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    static final Migration MIGRATION_11_12 = new Migration(11, 12) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_11_12");
            try {

                database.execSQL("CREATE TABLE IF NOT EXISTS `NIOSExam_NEW` (`studentid` TEXT NOT NULL," +
                        " `subjectname` TEXT, `examname` TEXT, `examid` TEXT NOT NULL, `languageid` TEXT," +
                        " `subjectid` TEXT, `languagename` TEXT," +
                        " PRIMARY KEY(`studentid`,`examid`))");
                database.execSQL("INSERT INTO NIOSExam_NEW (studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename) SELECT studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename FROM NIOSExam");
                database.execSQL("DROP TABLE NIOSExam");
                database.execSQL("ALTER TABLE NIOSExam_NEW RENAME TO NIOSExam");


              /*  database.execSQL("ALTER TABLE NIOSExamTopics add COLUMN studentid TEXT NOT NULL");

                database.execSQL("CREATE TABLE IF NOT EXISTS `NIOSExamTopics_NEW` (`studentid` TEXT NOT NULL," +
                        " `subjectname` TEXT, `examname` TEXT, `examid` TEXT NOT NULL, `languageid` TEXT," +
                        " `subjectid` TEXT, `languagename` TEXT," + " `topicid` TEXT, `topicname` TEXT," +
                        " PRIMARY KEY(`studentid`,`examid`))");
                database.execSQL("INSERT INTO NIOSExamTopics_NEW (studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename,topicid,topicname) SELECT studentid, subjectname, examname,examid," +
                        "languageid,subjectid,languagename,topicid,topicname FROM NIOSExamTopics");
                database.execSQL("DROP TABLE NIOSExamTopics");
                database.execSQL("ALTER TABLE NIOSExamTopics_NEW RENAME TO NIOSExamTopics");

*/
                Log.d("$$$", "after MIGRATION_11_12");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    static final Migration MIGRATION_12_13 = new Migration(12, 13) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_12_13");
            try {
                database.execSQL("ALTER TABLE ScienceQuestion add COLUMN IsQuestionFromSDCard INTEGER not null DEFAULT 0");
                database.execSQL("ALTER TABLE TempScienceQuestion add COLUMN IsQuestionFromSDCard INTEGER not null DEFAULT 0");
                Log.d("$$$", "after MIGRATION_12_13");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    static final Migration MIGRATION_13_14 = new Migration(13, 14) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_13_14");
            try {
                database.execSQL("CREATE TABLE `CertificateTopicList` (`certificatequestionid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `certificatequestion` TEXT, `certificatekeyword` TEXT,`subjectid` TEXT,`examid` TEXT)");
                database.execSQL("CREATE TABLE `CertificateKeywordRating` (`keywordRatingId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `paperId` TEXT, `certificatequestion` TEXT, `certificatekeyword` TEXT, `rating` TEXT,`subjectId` TEXT,`examId` TEXT,`studentId` TEXT,`languageId` TEXT,`sentFlag` INTEGER NOT NULL)");
                database.execSQL("ALTER TABLE AssessmentPatternDetails add COLUMN keyworddetail TEXT");
                database.execSQL("ALTER TABLE ScienceQuestion add COLUMN revisitedStartTime TEXT");
                database.execSQL("ALTER TABLE ScienceQuestion add COLUMN revisitedEndTime TEXT");
                database.execSQL("ALTER TABLE Score add COLUMN revisitedStartDateTime TEXT");
                database.execSQL("ALTER TABLE Score add COLUMN revisitedEndDateTime TEXT");

                Log.d("$$$", "after MIGRATION_13_14");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
