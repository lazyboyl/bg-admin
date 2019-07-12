package com.github.bg.admin.core.controller;

import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.service.BehaviorLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linzf
 * @since 2019/5/20
 * 类描述：行为日志的controller
 */
@RestController
@RequestMapping("/behaviorLog")
public class BehaviorLogController {

    @Autowired
    private BehaviorLogService behaviorLogService;

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
    @ApiOperation(value = "获取行为日志列表")
    @PostMapping("queryBehaviorLogList")
    private ReturnInfo queryBehaviorLogList(@RequestParam(name = "search", required = false) String search,
                                            @RequestParam(name = "pageSize") int pageSize,
                                            @RequestParam(name = "current") int current,
                                            @RequestParam(name = "orderKey",required = false) String orderKey,
                                            @RequestParam(name = "orderByValue",required = false) String orderByValue){
        return behaviorLogService.queryBehaviorLogList(search, pageSize, current, orderKey, orderByValue);
    }

}
