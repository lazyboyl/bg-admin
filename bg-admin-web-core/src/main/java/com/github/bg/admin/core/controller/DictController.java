package com.github.bg.admin.core.controller;

import com.github.bg.admin.core.entity.Dict;
import com.github.bg.admin.core.service.DictService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author linzf
 * @since 2019/05/06
 * 类描述：数据字典的controller类
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 功能描述：实现增加字典数据
     *
     * @param dict 包含字典数据的实体
     * @return 返回操作结果
     */
    @ApiOperation(value = "实现增加字典数据")
    @PostMapping("save")
    public void save(Dict dict){
        dictService.save(dict);
    }

}
