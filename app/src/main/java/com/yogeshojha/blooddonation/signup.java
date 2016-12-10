package com.yogeshojha.blooddonation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    public void sgn2(View v)
    {
        Intent iinent= new Intent(signup.this,signup2.class);
        startActivity(iinent);
    }
}
