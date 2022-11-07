package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.entity.Sservice;
import com.markerhub.entity.UserSs;
import com.markerhub.service.BlogService;
import com.markerhub.service.SserviceService;
import com.markerhub.service.UserSsService;
import com.markerhub.util.R;
import com.markerhub.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 */
@RestController

public class SserviceController {


    @Autowired
    SserviceService sserviceService;
    @Autowired
    UserSsService userSsService;

    //获取微服务列表
    @GetMapping("/sservices")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "5") Integer pageSize,
                       @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Sservice> queryWrapper = new QueryWrapper<Sservice>().orderByAsc("id");
        queryWrapper.like("name", name);
        //分页
        Page page = new Page(currentPage, pageSize);
        IPage pageData = sserviceService.page(page, queryWrapper);
        System.out.println(pageData);
        return Result.succ(pageData);
    }

    //添加
    @ResponseBody
    @RequestMapping(value = "/addSservice")
    public R<String> addSservice(@RequestBody Sservice sservice) {
        R<String> result = new R<String>();
        try {
            System.out.println(sservice);
            sserviceService.addSservice(sservice);
            result.code = 200;
            result.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteSservice")
    public R<String> deleteSservice(Integer id) {
        R<String> result = new R<String>();
        try {
            System.out.println(id);
            sserviceService.deleteSservice(id);
            result.code = 200;
            result.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.setMsg("服务器错误");
        }
        return result;
    }

    //根据id获得微服务信息
    @GetMapping("/sservices/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Sservice sservice = sserviceService.getById(id);

        //传入的sservice不能为空，否则抛出异常
        Assert.notNull(sservice, "该微服务已被删除");
//        System.out.println(Result.succ(sservice));
//        Result(code=200, msg=操作成功, data=Sservice(id=2, name=s2测试))

        return Result.succ(sservice);
    }
    //修改
    @PutMapping("/sservices")
    public Result putSservice(@RequestBody Sservice sservice) {
        Sservice sservice1 = sserviceService.getById(sservice.getId());
        BeanUtil.copyProperties(sservice,sservice1);

        sserviceService.saveOrUpdate(sservice1);
        return Result.succ(null);
    }
    //删除
    @DeleteMapping("/sservices")
    public Result deleteSservice(@RequestParam(defaultValue = "") Long id) {
        sserviceService.removeById(id);

        userSsService.remove(new QueryWrapper<UserSs>().eq("ss_id",id));
        return Result.succ(null);
    }



}
