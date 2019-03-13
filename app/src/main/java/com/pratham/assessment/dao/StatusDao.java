package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.pratham.assessment.domain.Status;

import java.util.List;

@Dao
public interface StatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Status status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Status... statuses);

    @Update
    int update(Status status);

    @Delete
    void delete(Status status);

    @Delete
    void deleteAll(Status... statuses);

    @Query("select * from Status")
    List<Status> getAllStatuses();

    @Query("Select statusKey from Status where statusKey = :key")
    String getKey(String key);

    @Query("Select value from Status where statusKey = :key")
    String getValue(String key);

    @Query("UPDATE Status set value =:value where statusKey =:key")
    void updateValue(String key, String value);

}