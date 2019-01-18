package com.example.eduard.mobile.repository.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        tableName = "user_table",
        indices = {
                @Index(
                        value = {"username"},
                        unique = true
                )
        }
)
public class User {

    public User() {

    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUid() {
        return uid;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int uid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;
}
