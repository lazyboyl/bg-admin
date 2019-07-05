package com.github.bg.admin.core.service;

import com.github.bg.admin.core.auth.Auth;
import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.TreeDao;
import com.github.bg.admin.core.dao.UserDao;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.entity.Tree;
import com.github.bg.admin.core.entity.User;
import com.github.bg.admin.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019/07/05
 * 类描述：用户的service类的实现
 */
@Service
@Transactional(rollbackFor = {IllegalArgumentException.class})
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TreeDao treeDao;

    /**
     * 鉴权的实现
     */
    @Autowired
    private Auth authProvider;

    /**
     * 功能描述：实现用户登陆
     *
     * @param loginAccount  用户账号
     * @param loginPassword 用户密码
     * @return 返回登陆结果
     */
    public ReturnInfo login(String loginAccount, String loginPassword) {
        return authProvider.login(loginAccount, loginPassword);
    }

    /**
     * 功能描述：根据token来获取用户数据
     *
     * @param token token的值
     * @return 返回获取的结果
     */
    public ReturnInfo getUserInfo(String token) {
        User user = userDao.getUserInfo(token);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "获取账号信息错误");
        }
        user.setLoginPassword(null);
        List<Tree> treeList = treeDao.getLoginUserTree(user.getUserId());
        String[] access = new String[treeList.size()];
        for (int i = 0; i < treeList.size(); i++) {
            access[i] = treeList.get(i).getTreeCode();
        }
        Map<String, Object> result = JsonUtils.objToMap(user);
        result.put("access", access);
        return new ReturnInfo(SystemStaticConst.SUCCESS, "登陆成功", result);
    }

}
