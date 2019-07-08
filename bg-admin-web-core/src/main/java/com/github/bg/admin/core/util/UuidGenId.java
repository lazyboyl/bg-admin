package com.github.bg.admin.core.util;

import tk.mybatis.mapper.genid.GenId;

import java.util.UUID;

/**
 * 类描述：生成主键的类
 * @author linzf
 */
public class UuidGenId implements GenId<String> {

    @Override
    public String genId(String table, String column) {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
