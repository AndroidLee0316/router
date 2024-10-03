package com.pasc.lib.router.test.pascrouter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.interceptor.BaseRouterTable;

import com.pasc.lib.router.test.basebusiness.servic.IUserService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void viewClick(View view) {
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.gotoLogin:
                BaseJumper.jumpARouter(RouterTable.User.USER_LOGIN_PATH);
                break;
            case R.id.gotoCertification:
                BaseJumper.jumpARouter(RouterTable.User.USER_CERTIFICATION_PATH);
                break;
            case R.id.gotoNeedLogin:
                bundle = new Bundle();
                bundle.putBoolean(BaseRouterTable.BundleKey.KEY_NEED_LOGIN, true);
                BaseJumper.jumpBundleARouter(RouterTable.User.USER_NEEDLOGIN_PATH, bundle);
                break;
            case R.id.gotoNeedCertification:
                bundle = new Bundle();
                bundle.putString(BaseRouterTable.BundleKey.KEY_NEED_LOGIN, "true");
                // bundle.putString(BaseRouterTable.BundleKey.KEY_NEED_IDENTITY, "true");
                bundle.putString(BaseRouterTable.BundleKey.KEY_NEED_CERT, "true");
                BaseJumper.jumpBundleARouter(RouterTable.User.USER_NEEDLOGINCERTIFICATION_PATH, bundle);
                break;
            case R.id.btnWeb:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case R.id.gotoInterface:
                IUserService iUserService = BaseJumper.getService(RouterTable.Api.SERVICE_API_PATH);
                if (iUserService != null) {
                    iUserService.doSomethings();
                }
                break;
        }
    }

}
