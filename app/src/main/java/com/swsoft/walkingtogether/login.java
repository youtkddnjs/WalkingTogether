package com.swsoft.walkingtogether;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class login extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

    }

    public void login(View view){
        Intent intent = new Intent(this, roomlist.class);
        this.startActivity(intent);
        finish();

    }
    @Override
    public void onBackPressed() {

        Intent intentsetting = new Intent(login.this, MainActivity.class);
        login.this.startActivity(intentsetting);
        super.onBackPressed();
    }

}
