package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String,Object> map = new HashMap();
        String today = DateUtils.parseDate2String(DateUtils.getToday());//当前日期
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());//当前日期本周一
        String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        map.put("reportDate",today);//当前日期
        Integer todayNewMember = memberDao.getCountByRegtime(today);
        map.put("todayNewMember",todayNewMember);//当日新增会员数
        Integer totalMember = memberDao.getTotalMember();
        map.put("totalMember",totalMember);//总会员数
        Integer thisWeekNewMember = memberDao.getThisWeekNewMember(monday);
        map.put("thisWeekNewMember",thisWeekNewMember);//当周新增会员数
        Integer thisMonthNewMember = memberDao.getThisMonthNewMember(firstDayOfMonth);
        map.put("thisMonthNewMember",thisMonthNewMember);//当月新增会员数
        Integer todayOrderNumber = orderDao.getTodayOrderNumber(today);
        map.put("todayOrderNumber",todayOrderNumber);//当日新订单
        Integer todayVisitsNumber =orderDao.getTodayVisitsNumber(today);
        map.put("todayVisitsNumber",todayVisitsNumber);//当日到诊订单数
        Integer thisWeekOrderNumber = orderDao.getThisWeekOrderNumber(monday);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);//当周订单数量
        Integer thisWeekVisitsNumber = orderDao.getThisWeekVisitsNumber(monday);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);//当周到诊订单数量
        Integer thisMonthOrderNumber = orderDao.getThisMonthOrderNumber(firstDayOfMonth);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);//当月订单数量
        Integer thisMonthVisitsNumber = orderDao.getThisMonthVisitsNumber(firstDayOfMonth);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);//当月到诊订单数量

        List<Map<String,Object>> hotSetmeal = orderDao.getHotSetmeal();//热门套餐
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
