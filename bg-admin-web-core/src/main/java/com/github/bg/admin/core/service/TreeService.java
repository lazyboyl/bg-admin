package com.github.bg.admin.core.service;


import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.constant.TreeStaticConstant;
import com.github.bg.admin.core.dao.RoleTreeDao;
import com.github.bg.admin.core.dao.TreeDao;
import com.github.bg.admin.core.entity.Page;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.entity.Tree;
import com.github.bg.admin.core.mapper.TreeMapper;
import com.github.bg.admin.core.util.PageUtil;
import com.github.bg.admin.core.util.TreeInstall;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019-05-07
 * 类描述：菜单的service实现类
 */
@Service
@Transactional(rollbackFor = {IllegalArgumentException.class})
public class TreeService {

    @Autowired
    private TreeDao treeDao;
    @Autowired
    private RoleTreeDao roleTreeDao;
    @Autowired
    private TreeMapper treeMapper;

    /**
     * 功能描述：根据菜单流水ID来更新菜单数据
     * @param treeId 菜单流水ID
     * @param treeCode 菜单编码
     * @param treeName 菜单名称
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    public ReturnInfo updateTree(Integer treeId, String treeCode, String treeName, String powerPath){
        if(treeDao.updateTree(treeId, treeCode, treeName,powerPath)>0){
            return new ReturnInfo(SystemStaticConst.SUCCESS,"更新菜单节点成功");
        }else{
            return new ReturnInfo(SystemStaticConst.FAIL,"更新菜单节点失败");
        }
    }

    /**
     * 功能描述：根据菜单ID来获取菜单数据
     * @param treeId 菜单ID
     * @return 返回操作结果
     */
    public ReturnInfo getTreeByTreeId(Integer treeId){
        Tree tree = treeDao.selectByPrimaryKey(treeId);
        if(tree==null){
            return new ReturnInfo(SystemStaticConst.FAIL,"查无此菜单数据");
        }else{
            return new ReturnInfo(SystemStaticConst.SUCCESS,"获取菜单节点成功",tree);
        }
    }

    /**
     * 功能描述：删除菜单节点
     * @param treeId 菜单节点ID
     * @return 返回删除结果
     */
    public ReturnInfo deleteTree(Integer treeId){
        if(treeDao.countTreeChildren(treeId)>0){
            return new ReturnInfo(SystemStaticConst.FAIL, "当前菜单节点底下还有其他节点，请先删除底下的节点以后再来删除当前节点");
        }
        if(treeDao.deleteByPrimaryKey(treeId)>0){
            roleTreeDao.deleteRoleTreeByTreeId(treeId);
            return new ReturnInfo(SystemStaticConst.SUCCESS,"删除菜单节点成功");
        }else{
            return new ReturnInfo(SystemStaticConst.FAIL,"删除菜单节点失败");
        }
    }

    /**
     * 功能描述：冻结/解冻按钮
     * @param treeId 节点ID
     * @param treeState 节点状态
     * @return 返回操作结果
     */
    public ReturnInfo operateButton(Integer treeId,String treeState){
        if(treeDao.operateButton(treeId, treeState)>0){
            return new ReturnInfo(SystemStaticConst.SUCCESS, "操作成功");
        }else{
            return new ReturnInfo(SystemStaticConst.FAIL, "操作失败");
        }
    }

    /**
     * 功能描述：增加按钮
     * @param treeCode 按钮编码
     * @param treeName 按钮名称
     * @param powerPath 权限集合
     * @param parentTreeId 父菜单节点ID
     * @param treeType 节点类型
     * @return 返回增加结果
     */
    public ReturnInfo addButton(String treeCode,String treeName,String powerPath,Integer parentTreeId,String treeType){
        StringBuilder fullPath = new StringBuilder();
        Tree tree = new Tree();
        tree.setTreeCode(treeCode);
        tree.setTreeName(treeName);
        tree.setTreeType(treeType);
        tree.setPowerPath(powerPath);
        tree.setTreeState(TreeStaticConstant.TREE_STATE_NORMAL);
        tree.setParentTreeId(parentTreeId);
        tree.setCrtDate(new Date());
        if(parentTreeId.intValue()==0){
            tree.setParentTreeName("");
        }else{
            Tree parentTree = treeDao.selectByPrimaryKey(parentTreeId);
            if(parentTree==null){
                return new ReturnInfo(SystemStaticConst.FAIL, "增加按钮失败，不存在当前父菜单");
            }
            tree.setParentTreeName(parentTree.getTreeName());
            fullPath.append(parentTree.getFullPath());
        }
        if(treeDao.insert(tree)>0){
            fullPath.append(".").append(tree.getTreeId());
            treeDao.updateFullPath(fullPath.toString(),tree.getTreeId());
            return new ReturnInfo(SystemStaticConst.SUCCESS, "增加按钮成功",tree);
        }else{
            return new ReturnInfo(SystemStaticConst.FAIL, "增加按钮失败");
        }
    }

    /**
     * 功能描述：验证菜单节点是否已经增加过
     * @param treeId 菜单流水ID
     * @param treeCode 菜单编码
     * @return 返回验证结果
     */
    public ReturnInfo checkTreeCode(Integer treeId,String treeCode){
        Map<String,Object> result = new HashMap<>(1);
        if(treeDao.checkTreeCode(treeCode,treeId)>0){
            result.put(TreeStaticConstant.CHECK_TREE_TIP,TreeStaticConstant.CHECK_TREE_UN_PASS);
            return new ReturnInfo(SystemStaticConst.SUCCESS, "该编码已经存在了，请修改以后再提交！",result);
        }else{
            result.put(TreeStaticConstant.CHECK_TREE_TIP,TreeStaticConstant.CHECK_TREE_PASS);
            return new ReturnInfo(SystemStaticConst.SUCCESS, "验证通过！",result);
        }
    }

    /**
     * 功能描述：更新按钮节点
     * @param treeId 按钮节点ID
     * @param treeName 按钮名字
     * @param treeCode 按钮编码
     * @param powerPath 权限集合
     * @return 返回更新操作结果
     */
    public ReturnInfo updateButton(Integer treeId,String treeName,String treeCode,String powerPath){
        if(treeDao.checkTreeCode(treeCode,treeId)>0){
            return new ReturnInfo(SystemStaticConst.FAIL, "该编码已经存在了，请修改以后再提交！");
        }
        if(treeDao.updateButton(treeCode, treeName, treeId,powerPath)>0){
            return new ReturnInfo(SystemStaticConst.SUCCESS, "更新按钮节点成功！");
        }else{
            return new ReturnInfo(SystemStaticConst.FAIL, "更新按钮节点失败！");
        }
    }

    /**
     * 功能描述：删除按钮节点
     * @param treeId 按钮的ID
     * @return 返回删除结果
     */
    public ReturnInfo deleteButton(Integer treeId){
        if(treeDao.deleteByPrimaryKey(treeId)>0){
            roleTreeDao.deleteRoleTreeByTreeId(treeId);
            return new ReturnInfo(SystemStaticConst.SUCCESS,"删除按钮节点成功");
        }else{
            return new ReturnInfo(SystemStaticConst.FAIL,"删除按钮节点失败");
        }
    }

    /**
     * 功能描述：获取菜单的按钮的列表
     *
     * @param search   模糊匹配菜单的treeName，treeCode 允许为空
     * @param pageSize 每页显示的记录的条数
     * @param parentTreeId 父节点id
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    public ReturnInfo queryTreeButtonList(String search,Integer parentTreeId, int pageSize, int current, String orderKey, String orderByValue){
        PageHelper.startPage(current, (pageSize > 0 && pageSize <= 500) ? pageSize : 20,(orderKey != null && !"".equals(orderKey)) ? ((orderByValue != null && !"".equals(orderByValue)) ? (orderKey + " " + orderByValue) : orderKey) : "");
        HashMap<String, Object> res = PageUtil.getResult(treeDao.queryTreeButtonList(search,parentTreeId));
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取按钮列表数据成功！", new Page(pageSize, current, (long) res.get("total"), (List) res.get("rows")));
    }

    /**
     * 功能描述：获取菜单树目录
     * @return 返回菜单目录数据
     */
    @PostMapping("getTreeList")
    public ReturnInfo getTreeList(){
        Tree tree = new Tree();
        tree.setTreeState(TreeStaticConstant.TREE_STATE_NORMAL);
        tree.setTreeType(TreeStaticConstant.TREE_TYPE_MENU);
        return new ReturnInfo(SystemStaticConst.SUCCESS, "加载菜单数据成功", TreeInstall.installTree(treeMapper.treesToTreeDTO(treeDao.select(tree))));
    }

}
