package com.pasc.lib.router;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.pasc.lib.router.interceptor.ApiGet;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/7/16
 * @des
 * @modify
 **/
public class RouterManager {

    /**
     * 初始化路由
     */
    public static void initARouter(Application application, boolean debug) {
        if (debug) {
            ARouter.openLog ();     // 打印日志
            ARouter.openDebug ();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init (application); // 尽可能早，推荐在Application中初始化
    }

    /**
     * 销毁路由
     */
    public static void destroyRouter() {
        ARouter.getInstance ().destroy ();
    }

    private ApiGet apiGet = new ApiGet () {
    };

    public void setApiGet(ApiGet apiGet) {
        if (apiGet != null)
            this.apiGet = apiGet;
    }



    public ApiGet getApiGet() {
        return apiGet;
    }

    private final static class SingleHolder {
        private static final RouterManager ROUTER_MANAGER = new RouterManager ();
    }

    public static RouterManager instance() {
        return SingleHolder.ROUTER_MANAGER;
    }
    private RouterManager() {
    }
}
