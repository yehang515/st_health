package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

/**
 * 持久层Dao接口
 */
public interface CheckItemDao {
    public void add(CheckItem checkItem);
    Page<CheckItem> selectByCondition(String queryString);
    long selectGroupAndItem(Integer id);
    void deleteItemById(Integer id);


    CheckItem findById(Integer id);

    void updateItem(CheckItem checkItem);
}
