package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.pratham.assessment.domain.Assessment;

import java.util.List;


@Dao
public interface AssessmentDao {

    @Insert
    long insert(Assessment assessment);

    @Insert
    long[] insertAll(Assessment... assessments);

    @Insert
    long[] insertAllAssessments(List<Assessment> assessments);

    @Update
    int update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Delete
    void deleteAll(Assessment... assessments);

    @Query("select * from Assessment where sentFlag=0 and Labela!='assessment'")
    List<Assessment> getAllECEAssessment();

    @Query("select * from Assessment where sentFlag=0 and Labela='assessment'")
    List<Assessment> getAllScienceAssessment();

    @Query("DELETE FROM Assessment")
    void deleteAllAssessment();

    @Query("select * from Assessment WHERE StudentIDa=:stdID AND Labela=:COS_Lbl ORDER BY EndDateTimea DESC")
    List<Assessment> getCertificates(String stdID, String COS_Lbl);


    @Query("select count(*) from Assessment WHERE StudentIDa=:stdID")
    public int getAssessmentCount(String stdID);

    @Query("update Assessment set sentFlag=1 where sentFlag=0")
    public void setSentFlag();
}
