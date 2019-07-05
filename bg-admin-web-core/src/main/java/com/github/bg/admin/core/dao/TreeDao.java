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
     * 功能描述：根据用户登录的ID来获取菜单节点的数据
     * @param userId 用户ID
     * @return 返回数据集合
     */
    List<Tree> getLoginUserTree(@Param("userId") String userId);

    /**
     * 功能描述：根据用户登录的ID来获取权限数据的集合
     * @param userId 用户ID
     * @return 返回数据集合
     */
    List<Tree> getLoginUserPowerPath(@Param("userId") String userId);

}