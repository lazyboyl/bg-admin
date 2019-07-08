package com.github.bg.admin.core.mapper;


import com.github.bg.admin.core.dto.TreeDto;
import com.github.bg.admin.core.entity.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author linzf
 * @since 2019/5/5
 * 类描述：菜单实体快速转换的实现
 */
@Mapper(componentModel = "spring")
public interface TreeMapper {

    /**
     * 功能描述：实现数据的转换
     *
     * @param entity
     * @return
     */
    @Mappings({
            @Mapping(target = "title", source = "treeName"),
            @Mapping(target = "treeId", source = "treeId")
    })
    TreeDto treeToTreeDto(Tree entity);

    /**
     * 功能描述：实现集合的数据的转换，这边先设置实体之间的转换集合的设置才会实现
     *
     * @param trees
     * @return
     */
    List<TreeDto> treesToTreeDTO(List<Tree> trees);


}
