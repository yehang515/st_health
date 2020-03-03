package com.itheima.service;

import com.itheima.entify.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface SetmealService {

    Setmeal findGrouplist();

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void insertOne(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> findAllSetmeal();

    Setmeal findSetmealById(int id);

}
