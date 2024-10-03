package com.pasc.lib.router;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzijian
 * @date 2018/11/8
 * @des
 * @modify
 **/
public class ServiceProtocol {
    public static final String SmtTag = "smt://";
    public static final String EventTag = "event://";
    public static final String RouterTag = "router://";
    public static final String HttpTag = "http://";
    public static final String HttpsTag = "https://";

    /***EventBus***/
    public static final String eventKeyId = "eventKeyId";
    /**
     * http
     **/
    public static final String httpKeyId = "httpKeyId";
    /***默认***/
    public static final String defaultKeyId = "defaultKeyId";


    private static final class SingleHolder {
        private static ServiceProtocol protocol = new ServiceProtocol();
    }

    private ServiceProtocol() {
    }

    public static ServiceProtocol instance() {
        return SingleHolder.protocol;
    }

    private boolean needLog = true;

    public void setNeedLog(boolean needLog) {
        this.needLog = needLog;
    }

    /***id
     *
     * key -> id
     * value ->IServiceHandler
     *
     * ***/
    private static final Map<String, IServiceHandler> handlerMap = new HashMap<>();

    public boolean isServicesEmpty() {
        return handlerMap.isEmpty();
    }

    public boolean containService(String id) {
        if (id == null || TextUtils.isEmpty(id.trim())) {
            return false;
        }
        return handlerMap.get(id) != null;
    }

    /***
     * 注册服务能力
     * @param keyId
     * @param handler
     * @return
     */
    public ServiceProtocol register(String keyId, IServiceHandler handler) {
        if (!TextUtils.isEmpty(keyId) && handler != null) {
            handlerMap.put(keyId, handler);
        }
        return this;
    }

    /**
     * 开始服务跳转
     *
     * @param activity
     * @param param
     */
    public void startService(Activity activity, String url, Map<String, String> param) {
        startService(activity, url, param, null);
    }

    public void startService(final Activity activity, final String url, final Map<String, String> param, final ServiceHandlerCallback callback) {
        log("url=" + url + ", param = " + getMapStr(param));
        if (url != null && !TextUtils.isEmpty(url.trim())) {
            if (url.startsWith(SmtTag)) {
                String keyId = url.replace(SmtTag, "");
                IServiceHandler handler = handlerMap.get(keyId);
                if (handler != null) {
                    handler.handleService(activity, url, param);
                    log("找到对应的smt服务: " + handler.getClass().getSimpleName());
                } else {
                    log(url + "没找到对应的smt服务");
                }
            } else if (url.startsWith(EventTag)) {
                IServiceHandler handler = handlerMap.get(eventKeyId);
                if (handler != null) {
                    handler.handleService(activity, url, param);
                    log("找到对应的EventBus服务: " + handler.getClass().getSimpleName());
                } else {
                    log(url + "没找到对应的EventBus服务");
                }
            } else if (url.startsWith(RouterTag)) {
                if (callback != null) {
                    NavigationCallback navCallBack = new NavigationCallback() {
                        @Override
                        public void onFound(Postcard postcard) {
                            // do nothing
                        }

                        @Override
                        public void onLost(Postcard postcard) {
                            callback.onError(activity, url, param, 404, "路由跳转失败");
                        }

                        @Override
                        public void onArrival(Postcard postcard) {
                            callback.onSuccess(activity, url, param);
                        }

                        @Override
                        public void onInterrupt(Postcard postcard) {
                            // do nothing
                        }
                    };
                    BaseJumper.jumpSeriaARouter(url, param, navCallBack);
                } else {
                    BaseJumper.jumpSeriaARouter(url, param);
                }
            } else if (url.startsWith(HttpTag) || url.startsWith(HttpsTag)) {
                IServiceHandler handler = handlerMap.get(httpKeyId);
                if (handler != null) {
                    handler.handleService(activity, url, param);
                    log("找到对应的http服务: " + handler.getClass().getSimpleName());
                } else {
                    log(url + "没找到对应的Http服务");
                }
            } else {
                IServiceHandler handler = handlerMap.get(defaultKeyId);
                if (handler != null) {
                    handler.handleService(activity, url, param);
                    log("找到对应的default服务: " + handler.getClass().getSimpleName());
                } else {
                    log(url + "没找到对应的default服务");
                }
            }
        }

    }

    private String getMapStr(Map<String, String> param) {
        if (param != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("( ");
            for (Map.Entry<String, String> entry : param.entrySet()) {
                stringBuilder.append(entry.getKey() + ":" + entry.getValue() + " ,");
            }
            stringBuilder.append(" )");

            return stringBuilder.toString();
        }
        return "";
    }

    private void log(String msg) {
        if (needLog) {
            Log.d("serviceTag", msg);
        }
    }

}
