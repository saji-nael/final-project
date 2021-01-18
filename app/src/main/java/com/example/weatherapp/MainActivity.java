package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public final static String weatherKey = "4e53b3daa9226eed27b6c2d9a1c0f2a4";
   ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void currentLocationbtn(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
    public void searchByCity(View view) {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }
}