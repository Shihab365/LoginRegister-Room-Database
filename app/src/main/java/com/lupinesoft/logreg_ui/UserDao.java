package com.lupinesoft.logreg_ui;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface UserDao {

    @Insert
    void insertUserInfo(UserInfo userInfo);

    @Query("SELECT * FROM UserDB WHERE email = (:email) and password = (:password)")
    UserInfo loginInfo(String email, String password);
}
