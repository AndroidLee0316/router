package com.pasc.lib.router.test.pascrouter;

import android.app.Application;
import android.os.Bundle;

import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.RouterManager;
import com.pasc.lib.router.interceptor.ApiGet;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/8/30
 * @des
 * @modify
 **/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RouterManager.initARouter(this,true);
        RouterManager.instance ().setApiGet (new ApiGet () {
            @Override
            public boolean isLogin() {
                return MyLoginActivity.isLogin;
            }

            @Override
            public boolean isCertification() {
                return MyCertificationActivity.isCertification;
            }

            @Override
            public void beforeInterceptor(String path, Bundle bundle) {
            }

            @Override
            public void gotoLogin(String targetPath, Bundle targetBundle) {
                BaseJumper.jumpARouter (RouterTable.User.USER_LOGIN_PATH);
            }

            @Override
            public void gotoCertification(String targetPath, Bundle targetBundle) {
                BaseJumper.jumpARouter (RouterTable.User.USER_CERTIFICATION_PATH);

            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RouterManager.destroyRouter();
    }
}
