package com.patachadmin.patachoux.Screen;

import static com.patachadmin.patachoux.Utils.Constant.setUserLoginStatus;

import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

         ViewPager viewPager;
         TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logoutUser:
                setUserLoginStatus(MainActivity.this,false);
                startActivity(new Intent(MainActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case R.id._suplier:
                startActivity(new Intent(MainActivity.this, SuplierActivity.class));

            case R.id._user:
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                return true;
                case R.id.setting_account:
                startActivity(new Intent(MainActivity.this, UpdateAdminPasswordActivity.class));
                return true;
                case R.id.view_order:
                    startActivity(new Intent(MainActivity.this, SplierMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}