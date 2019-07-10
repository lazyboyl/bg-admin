package com.github.bg.admin.core.controller;


import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.service.MessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linzf
 * @since 2019/6/19
 * 类描述：消息的controller
 */
@RestController
@RequestMapping("/msg")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 功能描述：实现消息的阅读
     * @param targetMessageId 消息关联流水ID
     * @return 返回更新结果
     */
    @ApiOperation(value = "实现消息的阅读")
    @PostMapping("readMsg")
    public ReturnInfo readMsg(@RequestParam(name = "targetMessageId")String targetMessageId){
        return messageService.readMsg(targetMessageId);
    }

    /**
     * 功能描述：获取当前登录的用户的未读的消息
     * @return 返回消息数据
     */
    @ApiOperation(value = "获取当前登录的用户的未读的消息")
    @PostMapping("queryUserMsg")
    public ReturnInfo queryUserMsg(){
        return messageService.queryUserMsg();
    }

    /**
     * 功能描述：实现消息的发布
     * @param title 消息标题
     * @param content 消息内容
     * @param targetUsers 接收用户ID
     * @return 返回发布结果
     */
    @ApiOperation(value = "消息的发布")
    @PostMapping("publishNews")
    public ReturnInfo publishNews(@RequestParam(name = "title")String title,
                                  @RequestParam(name = "content")String content,
                                  @RequestParam(name = "targetUsers")String [] targetUsers){
        return messageService.publishNews(title,content,targetUsers);
    }

    /**
     * 功能描述：获取消息列表
     *
     * @param search   模糊匹配消息的title和content
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取消息列表")
    @PostMapping("queryMessageList")
    public ReturnInfo queryMessageList(@RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "pageSize") int pageSize,
                                    @RequestParam(name = "current") int current,
                                    @RequestParam(name = "orderKey", required = false) String orderKey,
                                    @RequestParam(name = "orderByValue", required = false) String orderByValue) {
        return messageService.queryMessageList(search, pageSize, current, orderKey, orderByValue);
    }

}
