package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.UserDao;
import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.MenuService;
import com.itheima.service.UserService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;


    public List<Map> findMenuByUsername(String username) {
        List<Menu> menuList = menuDao.findMenuByUsername(username);
        List<Map> menusMap = new ArrayList();
        for (Menu menu : menuList) {
            //查询菜单时候包含父类菜单id
            if(menu.getParentMenuId() == null){
                //封装每一个一级菜单列表
                Map<String,Object> map = new HashMap();
                map.put("path",menu.getPath());
                map.put("title",menu.getName());
                map.put("linkUrl",menu.getLinkUrl());
                map.put("children",findChildren(menuList,menu));
                menusMap.add(map);
            }
        }
        //便利查询结果
        return menusMap;
    }

    private List<Map> findChildren(List<Menu> menuList, Menu menu) {
        List<Map> childListMenu = new ArrayList();
        for (Menu childMenu : menuList) {
            //如果某菜单的父类id和别的菜单id一样,那么封装子菜单
            if(menu.getId() == childMenu.getParentMenuId()){
                Map map = new HashMap();
                map.put("path",childMenu.getPath());
                map.put("title",childMenu.getName());
                map.put("linkUrl",childMenu.getLinkUrl());
                //使用递归的方式遍历子菜单
                map.put("children",findChildren(menuList,childMenu));
                childListMenu.add(map);
            }
        }
        return childListMenu;
    }




    //分页查询角色
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Role> page = menuDao.findPage(queryPageBean.getQueryString());
        List<Role> rows = page.getResult();
        long total = page.getTotal();
        return new PageResult(total,rows);
    }


    //查询所有菜单
    public List<Menu> findAllMenu() {
        return menuDao.findAllMenu();
    }


    //查询角色所拥有的菜单
    public List<Integer> findMenusByRoleId(Integer id) {
        return menuDao.findMenusByRoleId(id);
    }


    //菜单分页查询
    public PageResult findMenuPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Menu> page = menuDao.findMenuPage(queryPageBean.getQueryString());
        List<Menu> rows = page.getResult();
        long total = page.getTotal();
        return new PageResult(total,rows);
    }


    //新增菜单
    public void addMenu(Menu menu) {
        menuDao.addMenu(menu);
    }

    //根据id查询菜单
    public Menu findMenuById(Integer id) {
        return menuDao.findMenuById(id);
    }


    //编辑菜单
    public void editMenu(Menu menu) {
        menuDao.editMenu(menu);
    }


    //删除菜单
    public void delete(Integer id) {
        List<Integer> roles = menuDao.findMenusByMenuId(id);
        if(roles!=null&&roles.size() > 0){
            return;
        }
        menuDao.delete(id);
    }

    @Override
    public List<Integer> findMenuIdsById(Integer id) {
        return menuDao.findMenuIdsById(id);
    }
}
