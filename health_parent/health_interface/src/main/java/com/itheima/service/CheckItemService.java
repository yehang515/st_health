package com.itheima.service;


import com.itheima.entify.PageResult;
import com.itheima.entify.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @PackageName: cn.itcast.service
 * @ClassName: CheckItemService
 * @Author: dongxiyaohui
 * @Date: 2019/11/29 19:00
 * @Description: //TODO
 */
public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult pageQuery(Integer currentPage,Integer pageSize, String queryString);

    void deleteOneItem(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);
}
