package com.pasc.lib.router.test.basebusiness;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;


/**
 * 自定义全局降级策略
 */
@Route(path = "/pascDegrade/path/degrade/")
public class DegradeServiceImpl implements DegradeService {
    @Override
    public void onLost(Context context, Postcard postcard) {
        //找不到路径
    }
    @Override
    public void init(Context context) {
    }
}
