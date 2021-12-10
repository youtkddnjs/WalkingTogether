package com.swsoft.walkingtogether;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomInfo extends AppCompatActivity {

    ArrayList<CreatRoomItem> creatroomItems = new ArrayList<>();
    TextView roominfotitle;
    String latitude;
    String longitude;
    SupportMapFragment mapFragment;
    GoogleMap mapMarker;
    String chatroom;
    String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roominfo);

        Toolbar roominfoToolbar = findViewById(R.id.roominfoToolbar);
        setSupportActionBar(roominfoToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //roomlist_apater.java에서 보낸 intent정보 얻기
        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        roominfotitle = findViewById(R.id.roominfotitle);

        roominfotitle.setText(title);



        //Google Map 참조 변수
        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.roominfofragment_map);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference roomInfoRef = firebaseDatabase.getReference("room");
        roomInfoRef.addValueEventListener(valueEventListener);

        //참석하기 버튼
        Button joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Waiting.class);
                startActivity(intent);
                finish();
            }
        });



    }//onCreate

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Iterable<DataSnapshot> keys = snapshot.getChildren();

            for(DataSnapshot t : keys) {
                CreatRoomItem item = t.getValue(CreatRoomItem.class);
                Log.i("tag###","["+title+"]" + ":" + "["+item.roomTitle+"]");
                if(title.equals(item.roomTitle)) {
                    ChatRoom.name = item.roomchattitle;
                    ChatRoom.latitude = item.roomLatitude;
                    ChatRoom.longitude = item.roomLongitude;
                    showMap();
                    return;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(RoomInfo.this);
            builder.setTitle("정보");
            builder.setMessage("방을 찾을 수 없습니다.");
            builder.setCancelable(false);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }

    };

    void showMap(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                double lat = Double.parseDouble(ChatRoom.latitude);
                double longi = Double.parseDouble(ChatRoom.longitude);
                LatLng latLng = new LatLng(lat,longi);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                if (ActivityCompat.checkSelfPermission(RoomInfo.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RoomInfo.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                mapMarker = googleMap;

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("출발");
                markerOptions.position(new LatLng(lat, longi));
                mapMarker.addMarker(markerOptions);

            }
        });//map
    }

    //g
//    ValueEventListener valueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            Iterable<DataSnapshot> keys = snapshot.getChildren();
//
//            for(DataSnapshot t : keys) {
//                CreatRoomItem item = t.getValue(CreatRoomItem.class);
//
//                ChatRoom.name = item.roomchattitle;
//
//            }
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//
//    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference  roomInfoRef = firebaseDatabase.getReference("room");
        roomInfoRef.removeEventListener(valueEventListener);
    }

    //뒤로가기 툴바 메뉴
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
    }//뒤로가기 툴바 메뉴

    //뒤로가기
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), RoomList.class);
        startActivity(intent);
        super.onBackPressed();
    }//뒤로가기
}
