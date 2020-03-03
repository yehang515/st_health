package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entify.Result;
import com.itheima.util.DateUtils;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result submitOrder(Map map) throws Exception {
//        1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        //将日期数据转换成Date类型进行查询数据库
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findOrderByDate(date);//Tue Dec 24 00:00:00 CST 2019
        if(orderSetting==null){
            //没有预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

//        2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if(reservations>=number){
            //预约已满,不能预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //如果没有预约满,则先检查该用户是否是会员
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTel(telephone);
        if(member !=null){
            //如果是会员,那么
            // 3、检查用户是否重复预约（同一个<用户>在<同一天>预约了<同一个套餐>），如果是重复预约则无法完成再次预约
            Integer memberId = member.getId();
            Integer setmealId = Integer.parseInt((String)map.get("setmealId"));
//            Integer setmealId = (Integer) map.get("setmealId");//java.lang.String cannot be cast to java.lang.Integer
            String orderstatus = Order.ORDERSTATUS_NO;
            Order order = new Order(memberId,date,null,orderstatus,setmealId);
            List<Order> orderList = orderDao.findByCondition(order);
            if(orderList.size()>0 || orderList !=null){
                //已经完成了预约,不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        //不是会员(说明也没有提交过订单)
//        4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
        member = new Member();
        member.setName((String) map.get("name"));
        member.setPhoneNumber(telephone);
        member.setIdCard((String) map.get("idCard"));
        member.setSex((String) map.get("sex"));
        member.setRegTime(new Date());
        memberDao.add(member);//(返回存储之后封装新纪录id)
        //注册成功之后完成预约
        Order order = new Order();
        order.setMemberId(member.getId());//会员id
        order.setOrderDate(date);//预约日期
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐id
        orderDao.add(order);


//        5、预约成功，更新当日的已预约人数
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservations(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //不能写@Override
    public Map<String,Object> findOrderById(Integer id) {//orderId
        Map<String,Object> map = orderDao.findById4Detail(id);
//        System.out.println(map.get("orderDate"));
        Date orderDate = (Date) map.get("orderDate");
        String format = new SimpleDateFormat("yyyy-MM-dd").format(orderDate);
        map.put("orderDate",format);
        //体检人member,体检套餐setmeal,体检日期orderDate,预约类型orderType
        return map;
    }
}
