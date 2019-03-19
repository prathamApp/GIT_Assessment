package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.pratham.assessment.domain.Crl;

import java.util.List;

@Dao
public interface CrlDao {

    @Insert
    long insert(Crl crl);

    @Insert
    long[] insertAll(Crl... crls);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Crl> crls);

    @Update
    int update(Crl crl);

    @Delete
    void delete(Crl crl);

    /*@Delete
    void deleteAll(Crl... crls);
*/
    @Query("DELETE FROM Crl")
    public void deleteAll();

    @Query("select * from Crl where CRLId = :crlID")
    Crl getCrl(String crlID);

    @Query("select * from Crl")
    List<Crl> getAllCrls();

    @Query("update Crl set newCrl = 0 where newCrl = 1")
    void setNewCrlToOld();

    @Query("select * from Crl where newCrl = 1")
    List<Crl> getAllNewCrls();

    @Query("select FirstName from Crl where UserName = :uName and Password = :uPass")
    String checkCrls(String uName, String uPass);

    @Query("SELECT * FROM CRL WHERE UserName=:user AND Password=:pass")
    public Crl checkUserValidation(String user, String pass);

    @Query("select CRLId from Crl where UserName = :uName and Password = :uPass")
    String getCrlId(String uName, String uPass);

}
