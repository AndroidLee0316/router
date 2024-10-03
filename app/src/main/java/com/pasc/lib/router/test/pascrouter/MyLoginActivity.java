package com.pasc.lib.router.test.pascrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.lib.router.interceptor.LoginInterceptor;

/**
 * @author yangzijian
 * @date 2018/12/7
 * @des
 * @modify
 **/
@Route (path = RouterTable.User.USER_LOGIN_PATH)
public class MyLoginActivity extends AppCompatActivity {

    public static boolean isLogin=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.my_login);
    }

    public void viewClick(View view){
        isLogin=true;
        finish ();
        Toast.makeText (this,"登陆成功",Toast.LENGTH_SHORT).show ();
        LoginInterceptor.notifyCallBack (true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy ();
        LoginInterceptor.notifyCallBack (false);
    }
}
