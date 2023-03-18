package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        reset_btn_cancel.setOnClickListener(this);
        reset_btn_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_btn_cancel:
                Intent intent_forgetPassword_to_login = new Intent(UpdatePasswordActivity.this,LoginActivity.class);
                startActivity(intent_forgetPassword_to_login);
                finish();
                break;
            case R.id.reset_btn_sure:
                resetPasswordCheck();
                break;
        }
    }

    private void resetPasswordCheck() {
        if(isUsernameAndPasswordValid()) {
            String username = et_name.getText().toString();
            String oldPassword = et_reset_old_password.getText().toString();
            String newPassword = et_reset_new_password.getText().toString();
            String newPasswordCheck = et_new_password_check.getText().toString();

            if(findByUsernameAndPassword(username,oldPassword)) {
                if(!newPassword.equals(newPasswordCheck)) {
                    Toast.makeText(this, getString(R.string.mismatching_password), Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(!updatePassword(username,newPassword)) {
                        Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                        Intent intent_forgetPassword_to_login = new Intent(UpdatePasswordActivity.this,LoginActivity.class);
                        startActivity(intent_forgetPassword_to_login);
                        finish();
                    }
                }
            }else {
                Toast.makeText(this,getString(R.string.mismatching) , Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    //接受后端数据并判断用户名与密码是否匹配
    public boolean findByUsernameAndPassword(String username,String password) {
        //TODO
        return true;
    }

    //传入后端更新数据，并返回是否更新成功
    public boolean updatePassword(String username,String password) {
        //TODO
        return true;
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
        }else if(et_new_password_check.getText().toString().length() <= 6 || et_new_password_check.getText().toString().contains(" ")) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}