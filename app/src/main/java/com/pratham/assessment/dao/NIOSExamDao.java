package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.NIOSExam;

import java.util.List;

@Dao
public interface NIOSExamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllExams(List<NIOSExam> exams);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSubject(NIOSExam subject);

    @Query("DELETE FROM NIOSExam")
    public void deleteAllSubjects();

    @Query("DELETE FROM NIOSExam where studentid=:studId")
    public void deleteByStudId(String studId);

    @Query("SELECT * FROM NIOSExam ")
    public List<NIOSExam> getAllSubjects();

    @Query("SELECT * FROM NIOSExam where languageid=:langId")
    public List<NIOSExam> getAllSubjectsByLangId(String langId);

    @Query("SELECT * FROM NIOSExam where studentid=:studId ")
    public List<NIOSExam> getAllSubjectsByStudId(String studId);

    @Query("SELECT * FROM NIOSExam where studentid=:studId and subjectid=:subId")
    public List<NIOSExam> getAllSubjectsByStudIdSubId(String studId, String subId);
//    @Query("SELECT * FROM Groups WHERE VillageID=:vID ORDER BY GroupName ASC")
//    public List<Groups> GetGroups(int vID);

    @Query("select subjectid FROM NIOSExam WHERE subjectname=:subName")
    public String getIdByName(String subName);

    @Query("select * FROM NIOSExam WHERE subjectid=:subId")
    public NIOSExam getSubjectById(String subId);


}