package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.entify.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查项服务
 * 指定提供服务的接口名
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService{
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckGroup checkGroup,Integer[] checkitemIds) {
        //分别插入两张表,检查组表和中间表
        checkGroupDao.add(checkGroup);
        if(checkitemIds.length>0 && checkitemIds!=null){
            for (Integer itemId : checkitemIds) {
               /* Map<String,Integer> map = new HashMap<String,Integer>();
                map.put("checkgroup_id",checkGroup.getId());
                map.put("checkitem_id",itemId);*/
                checkGroupDao.addGroupAndItem(checkGroup.getId(),itemId);
            }
        }


    }

    /**
     * 分页条件查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //使用mybatis框架中的分页助手进行条件查询
        PageHelper.startPage(currentPage,pageSize);
        //将查询结果封装为page对象
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        //获取所需要的结果封装结果集
        long total = page.getTotal();
        List<CheckGroup> result = page.getResult();
        return new PageResult(total,result);
    }

    /**
     * 点击删除按钮删除数据
     * @param id
     */
    @Override
    public void deleteOneGroup(Integer id) {
        //判断该检查项是否被检查组所引用,如果被引用,不允许被删除
        //查询检查组和检查项中间表
        long count = checkGroupDao.selectGroupAndSetmeal(id);
        if(count>0){
            //表示已经被检查组所引用,不允许被删除
            throw new RuntimeException();
        }
        checkGroupDao.deleteGroupAndItemByGroupId(id);
        checkGroupDao.deleteGroupById(id);
    }

    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        //根据groupId查询中间表对应的检查项
        List<Integer> itemIdList = checkGroupDao.findItemByGroupIds(id);
        //检查组中包含的检查项对象
        List<CheckItem> checkItemList = new ArrayList<CheckItem>();
        for (Integer itemId : itemIdList) {
            CheckItem checkItem = checkItemDao.findById(itemId);
            checkItemList.add(checkItem);
        }
        checkGroup.setCheckItems(checkItemList);
        return checkGroup;
    }

    @Override
    public void update(CheckGroup checkGroup,Integer[] checkitemIds) {
        //首先根据检查组id删除中间表对应字段,然后再进行修改存储
        checkGroupDao.deleteGroupAndItemByGroupId(checkGroup.getId());
        //删除之后,再将检查组表进行修改
        checkGroupDao.updateGroup(checkGroup);
        //修改啊我弄成之后再进行中间表的插入
        for (Integer itemId : checkitemIds) {
            checkGroupDao.addGroupAndItem(checkGroup.getId(),itemId);
        }
    }

    @Override
    public CheckGroup findItemList() {
        Page<CheckItem> checkItems = checkItemDao.selectByCondition(null);
        List<CheckItem> result = checkItems.getResult();
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setCheckItems(result);
        return checkGroup;
    }

    @Override
    public List<Integer> findItemIdsByGroupId(Integer id) {
        //根据groupId查询中间表对应的检查项
        List<Integer> itemIdList = checkGroupDao.findItemByGroupIds(id);
        return itemIdList;
    }
}
