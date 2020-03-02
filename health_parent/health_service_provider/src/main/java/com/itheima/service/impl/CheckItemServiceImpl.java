package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entify.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 * 指定提供服务的接口名
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService{
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
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
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> result = page.getResult();
        System.out.println(result);
        return new PageResult(total,result);
    }

    /**
     * 点击删除按钮删除数据
     * @param id
     */
    @Override
    public void deleteOneItem(Integer id) {
        //判断该检查项是否被检查组所引用,如果被引用,不允许被删除
        //查询检查组和检查项中间表
        long count = checkItemDao.selectGroupAndItem(id);
        if(count>0){
            //表示已经被检查组所引用,不允许被删除
            new RuntimeException();
        }
        checkItemDao.deleteItemById(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.updateItem(checkItem);
    }
}
