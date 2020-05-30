package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.pratham.assessment.domain.SupervisorData;

import java.util.List;

@Dao
public interface SupervisorDataDao {

    @Insert
    long insert(SupervisorData supervisorData);

    @Insert
    void insertAll(List<SupervisorData> supervisorData);

    @Query("DELETE FROM SupervisorData")
    public void deleteAllSupervisorData();

    @Query("SELECT * FROM SupervisorData where sentFlag=0")
    public List<SupervisorData> getAllSupervisorData();

    @Query("SELECT * FROM SupervisorData where supervisorId = :supervisorId")
    public SupervisorData getSupervisorById(String supervisorId);

    @Query("SELECT * FROM SupervisorData where assessmentSessionId = :assessmentSessionId")
    public List<SupervisorData> getSupervisorBySession(String assessmentSessionId);

    @Query("update SupervisorData set sentFlag=1 where sentFlag=0")
    public int setSentFlag();
}