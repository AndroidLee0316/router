package com.pasc.lib.router.test.basebusiness;

import java.util.HashMap;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/8/25
 * @des 路由修改 重定向
 * @modify
 **/
public class PathReplaceUtil {
    private static final HashMap<String, String> pathReplaceMap = new HashMap<>();

    static {
        //可以修改路径
        // 比如 原本要跳转到 登陆界面 , 修改为 跳到 注册界面
        /****政务大厅模块修改 由app 移动到 BusinessGovernmentaffairs ,路径变化了，需要适配新路径* **/
//        pathReplaceMap.put("/service/onlineHall/main", GovernmentAffairsJumper.PATH_GOVERNMENT_GUIDE_ACTIVITY);
//        pathReplaceMap.put("/service/myAffairsDetail/main", GovernmentAffairsJumper.PATH_MYAFFAIRS_DETAIL_ACTIVITY);
//        pathReplaceMap.put("/service/myAffairs/main", GovernmentAffairsJumper.PATH_MYAFFAIRS_ACTIVITY);
    }

    public static HashMap<String, String> getPathReplaceMap() {
        return pathReplaceMap;
    }

}
