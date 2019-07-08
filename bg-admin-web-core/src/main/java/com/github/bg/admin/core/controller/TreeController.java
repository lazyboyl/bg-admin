package com.github.bg.admin.core.controller;


import com.github.bg.admin.core.constant.TreeStaticConstant;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.service.TreeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linzf
 * @since 2019/05/07
 * 类描述：菜单管理的controller
 */
@RestController
@RequestMapping("/tree")
public class TreeController {

    @Autowired
    private TreeService treeService;

    /**
     * 功能描述：根据菜单流水ID来更新菜单数据
     *
     * @param treeId   菜单流水ID
     * @param treeCode 菜单编码
     * @param treeName 菜单名称
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    @ApiOperation(value = "根据菜单流水ID来更新菜单数据")
    @PostMapping("updateTree")
    public ReturnInfo updateTree(@RequestParam(name = "treeId") Integer treeId,
                                 @RequestParam(name = "treeCode") String treeCode,
                                 @RequestParam(name = "treeName") String treeName,
                                 @RequestParam(name = "powerPath") String powerPath) {
        return treeService.updateTree(treeId, treeCode, treeName,powerPath);
    }

    /**
     * 功能描述：根据菜单ID来获取菜单数据
     *
     * @param treeId 菜单ID
     * @return 返回操作结果
     */
    @ApiOperation(value = "根据菜单ID来获取菜单数据")
    @PostMapping("getTreeByTreeId")
    public ReturnInfo getTreeByTreeId(@RequestParam(name = "treeId") Integer treeId) {
        return treeService.getTreeByTreeId(treeId);
    }

    /**
     * 功能描述：删除菜单节点
     *
     * @param treeId 菜单节点ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除菜单")
    @PostMapping("deleteTree")
    public ReturnInfo deleteTree(@RequestParam(name = "treeId") Integer treeId) {
        return treeService.deleteTree(treeId);
    }

    /**
     * 功能描述：增加菜单
     *
     * @param treeCode     菜单编码
     * @param treeName     菜单名称
     * @param parentTreeId 父菜单节点ID
     * @param powerPath    权限集合
     * @return 返回增加结果
     */
    @ApiOperation(value = "增加菜单")
    @PostMapping("addTree")
    public ReturnInfo addTree(@RequestParam(name = "treeCode") String treeCode,
                              @RequestParam(name = "treeName") String treeName,
                              @RequestParam(name = "powerPath") String powerPath,
                              @RequestParam(name = "parentTreeId") Integer parentTreeId) {
        return treeService.addButton(treeCode, treeName, powerPath, parentTreeId, TreeStaticConstant.TREE_TYPE_MENU);
    }

    /**
     * 功能描述：冻结/解冻按钮
     *
     * @param treeId    节点ID
     * @param treeState 节点状态
     * @return 返回操作结果
     */
    @ApiOperation(value = "冻结/解冻按钮")
    @PostMapping("operateButton")
    public ReturnInfo operateButton(@RequestParam(name = "treeId") Integer treeId, @RequestParam(name = "treeState") String treeState) {
        return treeService.operateButton(treeId, treeState);
    }

    /**
     * 功能描述：增加按钮
     *
     * @param treeCode     按钮编码
     * @param treeName     按钮名称
     * @param parentTreeId 父菜单节点ID
     * @return 返回增加结果
     */
    @ApiOperation(value = "增加按钮")
    @PostMapping("addButton")
    public ReturnInfo addButton(@RequestParam(name = "treeCode") String treeCode,
                                @RequestParam(name = "treeName") String treeName,
                                @RequestParam(name = "powerPath") String powerPath,
                                @RequestParam(name = "parentTreeId") Integer parentTreeId) {
        return treeService.addButton(treeCode, treeName, powerPath, parentTreeId, TreeStaticConstant.TREE_TYPE_BUTTON);
    }

    /**
     * 功能描述：验证菜单节点是否已经增加过
     *
     * @param treeId   菜单流水ID
     * @param treeCode 菜单编码
     * @return 返回验证结果
     */
    @ApiOperation(value = "验证菜单节点是否已经增加过")
    @PostMapping("checkTreeCode")
    public ReturnInfo checkTreeCode(@RequestParam(name = "treeId", required = false) Integer treeId, @RequestParam(name = "treeCode") String treeCode) {
        return treeService.checkTreeCode(treeId, treeCode);
    }

    /**
     * 功能描述：更新按钮节点
     *
     * @param treeId   按钮节点ID
     * @param treeName 按钮名字
     * @param treeCode 按钮编码
     * @param powerPath 权限集合
     * @return
     */
    @ApiOperation(value = "更新按钮节点")
    @PostMapping("updateButton")
    public ReturnInfo updateButton(@RequestParam(name = "treeId") Integer treeId,
                                   @RequestParam(name = "treeName") String treeName,
                                   @RequestParam(name = "treeCode") String treeCode,
                                   @RequestParam(name = "powerPath") String powerPath) {
        return treeService.updateButton(treeId, treeName, treeCode,powerPath);
    }

    /**
     * 功能描述：删除按钮节点
     *
     * @param treeId 按钮的ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除按钮节点")
    @PostMapping("deleteButton")
    public ReturnInfo deleteButton(Integer treeId) {
        return treeService.deleteButton(treeId);
    }

    /**
     * 功能描述：获取菜单的按钮的列表
     *
     * @param search       模糊匹配菜单的treeName，treeCode 允许为空
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取菜单的按钮的列表")
    @PostMapping("queryTreeButtonList")
    public ReturnInfo queryTreeButtonList(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "parentTreeId") int parentTreeId, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "current") int current, @RequestParam(name = "orderKey", required = false) String orderKey, @RequestParam(name = "orderByValue", required = false) String orderByValue) {
        return treeService.queryTreeButtonList(search, parentTreeId, pageSize, current, orderKey, orderByValue);
    }

    /**
     * 功能描述：获取菜单树目录
     *
     * @return 返回菜单目录数据
     */
    @PostMapping("getTreeList")
    public ReturnInfo getTreeList() {
        return treeService.getTreeList();
    }

}
