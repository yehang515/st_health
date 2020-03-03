package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.entify.Result;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.MenuService;
import com.itheima.service.RoleService;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
//@CrossOrigin(origins = "http://localhost:81/pages/*")
public class RoleController {
    @Reference
    private RoleService roleService;
    //查询所有的角色
    @RequestMapping("/findAll")
    public List<Role> findAllRole(){
        List<Role> roles = roleService.findAll();
        return roles;
    }
    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult page = roleService.findPage(queryPageBean);
        return page;
    }
    //添加角色
    @RequestMapping("/add")
    public Result add(@RequestBody Role role,Integer[] checkitemIds ){
        try {
            roleService.add(role,checkitemIds);
            return new Result(true, "新增角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新增角色失败");
        }
    }
    //根据id查询单个角色,用于数据回显
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Role role = roleService.findById(id);
            return new Result(true, "查询角色信息回显成功", role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "角色信息回显失败");
        }
    }


    //编辑角色
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role,Integer[] checkitemIds,Integer[] menuIds){
        try {
            roleService.edit(role,checkitemIds,menuIds);
            return new Result(true,"编辑角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑角色失败");
        }
    }

    //删除角色
    @RequestMapping("/deleteOne")
    public Result deleteOne(Integer id){
        Long count =  roleService.findUserById(id);
        if (count>0){
            return new Result(false,"角色关联了用户无法删除");
        }
        try {
            roleService.deleteOne(id);
            return new Result(true,"角色删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"角色删除失败");
        }
    }




    /*@Reference
    private RoleService roleService;


    @RequestMapping("/findAll.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = roleService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }*/

}
