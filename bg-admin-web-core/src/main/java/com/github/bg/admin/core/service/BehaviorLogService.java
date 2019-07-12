package com.github.bg.admin.core.service;


import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.BehaviorLogDao;
import com.github.bg.admin.core.entity.Page;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.util.PageUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author linzf
 * @since 2019/5/20
 * 类描述：行为日志的service
 */
@Service
public class BehaviorLogService {

    @Autowired
    private BehaviorLogDao behaviorLogDao;

    /**
     * 功能描述：获取行为日志列表
     *
     * @param search   模糊匹配日志的用户名字和响应方法
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    public ReturnInfo queryBehaviorLogList(String search, int pageSize, int current, String orderKey, String orderByValue) {
        PageHelper.startPage(current, (pageSize > 0 && pageSize <= 500) ? pageSize : 20,(orderKey != null && !"".equals(orderKey)) ? ((orderByValue != null && !"".equals(orderByValue)) ? (orderKey + " " + orderByValue) : orderKey) : "");
        HashMap<String, Object> res = PageUtil.getResult(behaviorLogDao.queryBehaviorLogList(search));
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取日志列表数据成功！", new Page(pageSize, current, (long) res.get("total"), (List) res.get("rows")));
    }

}
