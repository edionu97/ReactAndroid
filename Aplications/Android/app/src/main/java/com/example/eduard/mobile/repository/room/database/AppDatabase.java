package com.example.eduard.mobile.repository.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.eduard.mobile.repository.room.dao.ContactDao;
import com.example.eduard.mobile.repository.room.dao.FetchDao;
import com.example.eduard.mobile.repository.room.dao.UserDao;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.repository.room.entity.Fetch;
import com.example.eduard.mobile.repository.room.entity.User;

@Database(
        entities = {
                User.class,
                Fetch.class,
                Contact.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract ContactDao contactDao();

    public abstract FetchDao fetchDao();


    public static AppDatabase getInstance(final Context context) {

        if (instance == null) {

            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "final_database_android.db"
                            )
                            .build();
                }
            }
        }

        return instance;
    }

    private static volatile AppDatabase instance;
}
