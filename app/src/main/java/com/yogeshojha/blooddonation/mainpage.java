package com.yogeshojha.blooddonation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class mainpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
    }
    public void signupst(View v)
    {
        Intent iinent= new Intent(mainpage.this,signup.class);
        startActivity(iinent);
    }
    public void loginst(View v)
    {
        Intent iinent= new Intent(mainpage.this,login.class);
        startActivity(iinent);
    }
}
