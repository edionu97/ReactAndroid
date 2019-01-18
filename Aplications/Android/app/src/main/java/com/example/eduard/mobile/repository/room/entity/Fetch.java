package com.example.eduard.mobile.repository.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        tableName = "fetch",
        indices = {
                @Index(
                        value = {
                                "username", "data", "table_name", "type", "fetched"
                        },
                        unique = true
                )
        }
)
public class Fetch {

    public Fetch() {

    }

    public Fetch(String username, String data, String tableName, int type) {
        this.username = username;
        this.data = data;
        this.tableName = tableName;
        this.type = type;
        this.fetched = false;
    }

    public Fetch(String data, String tableName, int type) {
        this.data = data;
        this.tableName = tableName;
        this.type = type;
        this.fetched = false;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fetch_id")
    private int fid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "data")
    private String data;

    @ColumnInfo(name = "table_name")
    private String tableName;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "fetched")
    private boolean fetched;
}
