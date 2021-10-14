package com.swsoft.walkingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kakao.sdk.common.util.Utility;

public class MainActivity extends AppCompatActivity {

long pressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key = Utility.INSTANCE.getKeyHash(this);
        Log.i("key", key);


    } //onCreate

    public void mainlogo(View view){
        Intent intent = new Intent(this, howto.class);
        this.startActivity(intent);

    } //mainlogo

    public void gotologin(View view) {

        Intent intent = new Intent(this, login.class);
        this.startActivity(intent);
    } //gotologin

    @Override
    public void onBackPressed() {
        if ( pressedTime == 0 ) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
                pressedTime = 0 ;
            }
            else {
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }//onBackPressed
} //main