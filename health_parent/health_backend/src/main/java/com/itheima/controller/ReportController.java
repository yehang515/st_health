package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entify.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //创建一个map封装data{"months":months,"memberCount",memberCount}
        Map<String,Object> map = new HashMap();
        List<String> months = new ArrayList();
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.add(Calendar.MONTH,-12);//将当前日期向前推迟1年
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONDAY,+1);//从一年前的日期每月像后加
            String month = new SimpleDateFormat("yyyy.MM").format(calendar.getTime());
            months.add(month);
        }
        map.put("months",months);

        //根据数据库查询每月的会员注册人数
        List<Integer> memberCount = memberService.findCountByMonths(months);
        map.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        //最重要的是分析数据格式
        /*data{
            setmealNames:["套餐一","套餐二"],
            setmealCount:[
            {value:10,name:"套餐一"}
            {value:15,name:"套餐二"}
            ]
}*/
        //创建Map集合
        try {
            Map<String,Object> data = new HashMap();
            //封装侧标题名
            List<String> setmealNames =memberService.findAllsetmealNames();
            //封装饼状图中的区域
            List<Map<String,Object>> setmealCount =memberService.findAllSetmealCount();

            data.put("setmealNames",setmealNames);
            data.put("setmealCount",setmealCount);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @Reference
    private ReportService reportService;

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            //分析数据格式
            Map<String,Object> reportData = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

//    /**
//     * 导出运营数据统计分析报表
//     * @return
//     */
//    @RequestMapping("/exportBusinessReport")
//    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
//        try {
//            //1.获取报表中的参数
//            Map<String,Object> map = reportService.getBusinessReportData();
//            //取出返回结果数据，准备将报表数据写入到Excel文件中
//            String reportDate = (String) map.get("reportDate");
//            Integer todayNewMember = (Integer) map.get("todayNewMember");
//            Integer totalMember = (Integer) map.get("totalMember");
//            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
//            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
//            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
//            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
//            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
//            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
//            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
//            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
//            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");
//
//            //2.获取报表模板绝对路径
//            //File.separator 表示"/"或者"\"
//            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
//            XSSFWorkbook excel = new XSSFWorkbook(filePath);
//            //读取第一个工作表
//            XSSFSheet sheet = excel.getSheetAt(0);
//
//            //3.将数据写入模板中
//            XSSFRow row = sheet.getRow(2);
//            row.getCell(5).setCellValue(reportDate);//日期
//
//            row = sheet.getRow(4);
//            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
//            row.getCell(7).setCellValue(totalMember);//总会员数
//
//            row = sheet.getRow(5);
//            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
//            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数
//
//            row = sheet.getRow(7);
//            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
//            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数
//
//            row = sheet.getRow(8);
//            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
//            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数
//
//            row = sheet.getRow(9);
//            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
//            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
//
//            int rowNum = 12;
//            for(Map setmealMap : hotSetmeal){//热门套餐
//                String name = (String) setmealMap.get("name");
//                Long setmeal_count = (Long) setmealMap.get("setmeal_count");
//                BigDecimal proportion = (BigDecimal) setmealMap.get("proportion");
//                row = sheet.getRow(rowNum ++);
//                row.getCell(4).setCellValue(name);//套餐名称
//                row.getCell(5).setCellValue(setmeal_count);//预约数量
//                row.getCell(6).setCellValue(proportion.doubleValue());//占比
//            }
//
//            //4.使用输出流进行表格下载,基于浏览器作为的客户端下载,所以用reponse获取文件输出流
//            OutputStream outputStream = response.getOutputStream();
//            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
//            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
//            excel.write(outputStream);
//
//            outputStream.flush();
//            outputStream.close();
//            excel.close();
//
//            return null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
//        }
//    }

}
