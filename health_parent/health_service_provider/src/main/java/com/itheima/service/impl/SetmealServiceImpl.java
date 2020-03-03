package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entify.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查项服务
 * 指定提供服务的接口名
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Setmeal findGrouplist() {
        Page<CheckGroup> checkgroups = checkGroupDao.selectByCondition(null);
        List<CheckGroup> result = checkgroups.getResult();
        Setmeal setmeal = new Setmeal();
        setmeal.setCheckGroups(result);
        return setmeal;
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmeals = setmealDao.findByCondition(queryString);
        List<Setmeal> result = setmeals.getResult();
        long total = setmeals.getTotal();
        return new PageResult(total,result);
    }

    @Override
    public void insertOne(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.insertSetmeal(setmeal);
        //根据插入的信息获取id
        Integer setmealId = setmeal.getId();
        if(checkgroupIds.length>0 && checkgroupIds !=null){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.insertSetmealAndGroup(setmealId,checkgroupId);
            }
        }
        //存储完成之后将上传的文件名存储到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    @Override
    public List<Setmeal> findAllSetmeal() {
        List<Setmeal> list = setmealDao.findAllSetmeal();
        return list;
    }

    @Override
    public Setmeal findSetmealById(int id) {
        Setmeal setmeal = setmealDao.finSetmealById(id);
        //首先根据检查套餐id查询中间表看关联了那几个检查组
        List<Integer> groupIds = setmealDao.findsById(id);
        //遍历查询结果groupIds
        if(groupIds.size()>0 && groupIds != null){
            List<CheckGroup> checkGroupList = new ArrayList<CheckGroup>();
            for (Integer groupId : groupIds) {
                CheckGroup checkGroup = checkGroupDao.findById(groupId);
                //获取检查组中的检查项
                List<Integer> itemIds = checkGroupDao.findItemByGroupIds(groupId);
                if(itemIds.size()>0 && itemIds != null){
                    List<CheckItem> checkItemList = new ArrayList<CheckItem>();
                    for (Integer itemId : itemIds) {
                        CheckItem checkItem = checkItemDao.findById(itemId);
                        checkItemList.add(checkItem);
                    }
                    checkGroup.setCheckItems(checkItemList);
                }
                checkGroupList.add(checkGroup);
            }
            setmeal.setCheckGroups(checkGroupList);
        }
        return setmeal;
    }


}
