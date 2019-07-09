package com.github.bg.admin.core.util;


import com.github.bg.admin.core.dto.OrgCascaderDto;
import com.github.bg.admin.core.dto.OrgDto;
import com.github.bg.admin.core.entity.Org;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019-05-09
 * 类描述：实现组织架构数据的组装
 */
public class OrgInstall {

    /**
     * 功能描述：实现递归组装选中的菜单节点的数据
     *
     * @param orgList 全部菜单节点的集合
     * @param orgMap  当前被选中的数据
     * @return 返回组装好的数据
     */
    public static List<OrgDto> installCheckOrg(List<OrgDto> orgList, Map<String, Object> orgMap) {
        List<OrgDto> trees = new ArrayList<>();
        for (OrgDto orgDto : orgList) {
            if (0 == orgDto.getParentOrgId()) {
                orgDto.setChildren(getCheckChild(orgDto.getOrgId(), orgList, orgMap));
                // 用于防止子节点部分选中，而父节点全部选中
                if (orgDto.getChildren().size() > 0) {
                    for (OrgDto checkDto : orgDto.getChildren()) {
                        if (!checkDto.isChecked()) {
                            orgDto.setChecked(false);
                            break;
                        }
                    }
                }
                trees.add(orgDto);
            }
        }
        return trees;
    }

    /**
     * 功能描述：实现树形菜单组织架构的数据的组装
     *
     * @param orgList 需要组装的组织架构的数据
     * @return 返回组装好的数据
     */
    public static List<OrgCascaderDto> installOrgCascader(List<Org> orgList) {
        List<OrgCascaderDto> orgCascaderDtos = new ArrayList<>();
        OrgCascaderDto orgCascaderDto = null;
        for (Org org : orgList) {
            orgCascaderDto = new OrgCascaderDto();
            orgCascaderDto.setValue(org.getOrgId().toString());
            orgCascaderDto.setLabel(org.getOrgName());
            if (0 == org.getParentOrgId()) {
                orgCascaderDto.setChildren(getCascaderChild(org.getOrgId(), orgList));
                orgCascaderDtos.add(orgCascaderDto);
            }
        }
        return orgCascaderDtos;
    }

    /**
     * 功能描述：递归遍历组织架构节点
     *
     * @param id 父节点ID
     * @param orgList 组织架构数据
     * @return 递归组装数据
     */
    private static List<OrgCascaderDto> getCascaderChild(Integer id, List<Org> orgList) {
        List<OrgCascaderDto> childList = new ArrayList<>();
        OrgCascaderDto orgCascaderDto = null;
        for (Org org : orgList) {
            orgCascaderDto = new OrgCascaderDto();
            orgCascaderDto.setValue(org.getOrgId().toString());
            orgCascaderDto.setLabel(org.getOrgName());
            if (org.getParentOrgId().intValue() == id.intValue()) {
                orgCascaderDto.setChildren(getCascaderChild(org.getOrgId(), orgList));
                childList.add(orgCascaderDto);
            }
        }
        return childList;
    }

    /**
     * 功能描述：实现树形菜单组织架构的数据的组装
     *
     * @param orgDtoList 需要组装的组织架构的数据
     * @return 返回组装好的数据
     */
    public static List<OrgDto> installOrg(List<OrgDto> orgDtoList) {
        List<OrgDto> orgDtos = new ArrayList<>();
        for (OrgDto orgDto : orgDtoList) {
            if (0 == orgDto.getParentOrgId()) {
                orgDto.setChildren(getChild(orgDto.getOrgId(), orgDtoList));
                orgDtos.add(orgDto);
            }
        }
        return orgDtos;
    }


    /**
     * 功能描述：递归遍历菜单节点
     *
     * @param id      父节点ID
     * @param orgDtos 组织架构的集合
     * @param OrgMap  当前被选中的组织架构
     * @return 递归实现组装数据
     */
    private static List<OrgDto> getCheckChild(Integer id, List<OrgDto> orgDtos, Map<String, Object> OrgMap) {
        List<OrgDto> childList = new ArrayList<>();
        for (OrgDto orgDto : orgDtos) {
            if (orgDto.getParentOrgId().intValue() == id.intValue()) {
                orgDto.setChildren(getCheckChild(orgDto.getOrgId(), orgDtos, OrgMap));
                if (OrgMap.get(orgDto.getOrgId().toString()) != null) {
                    orgDto.setChecked(true);
                } else {
                    orgDto.setChecked(false);
                }
                if (orgDto.getChildren().size() > 0) {
                    for (OrgDto checkOrg : orgDto.getChildren()) {
                        if (!checkOrg.isChecked()) {
                            orgDto.setChecked(false);
                            break;
                        }
                    }
                }
                childList.add(orgDto);
            }
        }
        return childList;
    }

    /**
     * 功能描述：递归遍历组织架构节点
     *
     * @param id 父节点ID
     * @param orgDtos 组织架构数据
     * @return 递归组装数据
     */
    private static List<OrgDto> getChild(Integer id, List<OrgDto> orgDtos) {
        List<OrgDto> childList = new ArrayList<>();
        for (OrgDto orgDto : orgDtos) {
            if (orgDto.getParentOrgId().intValue() == id.intValue()) {
                orgDto.setChildren(getChild(orgDto.getOrgId(), orgDtos));
                childList.add(orgDto);
            }
        }
        return childList;
    }

}
