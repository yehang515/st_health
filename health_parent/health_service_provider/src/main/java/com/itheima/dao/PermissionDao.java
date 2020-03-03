package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;


public interface PermissionDao {

    Set<Permission> findByRoleId(Integer roleId);


    void add(Permission permission);

    List<Permission> findAll();

    Page<Permission> selectByCondition(String queryString);

    Permission findById(Integer id);


    public void deleteById(Integer id);

    void edit(Permission permission);

    List<Integer> findByRoleIds(Integer id);
}
