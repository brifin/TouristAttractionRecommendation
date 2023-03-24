package com.example.tourapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapp.R;
import com.example.tourapp.viewAndItem.ItemGroup;


public class UserFragment extends Fragment implements View.OnClickListener {

    private TextView tv_username;
    private ItemGroup ig_arrive;
    private ItemGroup ig_like;
    private ImageView iv_portrait;
    private PopupWindow popupWindow;
    private ImageView iv_backward;

    public UserFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        tv_username = view.findViewById(R.id.tv_username);
        ig_arrive = view.findViewById(R.id.ig_arrive);
        ig_like = view.findViewById(R.id.ig_like);
        iv_portrait = view.findViewById(R.id.iv_portrait);
        iv_backward = view.findViewById(R.id.iv_backward);
        /**通过后端查询返回设置
         //TODO

         */
        ig_arrive.setOnClickListener(this);
        ig_like.setOnClickListener(this);
        iv_portrait.setOnClickListener(this);
        iv_backward.setOnClickListener(this);
        view.findViewById(R.id.iv_backward).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ig_arrive:
                break;
            case R.id.ig_like:
                break;
            case R.id.iv_backward:
                Toast.makeText(getActivity(), "退出应用", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            case R.id.iv_portrait:
                showPhotoSelect();
                break;
        }
    }

    private void showPhotoSelect() {
        //展示修改头像的选择框，并设置选择框的监听器
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupWindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //设置监听器
        TextView take_photo = layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = layout_photo_selected.findViewById(R.id.cancel);
        //拍照按钮监听
        take_photo.setOnClickListener(view -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                //申请权限
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },10);
                //去除选择框
                popupWindow.dismiss();
            }
        });
        //相册按钮监听
        from_albums.setOnClickListener(view -> {
            //申请权限
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,3);
            //去除选择框
            popupWindow.dismiss();
        });
        //取消按钮监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 2:
                if(data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = bundle.getParcelable("data");
                    iv_portrait.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(getActivity(), getString(R.string.set_invalid), Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                Uri uri = data.getData();
                iv_portrait.setImageURI(uri);
                break;
        }
    }
}