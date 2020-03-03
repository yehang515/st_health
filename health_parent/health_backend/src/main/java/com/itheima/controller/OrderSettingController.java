package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entify.*;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        //首先获取到上传文件之后利用工具类去读取这个文件
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            //解析list集合中的String数组,将数组中的数据存入实体对象对应的字段
            if(list.size()>0 && list != null){
                //创建一个新的集合接收数据
                List<OrderSetting> orderSettingList = new ArrayList<OrderSetting>();
                for (String[] strings : list) {
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(new Date(strings[0]));
                    orderSetting.setNumber(Integer.parseInt(strings[1]));
                    orderSettingList.add(orderSetting);
                }
                //转换完成之后将数据存入数据库
                orderSettingService.add(orderSettingList);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//'2019-11'
//由前台数据遍历可知,返回结果封装了一个集合中封装了一个map集合
        try {
//            List<OrderSetting> settingList = orderSettingService.getOrderSettingsByDateStr(date);
            List<Map> mapList = orderSettingService.getOrderSettingsByDateStr(date);
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        //根据传回的orderSetting对象修改预约人数,对象包含当日日期
        try {
            orderSettingService.editNumberByDateStr(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
