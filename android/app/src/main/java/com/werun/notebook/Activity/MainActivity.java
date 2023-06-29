package com.cym.notebook.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.cym.notebook.Adapter.FragmentAdapter;
import com.cym.notebook.Fragment.OneFragment;
import com.cym.notebook.Fragment.ThreeFragment;
import com.cym.notebook.Fragment.TwoFragment;
import com.cym.notebook.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    ViewPager viewPager;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int mFlag = intent.getIntExtra("flag", 0);
        if (mFlag == 3) { // 我的
            bnView.getMenu().getItem(2).setChecked(true);
            viewPager.setCurrentItem(2);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("token", 0);
        String token = sp.getString("token", null);
        if(token==null) {
            //进入登陆页面
            Intent intent = new Intent( MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            //进入首页
            setContentView(R.layout.activity_main);
            bnView = findViewById(R.id.bottom_nav_view);
            viewPager = findViewById(R.id.view_pager);
            List<Fragment> fragments = new ArrayList<>();
            fragments.add(new OneFragment());
            fragments.add(new TwoFragment());
            fragments.add(new ThreeFragment());
            FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
            viewPager.setAdapter(adapter);


            //BottomNavigationView 点击事件监听
            bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int menuId = menuItem.getItemId();
                    // 跳转指定页面：Fragment
                    switch (menuId) {
                        case R.id.tab_one:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.tab_two:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.tab_three:
                            viewPager.setCurrentItem(2);
                    }
                    return false;
                }
            });

            // ViewPager 滑动事件监听
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    //将滑动到的页面对应的 menu 设置为选中状态
                    bnView.getMenu().getItem(i).setChecked(true);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }


    }
}