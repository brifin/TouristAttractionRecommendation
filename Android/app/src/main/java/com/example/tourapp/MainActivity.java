package com.example.tourapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tourapp.adapter.ViewPagerAdapter;
import com.example.tourapp.fragment.MapFragment;
import com.example.tourapp.fragment.TourFragment;
import com.example.tourapp.fragment.UserFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private LinearLayout pop_upsview;


    private List<Fragment> fragmentList;
    private TabLayoutMediator mediator;
    private String[] tabText;
    int activeColor = R.color.purple_200;
    int normalColor = R.color.black;

    int activeSize = 25;
    int normalSize = 16;

    private final String[] permissionArray = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    private ActivityResultLauncher<String[]> requestPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                boolean internet = Boolean.TRUE.equals(result.get(Manifest.permission.INTERNET));
                //boolean writeStorage = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                //boolean readStorage = Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE));
                boolean accessNetWork = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_NETWORK_STATE));
                boolean readContacts = Boolean.TRUE.equals(result.get(Manifest.permission.READ_CONTACTS));
                boolean readPhoneState = Boolean.TRUE.equals(result.get(Manifest.permission.READ_PHONE_STATE));
                boolean readAudio = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_AUDIO));
                boolean readImages = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_IMAGES));
                boolean readVideo = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_VIDEO));
                boolean writeStorage = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));

            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVersion();

        tabText = new String[]{"地图", "旅游团", "用户"};

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new MapFragment());
        fragmentList.add(new TourFragment());
        fragmentList.add(new UserFragment());

        viewPager2 = (ViewPager2) findViewById(R.id.viewpager2);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        // viewPager2.registerOnPageChangeCallback(changeCallback);

        mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(tabText[0]);

                        break;
                    case 1:
                        tab.setText(tabText[1]);

                        break;
                    case 2:
                        tab.setText(tabText[2]);

                        break;
                }
            }
        });
        mediator.attach();
        viewPager2.setUserInputEnabled(false);

    }

    private void checkVersion() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            for (int i = 0; i < permissionArray.length; i++) {
                if (checkSelfPermission(permissionArray[i]) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions.launch(permissionArray);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
                super.onBackPressed();
        } else {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }

    }

//    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
//        @Override
//        public void onPageSelected(int position) {
//            int tabCount = tabLayout.getTabCount();
//
//            for (int i = 0; i < tabCount; i++) {
//                TabLayout.Tab tab = tabLayout.getTabAt(i);
//                TextView textView = (TextView) tab.getCustomView();
//
//                if (tab.getPosition() == position) {
//                    textView.setTextSize(activeSize);
//                    textView.setTypeface(Typeface.DEFAULT_BOLD);
//                } else {
//                    textView.setTextSize(noemalSize);
//                    textView.setTypeface(Typeface.DEFAULT);
//                }
//            }
//        }
//    };

    @Override
    protected void onDestroy() {
        mediator.detach();
        // viewPager2.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }

}