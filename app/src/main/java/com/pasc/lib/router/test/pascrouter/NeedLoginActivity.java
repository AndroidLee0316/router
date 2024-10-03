package com.pasc.lib.router.test.pascrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * @author yangzijian
 * @date 2018/12/7
 * @des
 * @modify
 **/
@Route(path = RouterTable.User.USER_NEEDLOGIN_PATH)
//,extras = 1 << BaseRouterTable.Flag.FLAG_NEED_LOGIN)
public class NeedLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        Button button = new Button (this);

        button.setText ("我需要登陆");
        setContentView (button);
    }
}
