package com.github.bg.admin.core.service;

import com.github.bg.admin.core.dao.DictDao;
import com.github.bg.admin.core.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author linzf
 * @since 2019-05-06
 * 类描述：数据字典的service类
 */
@Service
public class DictService {

    @Autowired
    private DictDao dictDao;


    public void save(Dict dict){
        dictDao.insert(dict);
    }

}
