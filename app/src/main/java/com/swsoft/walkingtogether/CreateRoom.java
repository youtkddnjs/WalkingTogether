package com.swsoft.walkingtogether;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateRoom extends AppCompatActivity {

    Button create;
    Spinner spinner_personnel;
    Button time;
    int ahour=0;
    int aminute=0;
    EditText createRoomTitle;
    SupportMapFragment mapFragment;
    LocationManager locationManager;
    GoogleMap mapMarker;

    TextView tv_hour;
    TextView tv_minute;

    double latitude;
    double longitude;
    double mapLatitude;
    double mapLongitude;

    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom);

        //### Toolbar
        Toolbar createRoomToolBar = findViewById(R.id.createRoomToolbar);
        setSupportActionBar(createRoomToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Toolbar ###

        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        time = findViewById(R.id.time);
        spinner_personnel = findViewById(R.id.spinner_personnel);
        create = findViewById(R.id.create);
        createRoomTitle = findViewById(R.id.createRoomTitle);

        //Google Map 참조 변수
        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.fragment_map);

        //위치정보관리자 객체
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //위치정보제공자 중에서 최적의 제공자를 계산해서 알려주는 기능

        Criteria criteria = new Criteria();// Criteria 베스트제공자 판별하기 위한 기준 객체체
        criteria.setCostAllowed(true); // 비용지불 감수 여부
        criteria.setAccuracy(Criteria.NO_REQUIREMENT); //정확도를 요하는가?
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT); //베터리 소모량
        criteria.setAltitudeRequired(true); //고도에 대한 위치정보도 필요한가?

        //bestProvider 사용자 허가가 필요로함
        String bestProvider = locationManager.getBestProvider(criteria, true);

        //마시멜로우버전 에서 동적퍼미션 기능
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //이전에 허용되어 있었는지 확인하는 코드
            int checkResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkResult == PackageManager.PERMISSION_DENIED) { //거부 되어있다면
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, 0);
            }//if
        }//if


        //### 인원 선택 spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateRoom.this, R.array.spinner_personner_array,R.layout.spinner_selected);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_personnel.setAdapter(adapter);

        spinner_personnel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); //인원 선택 spinner ###

        //###시간 선택
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(CreateRoom.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay == 0 ){
                            tv_hour.setText(hourOfDay+"0");
                        }else{tv_hour.setText(hourOfDay+"");}
                        if(minute == 0){
                            tv_minute.setText(minute+"0");
                        }else{tv_minute.setText(minute+"");}
                    }
                },ahour,aminute,false);
                tpd.show();
            }
        }); //시간 선택###

        
        //좌표 가져오기
        Location location = null;

        //위치정보제공자 선택
        if (locationManager.isProviderEnabled("gps")) {
            //명시적으로 퍼미션체크 코드가 있어야 아래에 작성한 위치정보 취득 코드가 동작할 수있음.
            if (ActivityCompat.checkSelfPermission(CreateRoom.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateRoom.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //5초마다 또는 2M넘어갈때마다 찾으면 locationListener()를 한다.
            locationManager.requestLocationUpdates("gps",5000,2, locationListener);
            location = locationManager.getLastKnownLocation("gps"); //위치정보제공자 gps 사용
        }else if(locationManager.isProviderEnabled("network")){
            locationManager.requestLocationUpdates("network",5000,2, locationListener);
            location = locationManager.getLastKnownLocation("network"); //위치정보제공자 network 사용
        }

        //마지막 위치
        if(location == null) {
            Toast.makeText(CreateRoom.this, "위치를 못찾았습니다.", Toast.LENGTH_SHORT).show();
        }
        else{
                    latitude = location.getLatitude();//위도
                    longitude = location.getLongitude();//경도
        }



        //비동기 방식(별도 스레드)으로 지도데이터를 읽어오도록 요청
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                LatLng latLng = new LatLng(latitude,longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                if (ActivityCompat.checkSelfPermission(CreateRoom.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateRoom.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                mapMarker = googleMap;
                mapMarker.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {

                        mapMarker.clear();

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title("출발");

                        mapLatitude = latLng.latitude; // 위도
                        mapLongitude = latLng.longitude; // 경도
                        // 마커의 스니펫(간단한 텍스트) 설정
//                        markerOptions.snippet(latitude.toString() + ", " + longitude.toString());
                        // LatLng: 위도 경도 쌍을 나타냄
                        markerOptions.position(new LatLng(mapLatitude, mapLongitude));

                        mapMarker.addMarker(markerOptions);

                    }//onMapClick
                });//setOnMapClickListener
            }
        });//map

        //실시간 위치정보 종료
        locationManager.removeUpdates(locationListener);

        //방생성버튼
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("ddhhss");

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                DatabaseReference createRoomRef = firebaseDatabase.getReference("room");

                String title = createRoomTitle.getText().toString();
                String hour = tv_hour.getText().toString();
                String minute = tv_minute.getText().toString();
                String latitude = mapLatitude+"";
                String longitude = mapLongitude+"";
                String chatroom = hour+minute+sdf.format(new Date())+"";

                CreateRoomItem item = new CreateRoomItem(title, hour, minute, latitude, longitude,chatroom);

                createRoomRef.push().setValue(item);

                Intent intent = new Intent(getApplicationContext(), Waiting.class);
                intent.putExtra("chatroom",chatroom);

                startActivity(intent);

                finish();
            }
        });
    } //oncreate

    //locationListener
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    };//locationListener

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
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), RoomList.class);
        startActivity(intent);
        super.onBackPressed();
    }

    //requestPermissions()메소드의 호출을 통해 보여진 다이얼로그의 선택이 완료되면
    //자동으로 실행횓는 콜백메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(CreateRoom.this, "위지정보제공에 동의하였습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    finish();
                }
                break;
        }

    }//onRequestPermissionsResult

}//createroom



