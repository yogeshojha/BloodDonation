package com.yogeshojha.blooddonation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void logn(View v)
    {
        Intent iinent= new Intent(login.this,MainActivity.class);
        startActivity(iinent);
    }
}
