package com.swsoft.walkingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void howto(View view){
        Intent intent = new Intent(this, howto.class);
        this.startActivity(intent);

    }

    public void gotologin(View view) {

        Intent intent = new Intent(this, login.class);
        this.startActivity(intent);
    }

}