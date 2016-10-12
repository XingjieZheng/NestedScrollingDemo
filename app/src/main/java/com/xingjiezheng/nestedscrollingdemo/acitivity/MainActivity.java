package com.xingjiezheng.nestedscrollingdemo.acitivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.xingjiezheng.nestedscrollingdemo.fragment.LeftFragment;
import com.xingjiezheng.nestedscrollingdemo.fragment.RightFragment;
import com.xingjiezheng.nestedscrollingdemo.R;
import com.xingjiezheng.nestedscrollingdemo.widget.HeaderTabToolBarView;
import com.xingjiezheng.nestedscrollingdemo.widget.HeaderTabView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HeaderTabView headerTabView;
    private HeaderTabToolBarView headerTabToolBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        headerTabView = (HeaderTabView) findViewById(R.id.headerTabView);
        headerTabToolBarView = (HeaderTabToolBarView) findViewById(R.id.headerTabToolBarView);
        headerTabView.setToolBarViewToHeaderView(headerTabToolBarView);
        setupViewPager(viewPager);

        headerTabView.setNickname("Strider");
        headerTabView.setTitleLeftNumber("10");
        headerTabView.setTitleRightNumber("99");
    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new LeftFragment(), "Call");
        adapter.addFragment(new RightFragment(), "Message");
        viewPager.setAdapter(adapter);
        headerTabView.setupWithViewPager(viewPager);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
