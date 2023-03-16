package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
            SharedPreferences.Editor editor = login_sp.edit();
            if (isFindByUsernameAndPwd(username, password)) {
                editor.putString("username", username);
                editor.putString("password", password);
                if (cb_remember.isChecked()) {
                    editor.putBoolean("mRememberCheck", true);
                }else {
                    editor.putBoolean("mRememberCheck",false);
                }
                editor.commit();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isFindByUsernameAndPwd(String username, String password) {
        //TODO
        //后端查询数据返回结果，然后再做相应判断
        return true;
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
}