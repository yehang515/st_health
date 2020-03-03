package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entify.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        //根据用户id查询用户所对应的角色列表
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        if (roles.size() > 0 && roles != null) {
            for (Role role : roles) {
                //根据角色id查询每个角色对应的权限列表
                Integer roleId = role.getId();
                Set<Permission> permissions =permissionDao.findByRoleId(roleId);
                if (permissions.size() > 0 && permissions != null) {
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //使用mybatis框架中的分页助手进行条件查询
        PageHelper.startPage(currentPage,pageSize);
        //将查询结果封装为page对象
        Page<User> page = userDao.selectByCondition(queryString);
        //获取所需要的结果封装结果集
        long total = page.getTotal();
        List<User> result = page.getResult();
        return new PageResult(total,result);
    }

    @Override
    public User findRoles() {
        Page<Role> roles = roleDao.selectByCondition(null);
        List<Role> roleList = roles.getResult();
        Set<Role> roleSet = new HashSet<Role>(roleList);
        User user = new User();
        user.setRoles(roleSet);
        return user;
    }

    @Override
    public void add(User user, Integer[] roles) {
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashpw);
        userDao.addUser(user);
        Integer userId = user.getId();
        if(roles.length>0 && roles != null){
            for (Integer roleId : roles) {
                userDao.insertUserAndRole(userId,roleId);
            }
        }
    }

    public User findById(Integer id) {
        User user = null;
        try {
            user = userDao.findById(id);
            Set<Role> roles = roleDao.findByUserId(id);
            user.setRoles(roles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    public void update(User user, Integer[] roleIds) {
        //首先根据用户id删除中间表对应字段,然后再进行修改存储
        userDao.delUserAndRole(user.getId());
        //删除之后,再将检查组表进行修改
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashpw);
        userDao.updateUser(user);
        //修改弄成之后再进行中间表的插入
        for (Integer roleId : roleIds) {
            userDao.insertUserAndRole(user.getId(),roleId);
        }
    }


    public List<Integer> findByRoleIds(Integer userId) {
        List<Integer> roleIds = userDao.findByRoleIds(userId);
        return roleIds;
    }

    @Override
    public String findStationByUsername(String username) {
        String station = userDao.findStationByUsername(username);
        return station;
    }
}
