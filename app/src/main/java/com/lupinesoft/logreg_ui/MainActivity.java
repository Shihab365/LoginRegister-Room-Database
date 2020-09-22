package com.lupinesoft.logreg_ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CardView cardRegButon;
    ImageView submitButton;
    EditText etEmail, etPassword;
    UserViewModel userViewModel;
    private SharedPrefConfig prefConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        cardRegButon = findViewById(R.id.login_goRegButton_ID);
        submitButton = findViewById(R.id.login_submitButton_ID);
        etEmail = findViewById(R.id.login_et_email_ID);
        etPassword = findViewById(R.id.login_et_password_ID);

        prefConfig = new SharedPrefConfig(getApplicationContext());
        if(prefConfig.readLoginStatus()){
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }

        cardRegButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegActivity.class));
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strEmail = etEmail.getText().toString();
                final String strPassword = etPassword.getText().toString();

                if(strEmail.isEmpty() || strPassword.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fill all the box.", Toast.LENGTH_SHORT).show();
                }else{
                    UserRoomDB userRoomDB = UserRoomDB.getDatabase(getApplicationContext());
                    final UserDao userDao = userRoomDB.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserInfo userInfo = userDao.loginInfo(strEmail, strPassword);
                            if(userInfo == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Invalid Info!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                String strName = userInfo.getName();
                                String strAddress = userInfo.getAddress();
                                String strEmail = userInfo.getEmail();

                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                SharedPreferences sharedPreferences=getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("sp_name", strName);
                                editor.putString("sp_address", strAddress);
                                editor.putString("sp_email", strEmail);
                                editor.commit();
                                prefConfig.writeLoginStatus(true);
                                startActivity(intent);
                            }
                        }
                    }).start();
                }
            }
        });
    }
}
