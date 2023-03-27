package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapp.R;
import com.example.tourapp.data.User;
import com.example.tourapp.httpInterface.UserInterface;
import com.example.tourapp.data.Result;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_remember;
    private Button btn_updatePwd;
    private Button btn_login;
    private TextView tv_register;
    private SharedPreferences login_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        cb_remember = findViewById(R.id.cb_remember);
        btn_updatePwd = findViewById(R.id.btn_updatePwd);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);

        btn_login.setOnClickListener(this);
        btn_updatePwd.setOnClickListener(this);
        tv_register.setOnClickListener(this);

        login_sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String lastUsername = login_sp.getString("username", "");
        String lastPassword = login_sp.getString("password", "");
        Boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);

        hideStable();
        if (choseRemember) {
            et_username.setText(lastUsername);
            et_password.setText(lastPassword);
            cb_remember.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_updatePwd:
                Intent intent_login_to_reset = new Intent(LoginActivity.this, UpdatePasswordActivity.class);
                startActivity(intent_login_to_reset);
                finish();
                break;
            case R.id.tv_register:
                Intent intent_login_to_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_login_to_register);
                finish();
                break;
        }
    }

    public void login() {
        if (isUsernameAndPwdValid()) {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();

            //后端查询数据返回结果，然后再做相应判断
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            UserInterface userInterface = retrofit.create(UserInterface.class);
            User user = new User();
            user.setAccount(username);
            user.setPassword(password);

            Gson gson = new Gson();
            String json = gson.toJson(user);

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Call<Result> resultCall = userInterface.login(requestBody);
            resultCall.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Result result = response.body();
                    int code = result.getCode();
                    Log.d("TAG",result.getMsg());
                    if (code == 200) {
                        SharedPreferences.Editor editor = login_sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        if (cb_remember.isChecked()) {
                            editor.putBoolean("mRememberCheck", true);
                        } else {
                            editor.putBoolean("mRememberCheck", false);
                        }
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("username",username);
                        Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        return;
                    }


                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    System.out.println("连接失败！");
                    System.out.println(t.getMessage());
                }
            });
        }
    }


    public boolean isUsernameAndPwdValid() {
        if (et_username.getText().toString().trim().isEmpty() || et_username.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_password.getText().toString().length() <= 6 || et_password.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //隐藏状态栏
    public void hideStable() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarDarkFont(true)
                .statusBarAlpha(0.0f)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }
}