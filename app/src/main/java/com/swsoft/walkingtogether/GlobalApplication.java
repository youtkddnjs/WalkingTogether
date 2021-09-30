package com.swsoft.walkingtogether;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;


public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, "80e60226ec3a106a2f6b4f9462da7a74");

    }
}
