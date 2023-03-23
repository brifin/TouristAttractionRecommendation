package com.example.tourapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.text.Format;

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

            if(findUserByUsername(reset_username)) {
                String str = reset_username + "已存在";
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                return;
            }

            if(!et_old_password.equals(new_password)) {
                Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                return;
            }else {
                //TODO
                //传入后端，进行注册，并返回登录界面
            }

        }
    }

    //接受后端数据并判断用户是否存在
    private boolean findUserByUsername(String username) {
        //TODO
        return true;
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
