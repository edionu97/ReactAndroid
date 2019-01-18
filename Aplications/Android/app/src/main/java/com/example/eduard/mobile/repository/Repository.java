package com.example.eduard.mobile.repository;

import com.example.eduard.mobile.repository.room.Syncronizable;
import com.example.eduard.mobile.repository.room.entity.Fetch;

import java.util.List;

public interface Repository  extends Syncronizable {

    void contactAdd(String firstName, String lastName, String phoneNumber);

    void contactUpdate(String firstName, String secondName, String phoneNumber);

    void contactDelete(String firstName);

    void getAllContactsForUser();

    void getChartData();

    void addUnfetched(String tableName, String data, int type);

    void fetch(String tableName, String data, int type);

    List<Fetch> getUnfetchedData();
}
