package com.swsoft.walkingtogether;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


public class waiting extends AppCompatActivity {

    ArrayList<ChattingItem> chattingItems = new ArrayList<>();
    ListView chatting;
    EditText message;
    Button send;
    ChattingAdapter chattingAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        Toolbar createRoomToolBar = findViewById(R.id.waitingToolbar);
        setSupportActionBar(createRoomToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button start = (Button) findViewById(R.id.start);

        chatting = findViewById(R.id.chatting);
        chattingAdapter = new ChattingAdapter(waiting.this, chattingItems);
        chatting.setAdapter(chattingAdapter);


        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("chatroomname");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChattingItem item = snapshot.getValue(ChattingItem.class);
                chattingItems.add(item);

                chattingAdapter.notifyDataSetChanged(); //전체를 갱신할때
//              chattingAdapter.notifyInsertItem();// 리사이클러뷰 에서 가능한 코드

                chatting.setSelection(chattingItems.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //메세지보내기버튼
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sendmessage = message.getText().toString();
                Calendar calendar = Calendar.getInstance();
                String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

                //item단위로 서버로 저장하기
                ChattingItem item = new ChattingItem(logininfo.nickname,sendmessage,time,logininfo.profileURL);
                databaseReference.push().setValue(item);
                message.setText("");
            }
        });



    //산책시작
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),walking.class);
                startActivity(intent);
                finish();
            }
        });
    }


    // 뒤로가기 버튼 눌렀을때
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),roomlist.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // 디바이스의 뒤로가기 버튼 눌렀을때
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(),roomlist.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
