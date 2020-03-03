package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.entify.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceimpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;


    //添加
    @Override
    public void add(Permission permission) {

        permissionDao.add(permission);

    }

    //分页
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Permission> page = permissionDao.selectByCondition(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    //id
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        permissionDao.deleteById(id);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public Set<Permission> findByRoleId(Integer id) {
        return  permissionDao.findByRoleId(id);
    }

    @Override
    public List<Integer> findByRoleIds(Integer id) {
        return permissionDao.findByRoleIds(id);
    }


    //查询所有
    @Override
    public List<Permission> findAll() {
       return permissionDao.findAll();
    }
}
