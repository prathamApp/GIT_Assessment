package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.pratham.assessment.domain.Student;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Student student);

    /*@Insert
    long[] insertAll(Student... students);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Student> studentList);

    @Update
    int update(Student student);

    @Delete
    void delete(Student student);

    /*  @Delete
      void deleteAll(Student... students);
  */
    @Query("DELETE FROM Student")
    public void deleteAll();

    @Query("select * from Student where StudentID = :studentID")
    Student getStudent(String studentID);

    @Query("select * from Student where StudentID = :studentID")
    Student addStudent(String studentID);

    @Query("select * from Student")
    List<Student> getAllStudents();

    @Query("SELECT * FROM Student WHERE GroupId=:gID")
    public List<Student> getGroupwiseStudents(String gID);

    /*@Query("select * from Student where newFlag = 1")
    List<Student> getAllNewStudents();*/

    @Query("select * from Student where newFlag = 0")
    List<Student> getAllNewStudents();


    @Query("update Student set newFlag=0 where newFlag = 1")
    void setNewStudentsToOld();

    @Query("update Student set newFlag=0 where StudentID = :studentID")
    void setFlagFalse(String studentID);

    @Query("select FirstName from Student where StudentID = :studentID")
    String getStudentFirstName(String studentID);

    @Query("select FullName from Student where StudentID = :studentID")
    String getFullName(String studentID);

    @Query("select FirstName from Student where StudentID = :studentID")
    String checkStudent(String studentID);

    @Query("select avatarName from Student where StudentID = :studentID")
    String getStudentAvatar(String studentID);

    @Query("DELETE FROM Student WHERE GroupID=:grpID")
    public void deleteDeletedGrpsStdRecords(String grpID);

    @Query("DELETE FROM Student WHERE Gender='Deleted'")
    public void deleteDeletedStdRecords();

    @Query("update Student set newFlag=1 where newFlag=0")
    public void setSentFlag();
}
