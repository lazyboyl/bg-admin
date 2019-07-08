package com.github.bg.admin.core.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageUtil {

    private PageUtil() {
    }

    public static void startPage(Map<String, String> reqMap) {
        int page = Integer.parseInt(reqMap.getOrDefault("page", "1"));
        int size = Integer.parseInt(reqMap.getOrDefault("size", "20"));
        PageHelper.startPage(page, (size > 0 && size <= 500) ? size : 20);
    }

    @Deprecated
    public static HashMap<String, Object> getResultMap(List<?> result) {
        return getResult(result);
    }

    public static HashMap<String, Object> getResult(List<?> result) {
        PageInfo<?> pageInfo = new PageInfo<>(result);
        HashMap<String, Object> res = new HashMap<>(4);
        res.put("page", pageInfo.getPageNum());
        res.put("size", pageInfo.getPageSize());
        res.put("total", pageInfo.getTotal());
        res.put("rows", pageInfo.getList());
        return res;
    }

    public static void startPageObject(Map<String, Object> reqMap) {
        int page = Integer.parseInt(reqMap.getOrDefault("page", "1").toString());
        int size = Integer.parseInt(reqMap.getOrDefault("size", "20").toString());
        PageHelper.startPage(page, (size > 0 && size <= 500) ? size : 20);
    }
}
