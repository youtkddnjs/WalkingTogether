package com.swsoft.walkingtogether;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class Login extends AppCompatActivity {

    EditText inputID;
    EditText inputPW;

    int idcount=0;
    int pwcount=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        inputID = findViewById(R.id.inputID);
        inputPW = findViewById(R.id.inputPW);

        //kakaologin
        ImageView kakaologin = findViewById(R.id.kakaologin);
        kakaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().loginWithKakaoAccount(Login.this, new Function2<OAuthToken, Throwable, Unit>() {
                    @Override
                    public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                        String token = String.valueOf(oAuthToken);
                        Log.i("oAuthToken",token);

                        if (oAuthToken != null) {
                            Toast.makeText(Login.this, "로그인", Toast.LENGTH_SHORT).show();

                            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                                @Override
                                public Unit invoke(User user, Throwable throwable) {
                                    if (user != null) {

                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference signupRef = firebaseDatabase.getReference("member");

                                        String NewID = user.getId()+"";
                                        String NewPW = user.getId()+"";
                                        String NewNickName = user.getKakaoAccount().getProfile().getNickname();
                                        String NewProFile = user.getKakaoAccount().getProfile().getProfileImageUrl();

                                        //정보를 Item으로 만들어서 보낼것
                                        SignupItem item = new SignupItem(NewID,NewPW,NewNickName,NewProFile);

                                        //ID를 Key으로한 노드에 정보 입력됨.
                                        signupRef.child(NewID).setValue(item);

//                                        kakaologininfo.id = user.getId();
//                                        kakaologininfo.nickname = user.getKakaoAccount().getProfile().getNickname();
//                                        kakaologininfo.profileURL = user.getKakaoAccount().getProfile().getProfileImageUrl();
                                        LoginUserInfo.id = user.getId()+"";
                                        LoginUserInfo.nickname = user.getKakaoAccount().getProfile().getNickname();
                                        LoginUserInfo.profileURL = user.getKakaoAccount().getProfile().getProfileImageUrl();
                                        savedata();

                                        KakaoLoginUserInfo.loginway=true;

                                        Intent intent = new Intent(Login.this, RoomList.class);
                                        Login.this.startActivity(intent);
                                        finish();
                                    }//if
                                    return null;
                                }// invok
                            }); // UserApiClient.getInstance
                        } else {
                            Toast.makeText(Login.this, "사용자 정보 실패", Toast.LENGTH_SHORT).show();
                        } //else
                        return null;
                    } // invok
                });//UserApiClient.getInstance().loginWithKakaoAccount
            } //click
        }); //kakaologin.setOnClickListener
    }//oncreate

    //일반 로그인
    public void login(View view) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dologinRef = firebaseDatabase.getReference("member");
        dologinRef.addValueEventListener(valueEventListener);
    }

    //뒤로가기 버튼
    @Override
    public void onBackPressed() {
        Intent intentsetting = new Intent(Login.this, MainActivity.class);
        Login.this.startActivity(intentsetting);
        super.onBackPressed();
    }

    //회원가입 버튼
    public void signUp(View view) {
        Intent intent = new Intent(this, SignupPage.class);
        this.startActivity(intent);
        finish();
    }//회원가입 버튼
    

    //DatabaseReference의 참조변수의 리스너의 매개변수를 참조변수로 뽑아냄
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Iterable<DataSnapshot> keys = snapshot.getChildren();
            for (DataSnapshot t : keys) {
                SignupItem item = t.getValue(SignupItem.class);
                if (inputID.getText().toString().equals(item.ID)) {
                    idcount=-1; //아이디를 찾으면 -1
                    if(inputPW.getText().toString().equals(item.PW)){
                        pwcount=-1;
                        LoginUserInfo.id=item.ID;
                        LoginUserInfo.nickname=item.NickName;
                        LoginUserInfo.profileURL=item.ProFile;
                        break;
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("로그인실패");
                        builder.setMessage("비밀번호를 못 찾았습니다.");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    }
                }else{
                    idcount++; //아이디를 못찾으면 증가
                    continue;
                }
            }//아이디를찾는 for문

            if(idcount>0){ //아이디를 못찾아서 증가되면 아래 실행
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("로그인실패");
                builder.setMessage("아이디를 찾지 못하였습니다.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            if(pwcount==-1){
                savedata();
                Intent intent = new Intent(Login.this, RoomList.class);
                Login.this.startActivity(intent);
                finish();
            }

        } //valueEventListener

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }; //valueEventListener


    //액티비티가 메모리에서 사라질때!
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dologinRef = firebaseDatabase.getReference("member");
        dologinRef.removeEventListener(valueEventListener);

    }

    void savedata(){
        //SharedPreferences에 저장하기
        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
        //쓰기작업 시작을 알림
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id", LoginUserInfo.id);
        editor.putString("nickname", LoginUserInfo.nickname);
        editor.putString("profile", LoginUserInfo.profileURL);
        editor.commit();
    }

}//main


