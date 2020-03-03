package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;

import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.entify.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    //获取服务
    @Reference
    private CheckGroupService checkGroupService;


    @RequestMapping("/checkitemList.do")
    public CheckGroup checkitemList(){
        CheckGroup checkGroup = checkGroupService.findItemList();
        return checkGroup;
    }

    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")//权限校验
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        } catch (Exception e) {
            //判断添加数据是否成功,如果失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
//        System.out.println(pageResult);
        return pageResult;
    }

    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")//权限校验
    @RequestMapping("/deleteOne.do")
    public Result deleteOne(Integer id){
        try {
            checkGroupService.deleteOneGroup(id);
        } catch (Exception e) {
            //服务失败
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        CheckGroup checkGroup = null;
        try {
            checkGroup = checkGroupService.findById(id);
        } catch (Exception e) {
            //服务失败
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL,checkGroup);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    @RequestMapping("/findByCheckItemIds.do")
    public Result findByCheckItemIds(Integer id){
        List<Integer> checkItemIds = null;
        try {
            checkItemIds = checkGroupService.findItemIdsByGroupId(id);
        } catch (Exception e) {
            //服务失败
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL,checkItemIds);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
    }

    @RequestMapping("/update.do")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.update(checkGroup,checkitemIds);
        } catch (Exception e) {
            //判断修改数据是否成功,如果失败
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }
}
