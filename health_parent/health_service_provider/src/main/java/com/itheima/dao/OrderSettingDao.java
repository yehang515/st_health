package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 持久层Dao接口
 */
public interface OrderSettingDao {

    long findByOrderDate(Date orderDate);

    void updateOrderSetting(OrderSetting orderSetting);

    void insertOrderSetting(OrderSetting orderSetting);

     public List<OrderSetting> getOrderListByDateStr(String date);

     OrderSetting findOrderByDate(Date orderDate);

    void editReservations(OrderSetting orderSetting);
}
