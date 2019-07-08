package com.github.bg.admin.core.controller;

import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.entity.Role;
import com.github.bg.admin.core.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linzf
 * @since 2019/4/30
 * 类描述：角色管理的controller
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 功能描述：加载菜单节点的数据
     * @return
     */
    @ApiOperation(value = "加载菜单节点的数据")
    @PostMapping("loadTree")
    public ReturnInfo loadTree(){
        return roleService.loadTree();
    }

    /**
     * 功能描述：删除角色数据
     * @param roleId 角色流水ID
     * @return 返回删除结果
     */
    @ApiOperation(value = "删除角色数据")
    @PostMapping("deleteRole")
    public ReturnInfo deleteRole(@RequestParam(name = "roleId")String roleId){
        return roleService.deleteRole(roleId);
    }

    /**
     * 功能描述：更新角色数据
     * @param roleId 角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @param roleTrees 角色关联的菜单数据
     * @return 返回操作结果
     */
    @ApiOperation(value = "更新角色数据")
    @PostMapping("updateRole")
    public ReturnInfo updateRole(@RequestParam(name = "roleId") String roleId, @RequestParam(name = "roleName") String roleName, @RequestParam(name = "roleCode") String roleCode,@RequestParam(name = "roleTrees" ,required = false) String roleTrees){
        return roleService.updateRole(roleId, roleName, roleCode,roleTrees);
    }

    /**
     * 功能描述：获取角色信息
     * @param roleId 角色流水ID
     * @return 返回操作结果
     */
    @ApiOperation(value = "获取角色信息")
    @PostMapping("getRoleByRoleId")
    public ReturnInfo getRoleByRoleId(@RequestParam(name = "roleId") String roleId){
        return roleService.getRoleByRoleId(roleId);
    }

    /**
     * 功能描述：实现增加角色
     * @param role 角色实体数据
     * @return 返回操作结果
     */
    @ApiOperation(value = "增加角色")
    @PostMapping("addRole")
    public ReturnInfo addRole(Role role){
        return roleService.addRole(role);
    }

    /**
     * 功能描述：验证角色编码和角色名字是否重复
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回处理结果
     */
    @ApiOperation(value = "验证角色编码和角色名字是否重复")
    @PostMapping("checkRoleCodeAndName")
    public ReturnInfo checkRoleCodeAndName(@RequestParam(name = "roleId", required = false) String roleId, @RequestParam(name = "roleName", required = false) String roleName, @RequestParam(name = "roleCode", required = false) String roleCode) {
        return roleService.checkRoleCodeAndName(roleId, roleName, roleCode);
    }

    /**
     * 功能描述：获取角色列表数据
     *
     * @param search   模糊匹配角色的roleName和roleCode
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey 排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    @ApiOperation(value = "获取角色列表数据")
    @PostMapping("queryRoleList")
    public ReturnInfo queryRoleList(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "current") int current, @RequestParam(name = "orderKey",required = false) String orderKey, @RequestParam(name = "orderByValue",required = false) String orderByValue) {
        return roleService.queryRoleList(search, pageSize, current,orderKey,orderByValue);
    }

}
