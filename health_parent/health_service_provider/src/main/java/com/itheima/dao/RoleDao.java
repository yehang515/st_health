package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


public interface RoleDao {
    List<Role> findAll();

    Page<Role> findByQueryString(String queryString);

    void addRoleAndPermission(@Param("rid") Integer id, @Param("pid") Integer pId);

    void add(Role role);

    Role findByid(Integer id);

    void deleteAllpermissionIds(Integer id);

    void updateRole(Role role);

    void deleteAllMenu(Integer id);

    Long findUserById(Integer id);

    void deleteRole(Integer id);

    Set<Role> findByUserId(Integer userId);

    Page<Role> selectByCondition(String queryString);
}
