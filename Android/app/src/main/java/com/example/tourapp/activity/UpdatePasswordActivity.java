package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourapp.R;
import com.example.tourapp.data.User;
import com.example.tourapp.httpInterface.UserInterface;
import com.example.tourapp.data.Result;
import com.example.tourapp.interceptor.AddCookiesInterceptor;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_reset_old_password;
    private EditText et_reset_new_password;
    private EditText et_new_password_check;
    private Button reset_btn_cancel;
    private Button reset_btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        et_name = findViewById(R.id.et_name);
        et_reset_old_password = findViewById(R.id.et_reset_old_password);
        et_reset_new_password = findViewById(R.id.et_reset_new_password);
        et_new_password_check = findViewById(R.id.et_new_password_check);
        reset_btn_cancel = findViewById(R.id.reset_btn_cancel);
        reset_btn_sure = findViewById(R.id.reset_btn_sure);
        hideStable();

        reset_btn_cancel.setOnClickListener(this);
        reset_btn_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_btn_cancel:
                Intent intent_forgetPassword_to_login = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                startActivity(intent_forgetPassword_to_login);
                finish();
                break;
            case R.id.reset_btn_sure:
                resetPasswordCheck();
                break;
        }
    }

    private void resetPasswordCheck() {
        if (isUsernameAndPasswordValid()) {
            String username = et_name.getText().toString();
            String oldPassword = et_reset_old_password.getText().toString();
            String newPassword = et_reset_new_password.getText().toString();
            String newPasswordCheck = et_new_password_check.getText().toString();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AddCookiesInterceptor())
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .baseUrl("http://47.107.38.208:8090/user/")
                    .build();

            UserInterface userInterface = retrofit.create(UserInterface.class);
            User user = new User();
            user.setAccount(username);
            user.setPassword(oldPassword);

            Gson gson = new Gson();
            String json = gson.toJson(user);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Call<Result> resultCall = userInterface.login(requestBody);
            resultCall.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Result result = response.body();
                    if (result != null) {
                        int code = result.getCode();
                        Log.d("TAG", result.getMsg());
                        if (code == 200) {
                            if (!newPassword.equals(newPasswordCheck)) {
                                Toast.makeText(UpdatePasswordActivity.this, getString(R.string.mismatching_password), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                User user_ = new User();
                                user_.setAccount(username);
                                user_.setPassword(newPassword);
                                String json_ = gson.toJson(user_);
                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json_);
                                Call<Result> updatePwdCall = userInterface.updatePwd(body);
                                updatePwdCall.enqueue(new Callback<Result>() {
                                    @Override
                                    public void onResponse(Call<Result> call, Response<Result> response) {
                                        Result updateResult = response.body();
                                        int updateResultCode = updateResult.getCode();
                                        Log.d("TAG", updateResult.getMsg());
                                        if (updateResultCode == 200) {
                                            Toast.makeText(UpdatePasswordActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                                            Intent intent_forgetPassword_to_login = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                                            intent_forgetPassword_to_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent_forgetPassword_to_login);
                                            finish();
                                        } else {
                                            Toast.makeText(UpdatePasswordActivity.this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Result> call, Throwable t) {
                                        System.out.println("请求失败！");
                                        System.out.println(t.getMessage());
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(UpdatePasswordActivity.this, getString(R.string.mismatching), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    System.out.println("请求失败！");
                    System.out.println(t.getMessage());
                }
            });
        }

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

    public boolean isUsernameAndPasswordValid() {
        if (et_name.getText().toString().trim().isEmpty() || et_name.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_reset_old_password.getText().toString().length() <= 6 || et_reset_old_password.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_reset_new_password.getText().toString().length() <= 6 || et_reset_new_password.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_new_password_check.getText().toString().length() <= 6 || et_new_password_check.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}