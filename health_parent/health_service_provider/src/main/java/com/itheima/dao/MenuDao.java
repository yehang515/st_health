package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;

public interface MenuDao {
    List<Menu> findMenuByUsername(String username);

    void delMenuIds(Integer id);

    void addRoleAndMenu(Map<String, Object> map);

    Page<Role> findPage(String queryString);

    List<Menu> findAllMenu();

    List<Integer> findMenusByRoleId(Integer id);

    Page<Menu> findMenuPage(String queryString);

    void addMenu(Menu menu);

    Menu findMenuById(Integer id);

    void editMenu(Menu menu);

    List<Integer> findMenusByMenuId(Integer id);

    void delete(Integer id);

    List<Integer> findMenuIdsById(Integer id);

}
