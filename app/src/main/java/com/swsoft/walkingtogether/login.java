package com.swsoft.walkingtogether;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class login extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        ImageView kakaologin = findViewById(R.id.kakaologin);
        kakaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().loginWithKakaoAccount(login.this, new Function2<OAuthToken, Throwable, Unit>() {
                    @Override
                    public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                        String token = String.valueOf(oAuthToken);
                        Log.i("oAuthToken",token);

                        if (oAuthToken != null) {
                            Toast.makeText(login.this, "로그인", Toast.LENGTH_LONG).show();

                            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                                @Override
                                public Unit invoke(User user, Throwable throwable) {
                                    if (user != null) {
                                        kakaologininfo.id = user.getId();
                                        kakaologininfo.nickname = user.getKakaoAccount().getProfile().getNickname();

                                        Intent intent = new Intent(login.this, roomlist.class);
                                        login.this.startActivity(intent);
                                        finish();
                                    }//if
                                    return null;
                                }// invok
                            }); // UserApiClient.getInstance
                        } else {
                            Toast.makeText(login.this, "사용자 정보 실패", Toast.LENGTH_SHORT).show();
                        } //else
                        return null;
                    } // invok
                });//UserApiClient.getInstance().loginWithKakaoAccount
            } //click
        }); //kakaologin.setOnClickListener
    }//oncreate

    public void login(View view) {
        Intent intent = new Intent(this, roomlist.class);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        Intent intentsetting = new Intent(login.this, MainActivity.class);
        login.this.startActivity(intentsetting);
        super.onBackPressed();
    }
}


