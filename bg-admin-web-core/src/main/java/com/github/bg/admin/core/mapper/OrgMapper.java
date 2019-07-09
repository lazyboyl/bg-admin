package com.github.bg.admin.core.mapper;


import com.github.bg.admin.core.dto.OrgDto;
import com.github.bg.admin.core.entity.Org;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author linzf
 * @since 2019/5/9
 * 类描述： 组织架构快速转换的实现
 */
@Mapper(componentModel = "spring")
public interface OrgMapper {

    /**
     * 功能描述：实现数据的转换
     *
     * @param entity
     * @return
     */
    @Mappings({
            @Mapping(target = "title", source = "orgName"),
            @Mapping(target = "orgId", source = "orgId")
    })
    OrgDto orgToOrgDto(Org entity);


    /**
     * 功能描述：实现集合的数据的转换
     *
     * @param orgs
     * @return
     */
    List<OrgDto> orgsToOrgDto(List<Org> orgs);

}
