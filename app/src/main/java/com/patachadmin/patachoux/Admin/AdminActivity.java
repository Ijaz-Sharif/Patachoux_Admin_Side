package com.patachadmin.patachoux.Admin;

import static com.patachadmin.patachoux.Utils.Constant.setUserLoginStatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.patachadmin.patachoux.Adapter.ViewPagerAdapter;
import com.patachadmin.patachoux.Fragments.BreadFragment;
import com.patachadmin.patachoux.Fragments.PasteryFragment;
import com.patachadmin.patachoux.Order.SplierMainActivity;
import com.patachadmin.patachoux.R;
import com.patachadmin.patachoux.Screen.LoginActivity;
import com.patachadmin.patachoux.Screen.SuplierActivity;
import com.patachadmin.patachoux.Screen.UpdateAdminPasswordActivity;
import com.patachadmin.patachoux.Screen.UserActivity;

public class AdminActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new BreadFragment(),"Bread");
        viewPagerAdapter.addFragment(new PasteryFragment(),"Pastry");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logoutUser:
                setUserLoginStatus(AdminActivity.this,false);
                startActivity(new Intent(AdminActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case R.id._suplier:
                startActivity(new Intent(AdminActivity.this, SuplierActivity.class));
            case R.id.add_sub_Admin:
                startActivity(new Intent(AdminActivity.this, AdminMainActivity.class));
            case R.id._user:
                startActivity(new Intent(AdminActivity.this, UserActivity.class));
                return true;
            case R.id.setting_account:
                startActivity(new Intent(AdminActivity.this, UpdateAdminPasswordActivity.class));
                return true;
            case R.id.view_order:
                startActivity(new Intent(AdminActivity.this, SplierMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}