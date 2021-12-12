package com.swsoft.walkingtogether;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Finish extends AppCompatActivity {

    int hour;
    int minute;
    int second;

    TextView resulttime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Toolbar createRoomToolBar = findViewById(R.id.finishRoomToolbar);
        setSupportActionBar(createRoomToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button complete = (Button) findViewById(R.id.complete);

        Intent intent = getIntent();
        hour = intent.getIntExtra("hour",0);
        minute = intent.getIntExtra("minute",0);
        second = intent.getIntExtra("second",0);

        resulttime = findViewById(R.id.resulttime);

        resulttime.setText(hour+"시간"+ minute+"분"+second+"초 입니다.");


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RoomList.class);
                startActivity(intent);
                finish();
            }
        });//완료버튼
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), RoomList.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }//뒤로가기

    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), RoomList.class);
        startActivity(intent);
        super.onBackPressed();
    }//뒤로가기
}//Finish

