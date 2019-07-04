package com.github.bg.admin.core.constant;

/**
 * @author linzf
 * @since 2019/4/25
 * 类描述：系统静态实体类
 */
public class SystemStaticConst {


    /**
     * 系统返回的编码的key
     */
    public static final String RESULT = "code";

    /**
     * 系统返回的消息的key
     */
    public static final String MSG = "msg";

    /**
     * 请求操作成功的标志
     */
    public static final int SUCCESS = 200;

    /**
     * 请求操作失败的标志
     */
    public static final int FAIL = 400;

    /**
     * 当前TOKEN过期
     */
    public static final int AUTH_TOKEN_EXPIRE = 409;

    /**
     * 当前用户没有权限
     */
    public static final int AUTH_FAIL = 403;

    /**
     * 当前用户没有登录
     */
    public static final int NOT_LOGIN = 401;

    /**
     * 登陆的地址
     */
    public static final String LOGIN_URL = "/user/login";

    /**
     * 功能描述：刷新token的地址
     */
    public static final String REFRESH_URL = "/user/refreshToken";

    /**
     * 获取用户信息地址
     */
    public static final String USER_INFO_URL = "/user/getUserInfo";


    /**
     * 请求的方式
     */
    public static final String ACTION_TYPE_OPTIONS = "OPTIONS";


}
