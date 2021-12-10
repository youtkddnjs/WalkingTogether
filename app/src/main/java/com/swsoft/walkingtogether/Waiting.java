package com.swsoft.walkingtogether;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class Waiting extends AppCompatActivity {

    ArrayList<ChattingItem> chattingItems = new ArrayList<>();
    ListView chatting;
    EditText message;
    Button start;
    Button send;
    ChattingAdapter chattingAdapter;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        //### Toolbar
        Toolbar createRoomToolBar = findViewById(R.id.waitingToolbar);
        setSupportActionBar(createRoomToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String chatroom = intent.getStringExtra("chatroom");

        chatting = findViewById(R.id.chatting);
        chattingAdapter = new ChattingAdapter(Waiting.this, chattingItems);
        chatting.setAdapter(chattingAdapter);
        //Toolbar ###

        start = findViewById(R.id.start);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        firebaseDatabase = FirebaseDatabase.getInstance();

//        DatabaseReference readyRef = firebaseDatabase.getReference("ready/"+ChatRoom.name);
//        readyRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



        databaseReference = firebaseDatabase.getReference("chatroomname/"+ChatRoom.name);
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
                ChattingItem item = new ChattingItem(LoginUserInfo.nickname,sendmessage,time, LoginUserInfo.profileURL);
                databaseReference.push().setValue(item);
                message.setText("");
            }
        });



    //산책시작
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = firebaseDatabase.getReference("chatroomname/"+ChatRoom.name);
                databaseReference.removeValue();

                databaseReference = firebaseDatabase.getReference("room/"+ChatRoom.name);
                databaseReference.removeValue();

                Intent intent = new Intent(getApplicationContext(), Walking.class);
                startActivity(intent);
                finish();
            }
        });
    } //onCreat


    // 뒤로가기 버튼 눌렀을때
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), RoomList.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    } // 뒤로가기
    
    // 디바이스의 뒤로가기 버튼 눌렀을때
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), RoomList.class);
        startActivity(intent);
        super.onBackPressed();
    } //onBackPressed
} //Waiting
