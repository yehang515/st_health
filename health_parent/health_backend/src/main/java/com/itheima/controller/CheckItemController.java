package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.entify.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    //获取服务
    @Reference
    private CheckItemService checkItemService;

    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限校验
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            //判断添加数据是否成功,如果失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")//设置权限校验
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
//        System.out.println(pageResult);
        return pageResult;
    }

    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping("/deleteOne.do")
    public Result deleteOne(Integer id){
        try {
            checkItemService.deleteOneItem(id);
        } catch (Exception e) {
            //服务失败
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @PreAuthorize("hasAnyAuthority('USER_QUERY')")//权限校验
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        CheckItem checkItem = null;
        try {
            checkItem = checkItemService.findById(id);
        } catch (Exception e) {
            //服务失败
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL,checkItem);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限校验
    @RequestMapping("/update.do")
    public Result update(@RequestBody CheckItem checkItem){
        try {
            checkItemService.update(checkItem);
        } catch (Exception e) {
            //判断修改数据是否成功,如果失败
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
