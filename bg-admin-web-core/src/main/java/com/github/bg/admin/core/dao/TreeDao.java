package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.Tree;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author linzf
 * @since 2019-07-05
 * 类描述：菜单的dao
 */
public interface TreeDao extends Mapper<Tree> {

    /**
     * 功能描述：根据用户登录的ID来获取权限数据的集合
     * @param userId 用户ID
     * @return 返回数据集合
     */
    List<Tree> getLoginUserPowerPath(@Param("userId") String userId);

    /**
     * 功能描述：根据用户登录的ID来获取菜单节点的数据
     * @param userId 用户ID
     * @return 返回数据集合
     */
    List<Tree> getLoginUserTree(@Param("userId") String userId);
    /**
     * 功能描述：根据菜单流水ID来更新菜单数据
     * @param treeId 菜单流水ID
     * @param treeCode 菜单编码
     * @param treeName 菜单名称
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    int updateTree(@Param("treeId") Integer treeId,
                   @Param("treeCode") String treeCode,
                   @Param("treeName") String treeName,
                   @Param("powerPath")String powerPath);

    /**
     * 功能描述：统计当前菜单节点底下是否还有子节点
     * @param treeId 菜单流水ID
     * @return 返回统计结果
     */
    int countTreeChildren(@Param("treeId") Integer treeId);

    /**
     * 功能描述：冻结/解冻按钮
     * @param treeId 节点ID
     * @param treeState 节点状态
     * @return 返回操作结果
     */
    int operateButton(@Param("treeId") Integer treeId,@Param("treeState") String treeState);

    /**
     * 功能描述：更新节点的路径
     * @param fullPath 节点路径
     * @param treeId  节点ID
     * @return 返回更新节点路径结果
     */
    int updateFullPath(@Param("fullPath") String fullPath,@Param("treeId") Integer treeId);

    /**
     * 功能描述：更新按钮节点
     * @param treeCode 节点编码
     * @param treeName 节点名称
     * @param treeId 节点ID
     * @param powerPath 权限集合
     * @return 返回更新结果
     */
    int updateButton(@Param("treeCode") String treeCode,
                     @Param("treeName") String treeName,
                     @Param("treeId") Integer treeId,
                     @Param("powerPath") String powerPath);

    /**
     * 功能描述：验证菜单节点或者按钮节点的编码是否已经存在了
     * @param treeCode 节点编码
     * @param treeId 节点ID
     * @return 返回验证结果
     */
    int checkTreeCode(@Param("treeCode") String treeCode,@Param("treeId") Integer treeId);

    /**
     * 功能描述：获取菜单的按钮的列表
     * @param search 模糊查询treeName和treeCode的数据
     * @param parentTreeId 父节点ID
     * @return 返回该父节点底下的当级的按钮的数据
     */
    List<Tree> queryTreeButtonList(@Param("search") String search, @Param("parentTreeId") Integer parentTreeId);

    /**
     * 功能描述：根据角色ID来查询该角色底下的所有的关联的菜单的数据
     *
     * @param roleId 角色ID
     * @return 返回查询结果
     */
    List<Tree> queryTreeByRoleId(@Param("roleId") String roleId);

}