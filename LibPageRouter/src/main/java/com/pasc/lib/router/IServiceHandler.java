package com.pasc.lib.router;

import android.app.Activity;

import java.util.Map;

/**
 * @author yangzijian
 * @date 2018/11/8
 * @des
 * @modify
 **/
public interface IServiceHandler {
    void handleService(Activity activity, String url, Map<String,String> param);
}
