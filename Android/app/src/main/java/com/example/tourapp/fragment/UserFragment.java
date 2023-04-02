package com.example.tourapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

import com.example.tourapp.activity.MyBrowseActivity;
import com.example.tourapp.activity.MyLoveActivity2;
import com.example.tourapp.R;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.data.GetImageResult;
import com.example.tourapp.httpInterface.UserInterface;
import com.example.tourapp.data.Result;
import com.example.tourapp.interceptor.AddCookiesInterceptor;
import com.example.tourapp.viewAndItem.ItemGroup;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserFragment extends Fragment implements View.OnClickListener {

    private TextView tv_username;
    private ItemGroup ig_arrive;
    private ItemGroup ig_like;
    private ImageView iv_portrait;
    private PopupWindow popupWindow;
    private ImageView iv_backward;
    private Retrofit retrofit;
    private String username;

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

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://47.107.38.208:8090/user/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tv_username = view.findViewById(R.id.tv_username);
        ig_arrive = view.findViewById(R.id.ig_arrive);
        ig_like = view.findViewById(R.id.ig_like);
        iv_portrait = view.findViewById(R.id.iv_portrait);
        iv_backward = view.findViewById(R.id.iv_backward);

        ig_arrive.setOnClickListener(this);
        ig_like.setOnClickListener(this);
        iv_portrait.setOnClickListener(this);
        iv_backward.setOnClickListener(this);
        view.findViewById(R.id.iv_backward).setOnClickListener(this);

        //设置用户名
        Intent intent = getActivity().getIntent();
        username = intent.getStringExtra("username");
        tv_username.setText(username);

        //设置头像
        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<GetImageResult> imageResultCall = userInterface.getImage();
        imageResultCall.enqueue(new Callback<GetImageResult>() {
            @Override
            public void onResponse(Call<GetImageResult> call, Response<GetImageResult> response) {
                GetImageResult getImageResult = response.body();
                if(getImageResult != null) {
                    Integer code = getImageResult.getCode();
                    Log.d("YANG",getImageResult.getMsg());
                    if(code == 200) {
                        String data = getImageResult.getData();
                        Bitmap bitmap = null;
                        try {
                            byte[] bitmapArray = Base64.decode(data.split(",")[1], Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                            iv_portrait.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else {
                        Toast.makeText(MyApplication.getContext(), "头像获取失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<GetImageResult> call, Throwable t) {
                System.out.println("请求失败！");
                Log.e("YANG",t.getMessage());
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ig_arrive:
                Intent intent_browse = new Intent(getContext(), MyBrowseActivity.class);
                intent_browse.putExtra("nickname",username);
                startActivity(intent_browse);
                break;
            case R.id.ig_like:
                Intent intent_love = new Intent(getContext(), MyLoveActivity2.class);
                intent_love.putExtra("nickname",username);
                startActivity(intent_love);
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
                }, 10);
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
            startActivityForResult(intent, 1);
            //去除选择框
            popupWindow.dismiss();
        });
        //取消按钮监听
        cancel.setOnClickListener(view -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 2:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = bundle.getParcelable("data");
                        if (bitmap != null) {
                            iv_portrait.setImageBitmap(bitmap);
                            File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                uploadImage(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getActivity(), getString(R.string.set_invalid), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.set_invalid), Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                Uri uri = data.getData();
                iv_portrait.setImageURI(uri);

                String img_path;
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualImageCursor = getActivity().managedQuery(uri, proj, null,
                        null, null);
                if (actualImageCursor == null) {
                    img_path = uri.getPath();
                } else {
                    int actual_image_column_index = actualImageCursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    actualImageCursor.moveToFirst();
                    img_path = actualImageCursor
                            .getString(actual_image_column_index);
                }
                File file = new File(img_path);
                uploadImage(file);
                break;
        }
    }

    private void uploadImage(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("applyFiles", file.getName(), requestBody);

        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<Result> resultCall = userInterface.uploadFile(part);
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                int code = result.getCode();
                Log.d("YANG",result.getMsg());
                if(code == 200) {
                    Log.d("YANG","文件上传成功");
                }else {
                    Log.d("YANG","文件上传失败");
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