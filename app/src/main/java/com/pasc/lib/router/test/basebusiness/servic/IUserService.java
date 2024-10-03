package com.pasc.lib.router.test.basebusiness.servic;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/8/30
 * @des
 * @modify
 **/
public interface IUserService extends IProvider{
    String getUserId();
    void doSomethings();
}
