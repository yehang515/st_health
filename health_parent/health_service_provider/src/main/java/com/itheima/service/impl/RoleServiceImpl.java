package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.RoleService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleDao roleDao;
    //查询所有的角色
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //调用mybatis分页助手
        PageHelper.startPage(currentPage,pageSize);
        //调用方法完成条件查询;
        Page<Role> groupPage = roleDao.findByQueryString(queryString);
        List<Role> result = groupPage.getResult();
        long total = groupPage.getTotal();
        return new PageResult(total,result);
    }

    //添加角色
    public void add(Role role, Integer[] checkitemIds) {
        roleDao.add(role);
        if (checkitemIds != null && checkitemIds.length > 0){
            for (Integer pId : checkitemIds) {
                roleDao.addRoleAndPermission(role.getId(),pId);
            }
        }
    }

    //根据id查询单个角色用于数据回显
    public Role findById(Integer id) {
        Role role = roleDao.findByid(id);
        return role;
    }

    //编辑角色信息
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {

        //根据检查组id删除之前所有的检查项
        roleDao.deleteAllpermissionIds(role.getId());
        //根据检查组id删除之前所有的
        menuDao.delMenuIds(role.getId());
        //修改检查组基本信息
        roleDao.updateRole(role);
        //重新给中间表赋值
        if (permissionIds!=null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                roleDao.addRoleAndPermission(role.getId(), permissionId);
            }
        }

        Map<String,Object> map = new HashMap();
        if (menuIds!=null && menuIds.length > 0) {
            map.put("roleId",role.getId());
            map.put("menuIds",menuIds);
            menuDao.addRoleAndMenu(map);
        }
    }

    //根据id删除角色
    public void deleteOne(Integer id) {
        //根据id删除中间表
        roleDao.deleteAllpermissionIds(id);
        roleDao.deleteAllMenu(id);
        //根据id删除角色基本信息
        roleDao.deleteRole(id);
    }

    @Override
    public Long findUserById(Integer id) {
        return roleDao.findUserById(id);
    }



   /* @Autowired
    private RoleDao roleDao;

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //使用mybatis框架中的分页助手进行条件查询
        PageHelper.startPage(currentPage,pageSize);
        //将查询结果封装为page对象
        Page<Role> page = roleDao.selectByCondition(queryString);
        //获取所需要的结果封装结果集
        long total = page.getTotal();
        List<Role> result = page.getResult();
        return new PageResult(total,result);
    }*/
}
