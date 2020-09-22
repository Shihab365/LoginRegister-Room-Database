package com.lupinesoft.logreg_ui;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = UserInfo.class, version = 1)
public abstract class UserRoomDB extends RoomDatabase {

    public abstract UserDao userDao();
    public static volatile UserRoomDB userRoomDBInstance;
    static UserRoomDB getDatabase(final Context context){
        if(userRoomDBInstance==null){
            synchronized (UserRoomDB.class){
                if(userRoomDBInstance==null){
                    userRoomDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                            UserRoomDB.class, "UserDB").build();
                }
            }
        }
        return userRoomDBInstance;
    }

}
