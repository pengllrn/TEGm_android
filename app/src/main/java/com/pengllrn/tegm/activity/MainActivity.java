package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pengllrn.tegm.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("userid","2032");
        editor.putString("username","张");
    }

    public void into(View v){
        Intent intent= new Intent(MainActivity.this,ShowDevActivity.class);
        startActivity(intent);
    }

    public void into2(View v){
        Intent intent= new Intent(MainActivity.this,SearchDeviceActivity.class);
        startActivity(intent);
    }
    public void into3(View v){
        Intent intent= new Intent(MainActivity.this,DamageApllyActivity.class);
        startActivity(intent);
    }
}