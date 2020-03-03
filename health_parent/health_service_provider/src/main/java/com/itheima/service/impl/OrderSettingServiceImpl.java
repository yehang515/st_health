package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entify.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderSettingService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * 检查项服务
 * 指定提供服务的接口名
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 将数据存储进数据库
     * 1.判断数据库中是否存在该条数据findByOrderDate
     * 2.如存在,执行更新操作update
     * 3.若不存在,执行插入操作insert
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        //遍历集合
        for (OrderSetting orderSetting : orderSettingList) {
            //1.判断数据库中是否存在该条数据findByOrderDate
            Date orderDate = orderSetting.getOrderDate();
            long count = orderSettingDao.findByOrderDate(orderDate);
            if(count>0){
//                2.存在,执行更新操作update
                orderSettingDao.updateOrderSetting(orderSetting);
            }else {
//                3.不存在,执行插入操作insert
                orderSettingDao.insertOrderSetting(orderSetting);

            }
        }
    }

    @Override
    public List<Map> getOrderSettingsByDateStr(String date) {
//    public List<OrderSetting> getOrderSettingsByDateStr(String date) {
        List<OrderSetting> settingList = orderSettingDao.getOrderListByDateStr(date);
        List<Map> result = new ArrayList();
        if(settingList!=null && settingList.size()>0){
            for (OrderSetting orderSetting : settingList) {
                Map<String,Object> m = new HashMap();
                m.put("date",orderSetting.getOrderDate().getDate());//获取日期数字（几号）
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }

        return result;
    }

    @Override
    public void editNumberByDateStr(OrderSetting orderSetting) {
        //首先判断当日是否已经有预约
        long count = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if(count>0){
            //表示已经存在,执行修改操作
            orderSettingDao.updateOrderSetting(orderSetting);
        }else {
            //执行添加操作
            orderSettingDao.insertOrderSetting(orderSetting);
        }

    }
}
