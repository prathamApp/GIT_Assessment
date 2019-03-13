package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.pratham.assessment.domain.Attendance;

import java.util.List;

@Dao
public interface AttendanceDao {

    @Insert
    long insert(Attendance attendance);

    @Insert
    long[] insertAll(Attendance... attendances);

    @Insert
    long[] insertAll(List<Attendance> attendances);

    @Update
    int update(Attendance attendance);

    @Delete
    void delete(Attendance attendance);

    @Delete
    void deleteAll(Attendance... attendances);

    @Query("select * from Attendance")
    List<Attendance> getAllAttendanceEntries();

    @Query("select * from Attendance where sentFlag=0")
        List<Attendance> getAllPushAttendanceEntries();

    @Query("select StudentID from Attendance where SessionID = :sessionId")
    String getStudentBySession(String sessionId);

    @Query("SELECT * FROM Attendance WHERE sentFlag=0 AND SessionID=:s_id")
    public List<Attendance> getNewAttendances(String s_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addAttendanceList(List<Attendance> contentList);

    @Query("update Attendance set sentFlag=1 where sentFlag=0")
    public void setSentFlag();
}
