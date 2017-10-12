package com.pengllrn.tegm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pengllrn.tegm.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void into(View v){
        Intent intent= new Intent(MainActivity.this,ShowDevActivity.class);
        startActivity(intent);
    }

    public void into2(View v){
        Intent intent= new Intent(MainActivity.this,DevDetailActivity.class);
        startActivity(intent);
    }


}