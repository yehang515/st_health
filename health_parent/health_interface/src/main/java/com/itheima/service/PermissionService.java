package com.itheima.service;

import com.itheima.entify.PageResult;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionService {
    void add(Permission permission);

    List<Permission> findAll();

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Permission findById(Integer id);

    void deleteById(Integer id);

    void edit(Permission permission);

    Set<Permission> findByRoleId(Integer id);

    List<Integer> findByRoleIds(Integer id);
}
