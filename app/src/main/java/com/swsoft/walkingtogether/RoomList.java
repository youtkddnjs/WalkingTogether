package com.swsoft.walkingtogether;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RoomList extends AppCompatActivity {

    ArrayList<RoomListItem> items = new ArrayList<>();
    FloatingActionButton roomlistfab;
    BottomNavigationView roomlistbottomnavigation;

    long pressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomlist);

        Toolbar roomlistToolbar = findViewById(R.id.roomlistToolbar);
        setSupportActionBar(roomlistToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //새로운 방만들기 버튼
        roomlistfab = findViewById(R.id.roomlistfloating);
        roomlistfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomList.this, CreatRoom.class);
                startActivity(intent);
                finish();
            }
        });

        //Bottom navigation
        roomlistbottomnavigation = findViewById(R.id.roomlistbottomnavigation);
        roomlistbottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){

                    case R.id.notice:
                        Intent intentnotice = new Intent(RoomList.this, Notice.class);
                        RoomList.this.startActivity(intentnotice);
                        finish();

                        break;
                    case R.id.myinfo:
                        Intent intentmyinfo = new Intent(RoomList.this, MyInfo.class);
                        RoomList.this.startActivity(intentmyinfo);
                        finish();

                        break;
                    case R.id.howto:
                        Intent intenthowto = new Intent(RoomList.this, Howto.class);
                        RoomList.this.startActivity(intenthowto);

                        break;
                    case R.id.setting:
                        Intent intentsetting = new Intent(RoomList.this, Setting.class);
                        RoomList.this.startActivity(intentsetting);
                        finish();
                        break;

                }
                return false;
            }
        });
    }//oncreate

    @Override
    public void onBackPressed() {
        if ( pressedTime == 0 ) {
            Toast.makeText(RoomList.this, " 한 번 더 누르면 로그아웃 됩니다." , Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(RoomList.this, " 한 번 더 누르면 로그아웃 됩니다." , Toast.LENGTH_SHORT).show();
                pressedTime = 0 ;
            }
            else {
                Toast.makeText(RoomList.this, " 로그아웃 되었습니다." , Toast.LENGTH_SHORT).show();
                Intent intentsetting = new Intent(RoomList.this, Login.class);
                RoomList.this.startActivity(intentsetting);
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }//뒤로가기 버튼

    //load 하기
    void loadData(){
        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
        LoginUserInfo.nickname = pref.getString("nickname", null);
        LoginUserInfo.profileURL = pref.getString("profile",null);
    }

}//roomlist
