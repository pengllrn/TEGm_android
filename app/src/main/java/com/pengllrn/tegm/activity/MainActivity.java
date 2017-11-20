package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.pengllrn.tegm.R;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ImageView menu_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("userid","2032");
        editor.putString("username","张");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        menu_home = (ImageView) findViewById(R.id.menu_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_look_device:
                        Intent intent= new Intent(MainActivity.this,SearchDeviceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_search_device:
                        Intent intent1= new Intent(MainActivity.this,ShowDevActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_damage_center:
                        Intent intent2= new Intent(MainActivity.this,DamageApllyActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_abnormal_warn:
                        break;
                    case R.id.nav_property_statics:
                        break;
                    case R.id.nav_private_center:
                        break;
                }
                return true;
            }
        });
        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}