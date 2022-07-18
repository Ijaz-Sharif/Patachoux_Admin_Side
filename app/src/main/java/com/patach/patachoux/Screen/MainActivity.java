package com.patach.patachoux.Screen;

import static com.patach.patachoux.Utils.Constant.setUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.Adapter.ViewPagerAdapter;
import com.patach.patachoux.Fragments.BreadFragment;
import com.patach.patachoux.Fragments.PasteryFragment;
import com.patach.patachoux.Model.Product;
import com.patach.patachoux.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
                return true;
                case R.id.setting_account:
                startActivity(new Intent(MainActivity.this, UpdateAdminPasswordActivity.class));
                return true;
                case R.id.view_order:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}