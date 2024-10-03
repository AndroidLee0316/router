package com.pasc.lib.router.test.pascrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * @date 2019/5/23
 * @des
 * @modify
 **/
@Route(path = RouterTable.Other.OTHER_DEEPLINK_PATH)
public class TestDeepLinkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Button button = new Button (this);
        button.setText ("Deeplink");
        setContentView (button);
        String text = getIntent ().getStringExtra ("keyUserId");
        if (!TextUtils.isEmpty (text))
            Log.e ("yzj", text);

    }
}
