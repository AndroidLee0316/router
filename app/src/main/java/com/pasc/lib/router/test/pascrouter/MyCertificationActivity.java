package com.pasc.lib.router.test.pascrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.lib.router.interceptor.CertificationInterceptor;

/**
 * @author yangzijian
 * @date 2018/12/7
 * @des
 * @modify
 **/
@Route(path = RouterTable.User.USER_CERTIFICATION_PATH)
public class MyCertificationActivity extends AppCompatActivity {
    public static boolean isCertification=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.my_login);
        Button button=findViewById (R.id.btn);
        button.setText ("实名认证");
    }

    public void viewClick(View view){
        isCertification=true;
        finish ();
        Toast.makeText (this,"实名认证成功",Toast.LENGTH_SHORT).show ();
        CertificationInterceptor.notifyCallBack (true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        CertificationInterceptor.notifyCallBack (false);

    }
}
