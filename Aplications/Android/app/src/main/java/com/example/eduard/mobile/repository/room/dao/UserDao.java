package com.example.eduard.mobile.repository.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.eduard.mobile.repository.room.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void add(User user);

    @Query("SELECT * FROM user_table WHERE username = :username")
    User findByUsername(String username);

    @Query("SELECT * FROM user_table WHERE username = :username AND password =:password")
    User findByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM user_table ORDER BY user_id ASC")
    LiveData<List<User>> getAll();
}
