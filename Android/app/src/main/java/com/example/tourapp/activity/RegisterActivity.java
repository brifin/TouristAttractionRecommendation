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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_reset_username;
    private EditText et_old_password;
    private EditText et_new_password;
    private Button btn_sure;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_reset_username = findViewById(R.id.et_reset_username);
        et_old_password = findViewById(R.id.et_old_password);
        et_new_password = findViewById(R.id.et_new_password);
        btn_sure = findViewById(R.id.btn_sure);
        btn_cancel = findViewById(R.id.btn_cancel);
        hideStable();
        btn_cancel.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                Intent intent_Register_to_login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent_Register_to_login);
                finish();
                break;
            case R.id.btn_sure:
                register();
                break;
        }
    }

    public void register() {
        if (isUsernameAndPasswordValid()) {
            String old_password = et_old_password.getText().toString();
            String new_password = et_new_password.getText().toString();
            String reset_username = et_reset_username.getText().toString();

            if(!old_password.equals(new_password)) {
                Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                return;
            }else {
                //TODO
                //传入后端，进行注册，并返回登录界面
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://47.107.38.208:8090/user/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UserInterface userInterface = retrofit.create(UserInterface.class);
                User user = new User();
                user.setAccount(reset_username);
                user.setPassword(new_password);
                user.setNickname(reset_username);

                Gson gson = new Gson();
                String json = gson.toJson(user);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                Call<Result> resultCall = userInterface.register(requestBody);
                resultCall.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        if(result != null) {
                            int code = result.getCode();
                            Log.d("TAG",result.getMsg());
                            if(code == 200) {
                                Toast.makeText(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, getString(R.string.reset_username_failed), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        System.out.println("请求失败！");
                        Log.e("YANG",t.getMessage());
                    }
                });


            }

        }
    }

    public boolean isUsernameAndPasswordValid() {
        if (et_reset_username.getText().toString().trim().isEmpty() || et_reset_username.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_username), Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_old_password.getText().toString().length() <= 6 || et_old_password.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_new_password.getText().toString().length() <= 6 || et_new_password.getText().toString().contains(" ")) {
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
