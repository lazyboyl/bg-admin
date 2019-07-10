package com.github.bg.admin.core.service;


import com.github.bg.admin.core.config.socketio.SocketIoService;
import com.github.bg.admin.core.constant.MessageConstant;
import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.MessageDao;
import com.github.bg.admin.core.dao.TargetMessageDao;
import com.github.bg.admin.core.entity.*;
import com.github.bg.admin.core.util.PageUtil;
import com.github.bg.admin.core.util.RedisCache;
import com.github.bg.admin.core.util.UserInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author linzf
 * @since 2019/6/19
 * 类描述：消息的service
 */
@Service
@Transactional(rollbackFor = {IllegalArgumentException.class})
public class MessageService {

    /**
     * 消息的dao
     */
    @Autowired
    private MessageDao messageDao;

    /**
     * 接收的消息的dao
     */
    @Autowired
    private TargetMessageDao targetMessageDao;

    /**
     * socket 推送的bean
     */
    @Autowired
    private SocketIoService socketIoService;

    /**
     * redis工具类的bean
     */
    @Autowired
    private RedisCache redisCache;

    /**
     * 功能描述：实现消息的阅读
     *
     * @param targetMessageId 消息关联流水ID
     * @return 返回更新结果
     */
    public ReturnInfo readMsg(String targetMessageId) {
        if (targetMessageDao.readMsg(MessageConstant.READE_STATE_READE, new Date(), targetMessageId) == 0) {
            return new ReturnInfo(SystemStaticConst.FAIL, "消息阅读失败！");
        } else {
            return new ReturnInfo(SystemStaticConst.SUCCESS, "消息阅读成功！");
        }
    }

    /**
     * 功能描述：获取当前登录的用户的未读的消息
     *
     * @return 返回消息数据
     */
    public ReturnInfo queryUserMsg() {
        User user = UserInfo.getLoginUser(redisCache);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "用户未登录！");
        }
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取用户消息成功！", messageDao.queryUserMsg(user.getUserId()));
    }

    /**
     * 功能描述：实现消息的发布
     *
     * @param title       消息标题
     * @param content     消息内容
     * @param targetUsers 接收用户ID
     * @return 返回发布结果
     */
    public ReturnInfo publishNews(String title, String content, String[] targetUsers) {
        User user = UserInfo.getLoginUser(redisCache);
        if (user == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "用户未登录！");
        }
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setCrtDate(new Date());
        message.setCrtUserId(user.getUserId());
        message.setCrtUserName(user.getNickName());
        message.setType(MessageConstant.MESSAGE_TYPE_SYSTEM);
        messageDao.insert(message);
        String[] targetUserSplit;
        TargetMessage targetMessage;
        // 插入接收者的数据
        for (String targetUser : targetUsers) {
            targetUserSplit = targetUser.split("\\|");
            targetMessage = new TargetMessage();
            targetMessage.setMessageId(message.getMessageId());
            targetMessage.setTargetName(user.getNickName());
            targetMessage.setRemoveState(MessageConstant.REMOVE_STATE_NORMAL);
            targetMessage.setUserId(targetUserSplit[1]);
            targetMessage.setState(MessageConstant.READE_STATE_NOT_READE);
            targetMessage.setSendTime(new Date());
            targetMessageDao.insert(targetMessage);
            socketIoService.pushMessage(targetUserSplit[0], content);
        }
        return new ReturnInfo(SystemStaticConst.SUCCESS, "消息发布成功");
    }


    /**
     * 功能描述：获取消息列表
     *
     * @param search       模糊匹配消息的title和content
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    public ReturnInfo queryMessageList(String search, int pageSize, int current, String orderKey, String orderByValue) {
        PageHelper.startPage(current, (pageSize > 0 && pageSize <= 500) ? pageSize : 20, (orderKey != null && !"".equals(orderKey)) ? ((orderByValue != null && !"".equals(orderByValue)) ? (orderKey + " " + orderByValue) : orderKey) : "");
        HashMap<String, Object> res = PageUtil.getResult(messageDao.queryMessageList(search));
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取消息列表数据成功！", new Page(pageSize, current, (long) res.get("total"), (List) res.get("rows")));
    }


}
