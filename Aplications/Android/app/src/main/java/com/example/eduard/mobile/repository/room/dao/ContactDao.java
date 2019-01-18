package com.example.eduard.mobile.repository.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.eduard.mobile.repository.room.entity.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void add(Contact contact);

    @Delete
    void delete(Contact contact);

    @Update
    void update(Contact contact);

    @Query("SELECT * FROM user_table ut " +
            "INNER JOIN contact c ON c.user_id = ut.user_id " +
            "WHERE c.first_name = :firstName AND ut.username = :username")
    Contact findContactByFirstNameAndUser(String firstName, String username);

    @Query("SELECT  * FROM contact WHERE contact_id = :contactId")
    Contact findContactById(int contactId);

    @Query("SELECT * FROM contact")
    LiveData<List<Contact>> getAll();

    @Query("SELECT * FROM contact c " +
            "INNER JOIN user_table ut on c.user_id = ut.user_id " +
            "WHERE ut.username = :username")
    LiveData<List<Contact>> getContactsForUser(String username);
}
