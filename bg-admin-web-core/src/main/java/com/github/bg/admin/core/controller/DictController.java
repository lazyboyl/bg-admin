package com.github.bg.admin.core.controller;

import com.github.bg.admin.core.entity.Dict;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.service.DictService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * 功能描述：加载全部的字典数据
     *
     * @return 返回操作结果
     */
    @ApiOperation(value = "加载全部的字典数据")
    @PostMapping("loadAll")
    public ReturnInfo loadAll() {
        return dictService.loadAll();
    }

    /**
     * 功能描述：更新字典数据
     *
     * @param dictCode  字典编码
     * @param dictText  字典文本
     * @param dictValue 字典值
     * @param id        字典流水id
     * @return 返回更新结果
     */
    @ApiOperation(value = "更新字典数据")
    @PostMapping("updateDict")
    public ReturnInfo updateDict(@RequestParam(name = "dictCode") String dictCode, @RequestParam(name = "dictText") String dictText, @RequestParam(name = "dictValue") String dictValue, @RequestParam(name = "id") String id) {
        return dictService.updateDict(dictCode, dictText, dictValue, id);
    }

    /**
     * 功能描述：根据字典流水来获取字典数据
     *
     * @param id 字典流水ID
     * @return 返回操作结果
     */
    @ApiOperation(value = "根据字典流水来获取字典数据")
    @PostMapping("getDict")
    public ReturnInfo getDict(@RequestParam(name = "id") String id) {
        return dictService.getDict(id);
    }

    /**
     * 功能描述：实现删除字典数据
     *
     * @param id 字典流水ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "实现删除字典数据")
    @PostMapping("deleteDict")
    public ReturnInfo deleteDict(@RequestParam(name = "id") String id) {
        return dictService.deleteDict(id);
    }

    /**
     * 功能描述：实现增加字典数据
     *
     * @param dict 包含字典数据的实体
     * @return 返回操作结果
     */
    @ApiOperation(value = "实现增加字典数据")
    @PostMapping("addDict")
    public ReturnInfo addDict(Dict dict) {
        return dictService.addDict(dict);
    }

    /**
     * 功能描述：验证字典的类型和编码是否重复
     *
     * @param id       字典流水ID 允许为空
     * @param dictType 字典类型
     * @param dictCode 字典编码
     * @return 返回验证结果
     */
    @ApiOperation(value = "验证字典的类型和编码是否重复")
    @PostMapping("checkTypeAndCode")
    public ReturnInfo checkTypeAndCode(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "dictType") String dictType, @RequestParam(name = "dictCode") String dictCode) {
        return dictService.checkTypeAndCode(id, dictType, dictCode);
    }

    /**
     * 功能描述：获取数据字典列表
     *
     * @param search       模糊匹配数据字典的dictType、dictText、dictValue、dictCode 允许为空
     * @param dictCode     模糊查询dictCode
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取数据字典列表")
    @PostMapping("queryDictList")
    public ReturnInfo queryDictList(@RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "dictCode", required = false) String dictCode,
                                    @RequestParam(name = "pageSize") int pageSize,
                                    @RequestParam(name = "current") int current,
                                    @RequestParam(name = "orderKey", required = false) String orderKey,
                                    @RequestParam(name = "orderByValue", required = false) String orderByValue) {
        return dictService.queryDictList(search, dictCode, pageSize, current, orderKey, orderByValue);
    }

}
