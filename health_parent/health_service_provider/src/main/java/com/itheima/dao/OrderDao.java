package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order order);

    void add(Order order);

    Order selectOrderById(Integer id);

    Map<String,Object> findById4Detail(Integer id);

    List<Map<String, Object>> findAllSetmealCount();

    Integer getTodayOrderNumber(String today);

    Integer getTodayVisitsNumber(String today);

    Integer getThisWeekOrderNumber(String monday);

    Integer getThisWeekVisitsNumber(String monday);

    Integer getThisMonthOrderNumber(String firstDayOfMonth);

    Integer getThisMonthVisitsNumber(String firstDayOfMonth);

    List<Map<String, Object>> getHotSetmeal();
}
