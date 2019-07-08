package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.Dict;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author linzf
 * @since 2019-07-08
 * 类描述：数据字典的dao
 */
public interface DictDao extends Mapper<Dict> {

    /**
     * 功能描述：更新字典数据
     * @param dictCode 字典编码
     * @param dictText 字典文本
     * @param dictValue 字典值
     * @param id 字典流水id
     * @return 返回更新结果
     */
    int updateDict(@Param("dictCode")String dictCode, @Param("dictText")String dictText, @Param("dictValue")String dictValue, @Param("id")String id);

    /**
     * 功能描述：验证字典的类型和编码是否重复
     * @param id 字典流水ID
     * @param dictType 字典类型
     * @param dictCode 字典编码
     * @return 返回验证结果
     */
    int checkTypeAndCode(@Param("id") String id,@Param("dictType") String dictType,@Param("dictCode") String dictCode);

    /**
     * 功能描述：获取数据字典列表
     * @param search 模糊匹配数据字典的dictType、dictText、dictValue、dictCode
     * @param dictCodeArray 分段模糊查询dictCode
     * @return 返回查询结果
     */
    List<Dict> queryDictList(@Param("search") String search, @Param("dictCodeArray")String [] dictCodeArray);

}