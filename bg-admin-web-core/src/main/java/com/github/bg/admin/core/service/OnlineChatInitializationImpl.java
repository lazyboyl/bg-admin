package com.github.bg.admin.core.service;

import com.github.bg.admin.core.entity.User;
import com.github.bg.admin.core.util.RedisCache;
import com.github.bg.admin.core.util.UserInfo;
import com.github.lazyboyl.chat.core.auth.OnlineChatInitialization;
import com.github.lazyboyl.chat.core.dao.ChatUserDao;
import com.github.lazyboyl.chat.core.entity.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author linzf
 * @since 2020/8/10
 * 类描述：
 */
@Service
public class OnlineChatInitializationImpl implements OnlineChatInitialization {

    @Autowired
    private RedisCache redisCache;
    /**
     * 注入用户的dao
     */
    @Autowired
    private ChatUserDao chatUserDao;


    @Override
    public ChatUser getLoginChatUser() {
        User user = UserInfo.getLoginUser(redisCache);
        if (user == null) {
            return null;
        }
        return chatUserDao.queryChatUserBySyncUserId(user.getUserId());
    }

    @Override
    public ChatUser getLoginChatUserByToken(String s) {
        User user = redisCache.getObject(s + "_USER",User.class);
        if (user == null) {
            return null;
        }
        return chatUserDao.queryChatUserBySyncUserId(user.getUserId());
    }
}
