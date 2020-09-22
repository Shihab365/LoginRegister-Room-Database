package com.lupinesoft.logreg_ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserDao userDao;
    private UserRoomDB userRoomDB;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRoomDB = UserRoomDB.getDatabase(application);
        userDao = userRoomDB.userDao();
    }

    public void insertUserInfo(UserInfo userInfo) {
        new InsertAsyncTask(userDao).execute(userInfo);
    }

    private class InsertAsyncTask extends AsyncTask<UserInfo, Void, Void> {
        UserDao mUserDAO;

        public InsertAsyncTask(UserDao mUserDAO) {
            this.mUserDAO = mUserDAO;
        }

        @Override
        protected Void doInBackground(UserInfo... userInfos) {
            mUserDAO.insertUserInfo(userInfos[0]);
            return null;
        }
    }
}
