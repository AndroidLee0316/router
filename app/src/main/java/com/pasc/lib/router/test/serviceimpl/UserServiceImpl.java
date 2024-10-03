package com.pasc.lib.router.test.serviceimpl;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.pasc.lib.router.test.basebusiness.servic.IUserService;
import com.pasc.lib.router.test.pascrouter.RouterTable;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/8/30
 * @des
 * @modify
 **/
@Route(path = RouterTable.Api.SERVICE_API_PATH)
public class UserServiceImpl implements IUserService {
    @Override
    public String getUserId() {
        return "9526";
    }

    @Override
    public void doSomethings() {
        Log.e ("userService","doSomethings");
    }

    @Override
    public void init(Context context) {

    }
}
