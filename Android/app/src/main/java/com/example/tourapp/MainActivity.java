package com.example.tourapp;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private List<Fragment> fragmentList;
    private TabLayoutMediator mediator;
    private String[] tabtext;
    int activeColor = R.color.purple_200;
    int normalColor = R.color.black;

    int activeSize = 25;
    int noemalSize = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabtext = new String[]{"地图", "旅游团", "用户"};

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new MapFragment());
        fragmentList.add(new TourFragment());
        fragmentList.add(new UserFragment());

        viewPager2 = (ViewPager2) findViewById(R.id.viewpager2);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
        viewPager2.setAdapter(viewPagerAdapter);

       // viewPager2.registerOnPageChangeCallback(changeCallback);

        mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(tabtext[0]);
                        break;
                    case 1:
                        tab.setText(tabtext[1]);
                        break;
                    case 2:
                        tab.setText(tabtext[2]);
                        break;
                }
            }
        });
        mediator.attach();
        ;
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