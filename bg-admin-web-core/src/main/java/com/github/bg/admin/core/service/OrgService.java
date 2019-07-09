package com.github.bg.admin.core.service;


import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.OrgDao;
import com.github.bg.admin.core.dao.UserOrgDao;
import com.github.bg.admin.core.entity.Org;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.mapper.OrgMapper;
import com.github.bg.admin.core.util.OrgInstall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author linzf
 * @since 2019-05-09
 * 类描述：组织架构的service
 */
@Service
@Transactional(rollbackFor = {IllegalArgumentException.class})
public class OrgService {

    @Autowired
    private OrgDao orgDao;
    @Autowired
    private UserOrgDao userOrgDao;
    @Autowired
    private OrgMapper orgMapper;

    /**
     * 功能描述：获取组织架构信息
     * @param orgId 组织架构流水ID
     * @return 返回获取结果
     */
    public ReturnInfo getOrgByOrgId(Integer orgId) {
        Org org = orgDao.selectByPrimaryKey(orgId);
        if (org == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "查无此组织架构数据，请重新刷新页面！");
        }
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取组织架构数据成功", org);
    }

    /**
     * 功能描述：删除组织架构
     *
     * @param orgId 组织架构流水ID
     * @return 返回删除结果
     */
    public ReturnInfo deleteOrg(Integer orgId) {
        Org org = orgDao.selectByPrimaryKey(orgId);
        if (org == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，查无此组织架构数据！");
        }
        if (orgDao.countOrgChildren(orgId) > 0) {
            return new ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，当前组织架构底下还有子组织，请先删除子组织！");
        }
        if (userOrgDao.countUserOrgByOrgId(orgId) > 0) {
            return new ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，当前组织架构底下还有用户，请先删除该组织架构底下的用户！");
        }
        if (orgDao.deleteByPrimaryKey(orgId) > 0) {
            userOrgDao.deleteUserOrgByOrgId(orgId);
            return new ReturnInfo(SystemStaticConst.SUCCESS, "删除组织架构成功！");
        } else {
            return new ReturnInfo(SystemStaticConst.FAIL, "删除组织架构数据失败，查无此组织架构数据！");
        }
    }

    /**
     * 功能描述：更新组织架构的数据
     *
     * @param orgId   组织架构流水ID
     * @param orgName 组织架构名字
     * @param orgCode 组织架构编码
     * @return 返回更新结果
     */
    public ReturnInfo updateOrg(Integer orgId, String orgName, String orgCode) {
        if (orgDao.checkOrgNameAndCode(orgId, orgName, "") > 0) {
            return new ReturnInfo(SystemStaticConst.FAIL, "当前组织架构名称已经存在，请重新修改以后再提交！");
        }
        if (orgDao.checkOrgNameAndCode(orgId, "", orgCode) > 0) {
            return new ReturnInfo(SystemStaticConst.FAIL, "当前组织架构编码已经存在，请重新修改以后再提交！");
        }
        Org org = orgDao.selectByPrimaryKey(orgId);
        if (org == null) {
            return new ReturnInfo(SystemStaticConst.FAIL, "查无此组织架构数据，请重新刷新页面！");
        }
        org.setOrgName(orgName);
        org.setOrgCode(orgCode);
        if (orgDao.updateByPrimaryKey(org) > 0) {
            return new ReturnInfo(SystemStaticConst.SUCCESS, "更新组织架构数据成功");
        } else {
            return new ReturnInfo(SystemStaticConst.FAIL, "更新组织架构失败");
        }
    }

    /**
     * 功能描述：增加组织架构
     *
     * @param orgName     组织架构名称
     * @param orgCode     组织架构编码
     * @param parentOrgId 父节点ID
     * @return
     */
    public ReturnInfo addOrg(String orgName, String orgCode, Integer parentOrgId) {
        StringBuilder fullPath = new StringBuilder();
        Org org = new Org();
        if (parentOrgId.intValue() == 0) {
            org.setParentOrgName("");
        } else {
            Org parent = orgDao.selectByPrimaryKey(parentOrgId);
            if (parent == null) {
                return new ReturnInfo(SystemStaticConst.FAIL, "查无此父组织架构数据，请重新选择父组织架构！");
            }
            org.setParentOrgName(parent.getOrgName());
            fullPath.append(parent.getFullPath());
        }
        org.setOrgName(orgName);
        org.setOrgCode(orgCode);
        org.setCrtDate(new Date());
        org.setParentOrgId(parentOrgId);
        if (orgDao.insert(org) > 0) {
            fullPath.append(".").append(org.getOrgId());
            orgDao.updateFullPath(org.getOrgId(), fullPath.toString());
            return new ReturnInfo(SystemStaticConst.SUCCESS, "增加组织架构成功", org);
        } else {
            return new ReturnInfo(SystemStaticConst.FAIL, "增加组织架构失败");
        }

    }

    /**
     * 功能描述：验证组织架构名字和编码是否存在
     *
     * @param orgId   组织架构ID
     * @param orgName 组织架构名称
     * @param orgCode 组织架构编码
     * @return 返回验证结果
     */
    public ReturnInfo checkOrgNameAndCode(Integer orgId, String orgName, String orgCode) {
        Map<String, Object> result = new HashMap<>(1);
        try {
            if (orgDao.checkOrgNameAndCode(orgId, orgName, orgCode) > 0) {
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
     * 功能描述：获取组织架构树目录
     *
     * @return 返回获取的结果
     */
    public ReturnInfo getOrgTree() {
        return new ReturnInfo(SystemStaticConst.SUCCESS, "加载组织架构数据成功", OrgInstall.installOrg(orgMapper.orgsToOrgDto(orgDao.selectAll())));
    }

    /**
     * 功能描述：获取组织架构的Cascader的数据
     * @return 返回获取结果
     */
    public ReturnInfo getOrgCascader(){
        return new ReturnInfo(SystemStaticConst.SUCCESS, "加载级联菜单的组织架构数据成功", OrgInstall.installOrgCascader(orgDao.selectAll()));
    }

}
