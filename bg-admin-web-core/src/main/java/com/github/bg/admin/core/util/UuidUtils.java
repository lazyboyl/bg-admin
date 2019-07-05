package com.github.bg.admin.core.util;

import java.util.UUID;

/**
 * @author linzf
 * @since 2019-04-25
 * 类描述：uuid生成工具类
 */
public class UuidUtils {

    /**
     * 获取主键uuid
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取随机的UUID字符串
     * @return uuid
     */
    public static String getRandomUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toLowerCase();
    }



}
