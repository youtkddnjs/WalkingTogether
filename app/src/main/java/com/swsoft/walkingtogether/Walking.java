package com.swsoft.walkingtogether;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;


public class Walking extends AppCompatActivity {

    TextView hour;
    TextView minute;
    TextView second;
    TextView stoptime;
    RunningTime runningTime;

    int timehour;
    int timemin;
    int timesec;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        Toolbar createRoomToolBar = findViewById(R.id.walkingRoomToolbar);
        setSupportActionBar(createRoomToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button finish = (Button) findViewById(R.id.finish);

        hour = findViewById(R.id.runninghour);
        minute = findViewById(R.id.runningminute);
        second = findViewById(R.id.runningsecond);
        stoptime = findViewById(R.id.stoptime);

        runningTime = new RunningTime();
        runningTime.start();


        //산책완료
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runningTime.stopfor();

                Intent intent = new Intent(getApplicationContext(), Finish.class);
                intent.putExtra("hour",timehour);
                intent.putExtra("minute",timemin);
                intent.putExtra("second",timesec);
                startActivity(intent);
                finish();
            }
        });//산책완료


    }//onCreate

    class RunningTime extends Thread {
        boolean isRun = true;
        boolean forRun= false;
        int h;
        int m;
        int s;
        @Override
        public void run() {

            for(h=0;h<99;h++) {
                if (forRun) {
                    break;
                }
                for (m = 0; m < 60; m++) {
                    if (forRun) {
                        break;
                    }
                    for (s = 0; s < 60; s++) {
                        if (forRun) {
                            break;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(s<10){
                                    second.setText("0" + s);
                                }else{
                                    second.setText(s + "");
                                }
                                timesec = s;
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }//초
                    if (forRun) {
                        break;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(s<10){
                                minute.setText("0" + m);
                            }else{
                                minute.setText(m + "");
                            }
                            timemin = m;
                        }
                    });
                }//분
                if (forRun) {
                    break;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(s<10){
                            hour.setText("0" + h);
                        }else{
                            hour.setText(h + "");
                        }
                        timehour = h;

                    }
                });
            }//시

        }//run

       void stopfor(){
            forRun = true;
        }

    }//RunningTime



    //뒤로가기
    public void onBackPressed() {

    }//뒤로가기

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runningTime.stopfor();
    }
}//Walking