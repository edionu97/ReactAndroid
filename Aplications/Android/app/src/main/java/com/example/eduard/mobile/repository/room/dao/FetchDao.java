package com.example.eduard.mobile.repository.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.eduard.mobile.repository.room.entity.Fetch;

import java.util.List;

@Dao
public interface FetchDao {

    @Insert
    void insert(Fetch fetch);

    @Update
    void update(Fetch fetch);

    @Delete
    void delete(Fetch fetch);

    @Query("DELETE FROM fetch")
    void deleteAll();

    @Query("SELECT * FROM fetch")
    List<Fetch> getAll();

    @Query("SELECT * FROM fetch WHERE username = :username AND fetched = 0")
    List<Fetch> getUnfetched(String username);

    @Query("SELECT * FROM fetch WHERE username = :username AND data = :data AND table_name  = :tableName AND type = :type")
    Fetch getByAllFields(String username, String data, String tableName, int type);

}
