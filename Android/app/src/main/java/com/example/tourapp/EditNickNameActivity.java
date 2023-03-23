package com.example.tourapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

public class EditNickNameActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStable();
        setContentView(R.layout.activity_edit_nick_name);
        findViewById(R.id.iv_backward2).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        et_nickname = findViewById(R.id.et_nickname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backward2:
                finish();
                break;
            case R.id.tv_save:
                String nickname = et_nickname.getText().toString();
                if(nickname.trim().isEmpty() || nickname.contains(" ")) {
                    Toast.makeText(this, getString(R.string.is_nickname_valid), Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(EditNickNameActivity.this,MainActivity.class);
                    intent.putExtra("nickname",nickname);
                    setResult(RESULT_OK,intent);
                    finish();
                }
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
}