package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    User findByUsername(String username);

    Page<User> selectByCondition(String queryString);

    void addUser(User user);

    void insertUserAndRole(@Param("user_id") int userId, @Param("role_id") int roleId);

    User findById(Integer id);

    void delUserAndRole(Integer id);

    void updateUser(User user);

    List<Integer> findByRoleIds(Integer userId);

    String findStationByUsername(String username);
}
