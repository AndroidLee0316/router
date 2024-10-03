package com.pasc.lib.router.interceptor;

public interface BaseRouterTable {


    public interface Flag {
        /*** 是否需要登陆 flag***/
        int FLAG_NEED_LOGIN = 1;
        /*** 是否需要实名认证 flag***/
        int FLAG_NEED_CERTIFICATION = 2;
    }

    public interface BundleKey {
        /*** 是否需要登陆 intent key***/
        String KEY_NEED_LOGIN = "needLogin";
        /**
         * 是否需要实名认证 intent key
         *
         * @deprecated 这是旧的字段，为了做兼容保留，新的请用KEY_NEED_CERT
         **/
        String KEY_NEED_IDENTITY = "needIdentity";
        String KEY_NEED_CERT = "needCert";
    }

    interface Path {
        String PATH_INTERCEPTOR_ACTIVITY = "/pascInterceptor/router/activity";
    }

}
