package com.github.bg.admin.core.service;

import com.github.bg.admin.core.constant.SystemStaticConst;
import com.github.bg.admin.core.dao.DictDao;
import com.github.bg.admin.core.entity.Dict;
import com.github.bg.admin.core.entity.Page;
import com.github.bg.admin.core.entity.ReturnInfo;
import com.github.bg.admin.core.util.PageUtil;
import com.github.bg.admin.core.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019-05-06
 * 类描述：数据字典的service类
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class DictService {

    /**
     * 字典的dao
     */
    @Autowired
    private DictDao dictDao;

    /**
     * 功能描述：加载全部的字典数据
     * @return 返回操作结果
     */
    public ReturnInfo loadAll(){
        try{
            List<Dict> dictList = dictDao.queryDictList("",new String [0]);
            return new ReturnInfo(SystemStaticConst.SUCCESS, "加载全部的字典数据成功",dictList);
        }catch (Exception e){
            return new ReturnInfo(SystemStaticConst.FAIL, "加载全部的字典数据失败！失败原因：" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新字典数据
     * @param dictCode 字典编码
     * @param dictText 字典文本
     * @param dictValue 字典值
     * @param id 字典流水id
     * @return 返回更新结果
     */
    public ReturnInfo updateDict(String dictCode,String dictText,String dictValue,String id){
        try{
            Dict dict = dictDao.selectByPrimaryKey(id);
            if (dictDao.checkTypeAndCode(dict.getId(), dict.getDictType(), dictCode) > 0) {
                return new ReturnInfo(SystemStaticConst.FAIL, "字典类型和字典编码已经存在，请修改以后再提交！");
            }
            if(dictDao.updateDict(dictCode, dictText, dictValue, id)>0){
                return  new ReturnInfo(SystemStaticConst.SUCCESS, "更新字典数据成功");
            }
            return new ReturnInfo(SystemStaticConst.FAIL, "更新字典数据失败！失败原因：查无此字典数据");
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnInfo(SystemStaticConst.FAIL, "更新字典数据失败！失败原因：" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据字典流水来获取字典数据
     * @param id 字典流水ID
     * @return 返回操作结果
     */
    public ReturnInfo getDict(String id){
        try{
            Dict dict = dictDao.selectByPrimaryKey(id);
            if(dict!=null){
                return new ReturnInfo(SystemStaticConst.SUCCESS, "获取字典数据成功", dict);
            }
        }catch (Exception e){
            return new ReturnInfo(SystemStaticConst.FAIL, "获取字典数据失败！失败原因：" + e.getMessage());
        }
        return new ReturnInfo(SystemStaticConst.FAIL, "获取字典数据失败！失败原因：查无此字典数据");
    }

    /**
     * 功能描述：实现删除字典数据
     *
     * @param id 字典流水ID
     * @return 返回删除结果
     */
    public ReturnInfo deleteDict(String id) {
        try {
            if (dictDao.deleteByPrimaryKey(id) > 0) {
                return new ReturnInfo(SystemStaticConst.SUCCESS, "删除字典数据成功");
            }
            return new ReturnInfo(SystemStaticConst.FAIL, "删除字典数据失败！失败原因：该字典数据不存在");
        } catch (Exception e) {
            return new ReturnInfo(SystemStaticConst.FAIL, "删除字典数据失败！失败原因：" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现增加字典数据
     *
     * @param dict 包含字典数据的实体
     * @return 返回操作结果
     */
    public ReturnInfo addDict(Dict dict) {
        try {
            if (dictDao.checkTypeAndCode(dict.getId(), dict.getDictType(), dict.getDictCode()) > 0) {
                return new ReturnInfo(SystemStaticConst.FAIL, "字典类型和字典编码已经存在，请修改以后再提交！");
            }
            dictDao.insert(dict);
        } catch (Exception e) {
            return new ReturnInfo(SystemStaticConst.FAIL, "增加字典数据失败，失败原因：" + e.getMessage());
        }
        return new ReturnInfo(SystemStaticConst.SUCCESS, "增加字典数据成功", dict);
    }

    /**
     * 功能描述：验证字典的类型和编码是否重复
     *
     * @param id       字典流水ID
     * @param dictType 字典类型
     * @param dictCode 字典编码
     * @return 返回验证结果
     */
    public ReturnInfo checkTypeAndCode(String id, String dictType, String dictCode) {
        Map<String, Object> result = new HashMap<>(1);
        try {
            // 查询的结果大于0表示数据库已经存在该数据了，反之则不存在
            if (dictDao.checkTypeAndCode(id, dictType, dictCode) > 0) {
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
     * 功能描述：获取数据字典列表
     *
     * @param search   模糊匹配数据字典的dictType、dictText、dictValue、dictCode
     * @param dictCode 模糊查询dictCode
     * @param pageSize 每页显示的记录的条数
     * @param current  当前访问第几页
     * @param orderKey     排序字段
     * @param orderByValue 排序方式，降序还是升序
     * @return 返回查询结果
     */
    public ReturnInfo queryDictList(String search,String dictCode, int pageSize, int current, String orderKey, String orderByValue) {
        PageHelper.startPage(current, (pageSize > 0 && pageSize <= 500) ? pageSize : 20,(orderKey != null && !"".equals(orderKey)) ? ((orderByValue != null && !"".equals(orderByValue)) ? (orderKey + " " + orderByValue) : orderKey) : "");
        String [] dictCodeArray = StringUtils.getObjStr(dictCode).split(" ");
        HashMap<String, Object> res = PageUtil.getResult(dictDao.queryDictList(search,dictCodeArray));
        return new ReturnInfo(SystemStaticConst.SUCCESS, "获取数据字典列表数据成功！", new Page(pageSize, current, (long) res.get("total"), (List) res.get("rows")));
    }


}
