package com.github.bg.admin.core.auth.impl;


import com.github.bg.admin.core.auth.ReleaseUrl;
import com.github.bg.admin.core.constant.SystemStaticConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author linzf
 * @since 2019-06-04
 * 类描述：放行的用户权限的地址
 */
@Component
public class ReleaseUrlProvider implements ReleaseUrl {

    /**
     * 此处的配置信息从配置文件中获取
     */
    @Value("${auth.releaseUrl}")
    private String releaseUrl;

    /**
     * 功能描述：获取放行的权限的数据
     * @return 返回放行的权限的数据
     */
    @Override
    public String getReleaseUrl() {
        if(releaseUrl==null){
            releaseUrl = "";
        }
        releaseUrl = releaseUrl + ":" + SystemStaticConst.LOGIN_URL + ":" + SystemStaticConst.USER_INFO_URL  + ":" + SystemStaticConst.REFRESH_URL;
        return releaseUrl;
    }
}
