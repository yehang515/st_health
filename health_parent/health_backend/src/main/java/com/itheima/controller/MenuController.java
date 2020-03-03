package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.entify.Result;

import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody  QueryPageBean queryPageBean){
        return menuService.findPage(queryPageBean);
    }

    //菜单分页查询
    @RequestMapping("/findMenuPage")
    public PageResult findMenuPage(@RequestBody  QueryPageBean queryPageBean){
        return menuService.findMenuPage(queryPageBean);
    }

    //查询所有菜单
    @RequestMapping("/findAllMenu")
    public Result findAllMenu(){
        try {
            List<Menu> menuList = menuService.findAllMenu();
            return new Result(true, "查询成功",menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }


    //根据角色id查询所有菜单
    @RequestMapping("/findMenusByRoleId")
    public Result findMenusByRoleId(Integer id){
        try {
            List<Integer> menuIds = menuService.findMenusByRoleId(id);
            return new Result(true, "查询成功",menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }


    //根据id查询菜单回写
    @RequestMapping("/findMenuById")
    public Result findMenuById(Integer id){
        try {
            Menu menu = menuService.findMenuById(id);
            return new Result(true, "回写成功",menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"回写失败");
        }
    }


    //新增菜单
    @RequestMapping("/addMenu")
    public Result addMenu(@RequestBody Menu menu){
        try {
            menuService.addMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"新增失败");
        }
        return new Result(true, "新增成功");
    }


    //编辑菜单
    @RequestMapping("/editMenu")
    public Result editMenu(@RequestBody Menu menu){
        try {
            menuService.editMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑失败");
        }
        return new Result(true, "编辑成功");
    }


    //删除菜单
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            menuService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
        return new Result(true, "删除成功");
    }



    @RequestMapping("/findMenuIdsById")
    public Result findMenuIdsById(Integer id){
        try {
            List<Integer> menuIds =  menuService.findMenuIdsById(id);
            return new Result(true, "查询成功",menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }

    }
}
