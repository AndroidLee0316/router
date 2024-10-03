package com.pasc.lib.router.test.basebusiness;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;

/***
 *
 * 中转 url跳转过来的Activity
 * 跳转 url 路径   url = "scheme://host" + target_router_path + parameters
 * ps  url="router://com.pasc.smt/login/activity/main?age=18&name=jay&job=android"
 * scheme=router
 * host=com.pasc.smt
 * target_router_path=/login/activity/main
 * parameters=age=18&name=jay&job=android
 *
 * scheme 推荐用自定义的，
 * 如果 使用 http 或者 https 的话， 会弹出 选择对话框 （ps 同时弹出多个浏览器选择）
 *
 */
public class FilterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            //对URI 数据分发
            Uri uri = getIntent().getData();
            //直接跳转目标Activity
            ARouter.getInstance().build(uri).navigation(this, new NavCallback() {
                @Override
                public void onArrival(Postcard postcard) {
                    //找到路径
                    // 关闭中转Activity
                    finish();
                }

                @Override
                public void onLost(Postcard postcard) {
                    //找不到路径
                    // 关闭中转Activity
                    finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            finish();
        }

    }
}
