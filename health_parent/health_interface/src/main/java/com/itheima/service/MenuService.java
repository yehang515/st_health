package com.itheima.service;


import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {

    List<Map> findMenuByUsername(String username);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Menu> findAllMenu();

    List<Integer> findMenusByRoleId(Integer id);

    PageResult findMenuPage(QueryPageBean queryPageBean);

    void addMenu(Menu menu);

    Menu findMenuById(Integer id);

    void editMenu(Menu menu);

    void delete(Integer id);

    List<Integer> findMenuIdsById(Integer id);

}
