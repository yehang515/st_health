package com.itheima.service;

import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void add(List<OrderSetting> orderSettingList);

    List<Map> getOrderSettingsByDateStr(String date);

    void editNumberByDateStr(OrderSetting orderSetting);
}
