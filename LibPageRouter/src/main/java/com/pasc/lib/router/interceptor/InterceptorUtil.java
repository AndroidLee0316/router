package com.pasc.lib.router.interceptor;

import android.app.Activity;
import android.os.Build;

import com.pasc.lib.router.BaseJumper;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzijian
 * @date 2018/12/11
 * @des 处理不是事跳转界面的
 * @modify
 **/
public class InterceptorUtil {

    public interface InterceptorCallback {
        void onSuccess(Activity activity, String url, Map<String, String> param);
    }

    private InterceptorUtil() {

    }

    private final static class SingleHolder {
        private static final InterceptorUtil INTERCEPTOR_HOLDER = new InterceptorUtil ();
    }

    public static InterceptorUtil instance() {
        return InterceptorUtil.SingleHolder.INTERCEPTOR_HOLDER;
    }

    private InterceptorCallback callback;
    private WeakReference<Activity> activity;
    private String url;
    private Map<String, String> param;



    protected boolean isValid() {
        return callback != null;
    }

    protected WeakReference<Activity> getActivity() {
        return activity;
    }

    /**
     * Activity 是否已经销毁
     *
     * @return
     */
    protected static boolean isActivityDestroy(Activity activity) {
        if (activity == null) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed ()) {
            return true;
        }

        return false;
    }

    protected void continueService(Activity activity) {
        if (callback != null && activity != null) {
            callback.onSuccess (activity, url, param);
        }
        clearCallback ();
    }
    public void clearCallback() {
        callback = null;
        activity = null;
        url = null;
        param = null;

    }
    public void startService(Activity activity, String url, Map<String, String> param, InterceptorCallback callback) {
        this.activity = new WeakReference<> (activity);
        this.callback = callback;
        this.url = url;
        this.param = param;
        HashMap<String, String> hashMap = new HashMap<> ();
        if (param != null) {
            hashMap.putAll (param);
        }
        BaseJumper.jumpSeriaARouter (BaseRouterTable.Path.PATH_INTERCEPTOR_ACTIVITY, hashMap);
    }


}
