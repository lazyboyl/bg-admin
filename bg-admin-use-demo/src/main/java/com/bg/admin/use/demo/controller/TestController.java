package com.bg.admin.use.demo.controller;

import com.bg.admin.use.demo.dao.TTestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linzf
 * @since 2020/5/8
 * 类描述：
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TTestDao testDao;

    @GetMapping("testGet")
    public String test(){
        return "test" + testDao.selectAll().size();
    }

}
