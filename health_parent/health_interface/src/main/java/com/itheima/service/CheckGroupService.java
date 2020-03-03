package com.itheima.service;

import com.itheima.entify.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void deleteOneGroup(Integer id);

    CheckGroup findById(Integer id);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    CheckGroup findItemList();

    List<Integer> findItemIdsByGroupId(Integer id);
}
