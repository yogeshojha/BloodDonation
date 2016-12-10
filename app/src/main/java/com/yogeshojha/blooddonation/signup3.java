package com.yogeshojha.blooddonation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class signup3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);
    }
    public void mainpg(View v)
    {
        Intent iinent= new Intent(signup3.this,MainActivity.class);
        startActivity(iinent);
    }
}
