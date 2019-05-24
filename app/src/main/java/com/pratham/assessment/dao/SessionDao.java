package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;


import com.pratham.assessment.domain.Session;

import java.util.List;

@Dao
public interface SessionDao {

    @Insert
    long insert(Session session);

    @Insert
    long[] insertAll(Session... sessions);

    @Update
    int update(Session session);

    @Delete
    void delete(Session session);

    @Delete
    void deleteAll(Session... sessions);

    @Query("select * from Session")
    List<Session> getAllSessions();

    @Query("select * from Session where sentFlag=0")
    List<Session> getAllNewSessions();

    @Query("UPDATE Session SET toDate = :toDate where SessionID = :SessionID")
    void UpdateToDate(String SessionID, String toDate);

    @Query("UPDATE Session SET sentFlag = :sentFlagNew where sentFlag=:sentFlagOld")
    void UpdateSentFlag(String sentFlagOld, String sentFlagNew);

    @Query("select toDate from Session where SessionID = :SessionID")
    String getToDate(String SessionID);

    @Query("update Session set sentFlag=1 where sentFlag=0")
    public void setSentFlag();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addSessionList(List<Session> contentList);

    /*    String query = "select FirstName,time from Student x INNER JOIN(select StudentID,sum(seconds) as time from(select b.StudentID, (strftime('%s',(substr(toDate, 7, 4)||'-'||substr(toDate, 4,2)||'-'||substr(toDate, 1,2)||' '||substr(toDate,11) )) - strftime('%s',(substr(fromDate, 7, 4)||'-'||substr(fromDate, 4,2)||'-'||substr(fromDate, 1,2)||' '||substr(fromDate,11))))" +
                " as seconds from Session radio_button_bg left outer join Attendance b on radio_button_bg.SessionID = b.SessionID) group by StudentID) y on x.StudentID = y.StudentID order by time desc";*/
    String query = "select Firstname, result from Student x inner join (select distinct studentid , sum (seconds) as result from (select distinct a.studentid, a.Firstname, (strftime('%s',(substr(c.toDate, 7, 4)||'-'||substr(c.toDate, 4,2)||'-'||substr(c.toDate, 1,2)||' '||substr(c.toDate,11) )) - strftime('%s',(substr(c.fromDate, 7, 4)||'-'||substr(c.fromDate, 4,2)||'-'||substr(c.fromDate, 1,2)||' '||substr(c.fromDate,11)))) as seconds from Session c, Student a Inner join Attendance b on a.studentid = b.studentid and b.sessionid = c.sessionid) group by Studentid order by result desc) z on x.studentid = z.studentid";

    @Query(query)
    Cursor getStudentUsageData();
}
