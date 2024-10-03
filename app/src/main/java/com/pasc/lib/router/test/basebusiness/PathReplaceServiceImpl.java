package com.pasc.lib.router.test.basebusiness;

import android.content.Context;
import android.net.Uri;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PathReplaceService;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/8/24
 * @des 重定向 ， 解决版本路径随模块变更后 变化，而去适配的问题
 * @modify 路径随意，group要保证在 模块中独一无二 ，其他模块不能有
 **/
@Route(path = "/pascReplace/path/replace/")
public class PathReplaceServiceImpl implements PathReplaceService {
    @Override
    public String forString(String path) {

        return replacePath(path);

    }

    @Override
    public Uri forUri(Uri uri) {
        return Uri.parse(replacePath(uri.getPath()));
    }

    @Override
    public void init(Context context) {

    }

    /***
     * key  旧的路径
     * value  新的路径
     * 路由修改  重定向
     */
    private HashMap<String, String> pathReplaceMap = PathReplaceUtil.getPathReplaceMap();

    private String replacePath(String path) {
        if (pathReplaceMap!=null) {
            for (Map.Entry<String, String> entry : pathReplaceMap.entrySet()) {
                String hitPath = replacePath(path, entry.getKey(), entry.getValue());
                if (!isEmpty(hitPath)) {
                    path = hitPath;
                    break;
                }
            }
        }
        return path;
    }


    /**
     * @param path       传入的路径
     * @param originPath 旧的路径
     * @param targetPath 新的路径
     * @return
     */
    private String replacePath(String path, String originPath, String targetPath) {

        if (!isEmpty(path) && !isEmpty(originPath) && !isEmpty(targetPath)) {
            if (path.contains(originPath)) {
              String  hitPath = path.replace(originPath, targetPath);
              return hitPath;
            }
        }
        return null;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
