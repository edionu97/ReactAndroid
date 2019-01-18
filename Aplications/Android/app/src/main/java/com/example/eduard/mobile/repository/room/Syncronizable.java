package com.example.eduard.mobile.repository.room;

public interface Syncronizable {

    void syncContactAdd(String firstName, String lastName, String phoneNumber, Runnable onSuccess);

    void syncContactUpdate(String firstName, String lastName, String phoneNumber, Runnable onSuccess);

    void syncContactDelete(String firstName, Runnable onSuccess);
}
