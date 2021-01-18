package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); EditText edt=(EditText)findViewById(R.id.city);

    }
    public void searchByCitybtn(View view){
        Intent intent = new Intent(this, MainActivity4.class);
        EditText editText = (EditText) findViewById(R.id.city);
        String message = editText.getText().toString();
        intent.putExtra("city", message);
        startActivity(intent);
    }
}