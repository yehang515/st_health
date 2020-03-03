package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.entify.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;


    @RequestMapping("/add")
    public Result add(@RequestBody Permission Permission){
        try {
            permissionService.add(Permission);
        }catch (Exception e){
            return new Result(false, "添加权限失败");
        }
        return new Result(true,"添加权限成功");
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = permissionService.pageQuery(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return  pageResult;

    }


    @RequestMapping("/findAll")
    public Result findAll(){
        List<Permission> permissionList = permissionService.findAll();
        if(permissionList !=null && permissionList.size()>0){
            Result result=new Result(true, "查询权限成功",permissionList);
            return  result;
        }
        return new Result(false, "查询权限失败");
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Permission permission=permissionService.findById(id);
            return new Result(true,"查询权限成功",permission);
        }catch (Exception e){
            return  new Result(false, "查询权限失败");
        }

    }

    @RequestMapping("/findByRoleIds")
    public Result findByRoleIds(Integer id){
        try {
            List<Integer> checkitemIds=permissionService.findByRoleIds(id);
            return new Result(true, "查询成功",checkitemIds);
        }catch (Exception e){
            return  new Result(false,"查询失败");
        }

    }


    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            permissionService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();

            return new Result(false,"删除成功");
        }
        return  new Result(true, "删除失败");
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);

        }catch (Exception e){

            return new Result(false,"编辑失败");

        }

        return new Result(true,"编辑成功");

    }

}
