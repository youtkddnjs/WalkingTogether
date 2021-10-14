package com.swsoft.walkingtogether;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;



public class myinfo extends AppCompatActivity {

    TextView nickname;
    CircleImageView profileImage;
    Button myinfoprofile_imageedit;
    Uri imageURI;

    boolean isFirst = true;
    boolean isChanged = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);

        Toolbar myinfoToolbar = findViewById(R.id.myinfoToolbar);
        setSupportActionBar(myinfoToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        nickname = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.myinfoprofile_image);

        //SharedPreferences에 저장되어 있는 닉네임, 프로필이미지 있다면 읽어오기

        loadData();
        if(logininfo.nickname != null){
            nickname.setText(logininfo.nickname);
            Glide.with(myinfo.this).load(logininfo.profileURL).into(profileImage);
            isFirst = false;
        }


        //프로필 사진 변경 버튼
        myinfoprofile_imageedit = findViewById(R.id.myinfoprofile_imageedit);
        myinfoprofile_imageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); //이미지의 모든 확장자
                resultLauncher.launch(intent);
            }
        }); //프로필 사진 변경 버튼


        //확인 버튼 클릭시 roomlist로 이동
        Button confirmmyinfo = findViewById(R.id.confirm_myinfo);
        confirmmyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveData();
//                Intent intent = new Intent(getApplicationContext(),roomlist.class);
//                startActivity(intent);
//                finish();

                if(isFirst||isChanged){
                    saveData();
                    Intent intent = new Intent(getApplicationContext(),roomlist.class);
                    startActivity(intent);
                    finish();

                }else{
                    Intent intent = new Intent(getApplicationContext(),roomlist.class);
                    startActivity(intent);
                    finish();
                }
            }
        }); //확인버튼



//        Glide.with(myinfo.this).load(kakaologininfo.profileURL).into(profileImage);


    }//oncreate


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
    } //onPotionsItemSelected


    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),roomlist.class);
        startActivity(intent);
        super.onBackPressed();
    } //onBackPressed

    //이미지 결과를 가져와서 실행하는 객체
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            //파라미터로 전달되 결과객체 ActivityResult result로 부터 결과코드와 돌아온 Intent처리
            if(result.getResultCode() == RESULT_OK){
                //결과물 데이터를 가지고 돌안온 Intent 얻어오기
                Intent intent = result.getData();
                imageURI = intent.getData(); // 선택한 프로필사진의 경로 uri 결과 데이터

                Glide.with(myinfo.this).load(imageURI).into(profileImage);
                isChanged = true;
            }
        }
    });

    void saveData(){
        if(imageURI == null) return;
        //년월일시분초 를기준으로 파일이름 만들기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = "IMG_" + sdf.format(new Date()) + ".png";
        //FirebaseStorage 관린 객체 및 서버 연결
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        //profileImage 라는 폴더 안에 중복되지 않는 파일명으로 참조노드만들고 참조객체 얻어오기
        StorageReference imgRef =firebaseStorage.getReference("profileImage/"+fileName);
        //파일 업로드
        UploadTask uploadTask = imgRef.putFile(imageURI);
        //업로드가 성공되면 아래 코드 실행
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //업로드된 파일의 다운로드 주소를 얻어오기
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //firebase 저장소에 저장되어 있는 이미지의 다운로드 주소 url을 문자열로 가져오기
                        logininfo.profileURL = uri.toString();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference profilesRef = firebaseDatabase.getReference("profiles");

                        profilesRef.child(logininfo.nickname).setValue(logininfo.profileURL);

                        //SharedPreferences에 저장하기
                        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
                        //쓰기작업 시작을 알림
                        SharedPreferences.Editor editor = pref.edit();
                        
                        //쓰기작업
                        editor.putString("nickname", logininfo.nickname);
                        editor.putString("profile",logininfo.profileURL);

                        //쓰기 작업 종료
                        editor.commit();

                    }
                });
            }
        }); //업로드 성공시
    } //saveData

    void loadData(){
        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
        logininfo.nickname = pref.getString("nickname", null);
        logininfo.profileURL = pref.getString("profile",null);
    }


} //main
