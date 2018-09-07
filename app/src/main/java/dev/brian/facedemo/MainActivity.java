package dev.brian.facedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import dev.brian.facedemo.fragment.FaceFragment;
import dev.brian.facedemo.fragment.GestureFragment;
import dev.brian.facedemo.fragment.OtherFragment;
import dev.brian.facedemo.fragment.SearchFragment;
import dev.brian.facedemo.util.Constant;

public class MainActivity extends AppCompatActivity {
    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        initData();
        initView();
    }

    // 初始化数据
    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add("人脸识别");
        mTitle.add("手势识别");
        mTitle.add("其他功能");

        mFragment = new ArrayList<>();
        mFragment.add(new SearchFragment());
        mFragment.add(new GestureFragment());
        mFragment.add(new OtherFragment());
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        // 预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());

        // mViewPager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        // 绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
