package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层Dao接口
 */
public interface SetmealDao {

    void insertSetmealAndGroup(@Param("setmealId") int id, @Param("checkgroupId") int id2);

    Page<Setmeal> findByCondition(String queryString);

    void insertSetmeal(Setmeal setmeal);

    List<Setmeal> findAllSetmeal();

    List<Integer> findsById(Integer id);

    Setmeal finSetmealById(Integer id);

    List<String> findAllsetmealNames();
}
