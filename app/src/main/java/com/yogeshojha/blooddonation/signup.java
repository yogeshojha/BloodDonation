package com.yogeshojha.blooddonation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import customfonts.MyEditText;

public class signup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    public void sgn2(View v)
    {
        MyEditText text = (MyEditText) findViewById(R.id.name);
        String value = text.getText().toString();
        Intent iinent= new Intent(signup.this,signup2.class);
        iinent.putExtra("Name", value);
        startActivity(iinent);
    }
}
