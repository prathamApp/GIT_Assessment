package com.pratham.assessment.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.pratham.assessment.domain.ContentTable;
import java.util.List;


/**
 * Created by Anki on 12/18/2018.
 */

@Dao
public interface ContentTableDao {

    @Insert
    long[] insertAll(List<ContentTable> contentTableList);

    @Insert
    long insert(ContentTable contentTableList);

    @Query("select * from ContentTable where parentId= :parentId")
    public List<ContentTable> getContentData(String parentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addContentList(List<ContentTable> contentList);

    @Query("SELECT * FROM ContentTable WHERE parentid ISNULL or parentid = 0 or parentid=''and contentLanguage=:language")
    public List<ContentTable> getParentsHeaders(String language);

    @Query("SELECT * FROM ContentTable WHERE parentid=:id and contentLanguage=:language")
    public List<ContentTable> getChildsOfParent(String id, String language);


}
