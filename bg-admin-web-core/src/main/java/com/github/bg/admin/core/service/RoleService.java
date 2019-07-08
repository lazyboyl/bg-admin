package com.github.bg.admin.core.service;


import com.github.bg.admin.core.constant.RoleStaticConstant;
import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.constant.TreeStaticConstant;
import com.github.bg.admin.core.dao.RoleDao;
import com.github.bg.admin.core.dao.RoleTreeDao;
import com.github.bg.admin.core.dao.TreeDao;
import com.github.bg.admin.core.dto.TreeDto;
import com.github.bg.admin.core.entity.*;
import com.github.bg.admin.core.mapper.TreeMapper;
import com.github.bg.admin.core.util.JsonUtils;
import com.github.bg.admin.core.util.PageUtil;
import com.github.bg.admin.core.util.TreeInstall;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019/4/30
 * 类描述：角色管理的service
 */
@Service
@Transactional(rollbackFor = {IllegalArgumentException.class})
public class RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private TreeDao treeDao;
    @Autowired
    private RoleTreeDao roleTreeDao;
    @Autowired
    private TreeMapper treeMapper;




    /**
     * 功能描述：加载菜单节点的数据
     * @return 返回加载结果
     */
    public ReturnInfo loadTree(){
        Tree tree = new Tree();
        tree.setTreeState(TreeStaticConstant.TREE_STATE_NORMAL);
        return new ReturnInfo(SystemStaticConst.SUCCESS, "加载菜单数据成功", TreeInstall.installTree(treeMapper.treesToTreeDTO(treeDao.select(tree))));
    }

    /**
     * 功能描述：删除角色数据
     *
     * @param roleId 角色流水ID
     * @return 返回删除结果
     */
    public ReturnInfo deleteRole(String roleId) {
        try {
            if (roleDao.deleteByPrimaryKey(roleId) > 0) {
                // 删除角色的关联数据
                roleTreeDao.deleteRoleTreeByRoleId(roleId);
                return new ReturnInfo(SystemStaticConst.SUCCESS, "删除角色数据成功");
            }
            return new ReturnInfo(SystemStaticConst.FAIL, "删除角色数据失败！失败原因：该角色数据不存在");
        } catch (Exception e) {
            return new ReturnInfo(SystemStaticConst.FAIL, "删除角色数据失败！失败原因：" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新角色数据
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @param roleTrees 角色菜单关联的数据
     * @return 返回操作结果
     */
    public ReturnInfo updateRole(String roleId, String roleName, String roleCode,String roleTrees) {
        try {
            if (roleDao.checkRoleCodeAndName(roleId, roleName, "") > 0) {
                return new ReturnInfo(SystemStaticConst.FAIL, "角色名字已经存在，请修改以后再提交！");
            }
            if (roleDao.checkRoleCodeAndName(roleId, "", roleCode) > 0) {
                return new ReturnInfo(SystemStaticConst.FAIL, "角色编码已经存在，请修改以后再提交！");
            }
            if (roleDao.updateRole(roleId, roleName, roleCode) > 0) {
                if(roleTrees!=null){
                    // 删除关联表的数据
                    roleTreeDao.deleteRoleTreeByRoleId(roleId);
                    // 重新插入新的关联数据
                    saveRoleAssociateTree(JsonUtils.jsonToList(roleTrees, TreeDto.class),new Role(roleId));
                }
                return new ReturnInfo(SystemStaticConst.SUCCESS, "更新角色数据成功");
            }
            return new ReturnInfo(SystemStaticConst.FAIL, "更新角色数据失败！失败原因：查无此角色数据");
        } catch (Exception e) {
            return new ReturnInfo(SystemStaticConst.FAIL, "更新角色数据失败！失败原因：" + e.getMessage());
        }
    }

    /**
     * 功能描述：获取角色信息
     *
     * @param roleId 角色流水ID
     * @return 返回操作结果
     */
    public ReturnInfo getRoleByRoleId(String roleId) {
        try {
            Role role = roleDao.selectByPrimaryKey(roleId);
            if (role != null) {
                List<Tree> treeList = treeDao.queryTreeByRoleId(role.getRoleId());
                List<Tree> allTree = treeDao.selectAll();
                Map<String,Object> treeMap = new HashMap<>(3);
                for(Tree tree:treeList){
                    treeMap.put(tree.getTreeId().toString(),tree);
                }
                Map<String,Object>  result = JsonUtils.objToMap(role);
                result.put(RoleStaticConstant.ROLE_TREE_LIST_NAME,TreeInstall.installCheckTree(treeMapper.treesToTreeDTO(allTree),treeMap));
                return new ReturnInfo(SystemStaticConst.SUCCESS, "获取角色数据成功", result);
            }
        } catch (Exception e) {
            return new ReturnInfo(SystemStaticConst.FAIL, "获取角色数据失败！失败原因：" + e.getMessage());
        }
        return new ReturnInfo(SystemStaticConst.FAIL, "获取角色数据失败！失败原因：查无此角色数据");
    }

    /**
     * 功能描述：实现增加角色数据
     *
     * @param role 角色实体数据
     * @return 返回操作结果
     */
    public ReturnInfo addRole(Role role) {
        role.setCrtDate(new Date());
        try {
            if (roleDao.checkRoleCodeAndName("", role.getRoleName(), "") > 0) {
                return new ReturnInfo(SystemStaticConst.FAIL, "角色名字已经存在，请修改以后再提交！");
            }
            if (roleDao.checkRoleCodeAndName("", "", role.getRoleCode()) > 0) {
                return new ReturnInfo(SystemStaticConst.FAIL, "角色编码已经存在，请修改以后再提交！");
            }
            roleDao.insert(role);
            saveRoleAssociateTree(JsonUtils.jsonToList(role.getRoleTrees(),TreeDto.class),role);
        } catch (Exception e) {
            return new ReturnInfo(SystemStaticConst.FAIL, "增加角色失败，失败原因：" + e.getMessage());
        }
        return new ReturnInfo(SystemStaticConst.SUCCESS, "增加加角色成功", role);
    }

    /**
     * 功能描述：验证角色编码和角色名字是否重复
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回处理结果
     */
    public ReturnInfo checkRoleCodeAndName(String roleId, String roleName, String roleCode) {
        Map<String, Object> result = new HashMap<>(1);
        try {
            // 查询的结果大于0表示数据库已经存在该数据了，反之则不存在
            if (roleDao.checkRoleCodeAndName(roleId, roleName, roleCode) > 0) {
                result.put("success", "unPass");
            } else {
                result.put("success", "pass");
            }
        } catch (Exception e) {
            return new ReturnInfo(SystemStaticConst.FAIL, "验证请求处理失败，失败原因：" + e.getMessage());
        }
        return new ReturnInfo(SystemStaticConst.SUCCESS, "验证请求处理成功", result);
    }

    /**
     * 功能描述：获取角色列表数据
     *
     * @param search       模糊匹配角色的roleName和roleCode
     * @param pageSize     每页显示的记录的条数
     * @param current      当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    public ReturnInfo queryRoleList(String search, int pageSize, int current, String orderKey, String orderByValue) {
        PageHelper.startPage(current, (pageSize > 0 && pageSize <= 500) ? pageSize : 20, (orderKey != null && !"".equals(orderKey)) ? ((orderByValue != null && !"".equals(orderByValue)) ? (orderKey + " " + orderByValue) : orderKey) : "");
        HashMap<String, Object> res = PageUtil.getResult(roleDao.queryRoleList(search));
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取角色列表数据成功！", new Page(pageSize, current, (long) res.get("total"), (List) res.get("rows")));
    }

    /**
     * 功能描述：保存角色和菜单的关联关系的数据
     * @param treeDtoList 菜单节点数据
     * @param entity 角色实体
     * @return 返回是否有子节点被选中
     */
    private boolean saveRoleAssociateTree(List<TreeDto> treeDtoList,Role entity){
        boolean hasChildrenChecked = false;
        for(TreeDto treeDto:treeDtoList){
            if(treeDto.getChildren()!=null&&treeDto.getChildren().size()>0){
                if(saveRoleAssociateTree(treeDto.getChildren(),entity)||treeDto.isChecked()){
                    roleTreeDao.insert(new RoleTree(entity.getRoleId(),treeDto.getTreeId()));
                    hasChildrenChecked = true;
                }
            }else{
                if(treeDto.isChecked()){
                    roleTreeDao.insert(new RoleTree(entity.getRoleId(),treeDto.getTreeId()));
                    hasChildrenChecked = true;
                }
            }
        }
        return hasChildrenChecked;
    }

}
