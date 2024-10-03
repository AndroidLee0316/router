package com.pasc.lib.router;

import android.app.Activity;

import java.util.Map;

public interface ServiceHandlerCallback {
    void onSuccess(Activity activity, String url, Map<String, String> param);

    void onError(Activity activity, String url, Map<String, String> param, int errorCode, String errorMsg);
}
