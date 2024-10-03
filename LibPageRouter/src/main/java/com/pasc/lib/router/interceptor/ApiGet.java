package com.pasc.lib.router.interceptor;

import android.os.Bundle;

/**
 * @author yangzijian
 * @date 2018/12/7
 * @des
 * @modify
 **/
public abstract class ApiGet {
    public boolean enableInterceptorNotifyCallBack(){
        return true;
    }
    public boolean isLogin() {
        return true;
    }

    public boolean isCertification() {
        return true;
    }

    public void beforeInterceptor(String path, Bundle bundle){

    }

    public void gotoLogin(String targetPath, Bundle targetBundle) {
    }

    public void gotoCertification(String targetPath, Bundle targetBundle) {
    }

}
