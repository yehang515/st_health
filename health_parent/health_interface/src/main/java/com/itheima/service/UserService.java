package com.itheima.service;

import com.itheima.entify.PageResult;
import com.itheima.pojo.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    User findRoles();

    void add(User user, Integer[] roles);

    User findById(Integer id);

    void update(User user, Integer[] roleIds);

    List<Integer> findByRoleIds(Integer userId);

    String findStationByUsername(String username);
}
