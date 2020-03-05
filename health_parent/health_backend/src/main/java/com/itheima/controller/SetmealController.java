package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entify.PageResult;
import com.itheima.util.QiniuUtils;
import com.itheima.entify.QueryPageBean;
import com.itheima.entify.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    //获取服务
    @Reference
    private SetmealService setmealService;

    @Resource(name = "jedisPool")
    private JedisPool jedisPool;


    @RequestMapping("/checkgroupList.do")
    public Result checkgroupList(){
        Setmeal setmeal = null;
        try {
            setmeal = setmealService.findGrouplist();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL,setmeal);
        }
    }

    @PreAuthorize("hasAuthority('SETMEAL_ADD')")//权限校验
    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.insertOne(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }

    }

    @RequestMapping("/findAll.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
//        System.out.println(pageResult);
        return pageResult;
    }

    /**
     * 表单文件上传
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result uploadFile(@RequestParam("imgFile") MultipartFile imgFile){//根据前端传回的文件名
        String originalFilename = imgFile.getOriginalFilename();//获取到前端上传文件的原始文件名
        int index = originalFilename.lastIndexOf(".");//截取原始文件的后缀名
        String extention = originalFilename.substring(index - 1);//截取到后缀名
        String fileName = UUID.randomUUID().toString()+extention;//拼接文件名
//        将文件上传到七牛云服务器
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            //文件上传成功之后将文件名称存储到setmealPicResources缓存当中,基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")//权限校验
    @RequestMapping("/deleteOne.do")
    public Result deleteOne(Integer id){
      return null;
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        return null;
    }

    @RequestMapping("/findByCheckGroupIds.do")
    public Result findByCheckItemIds(Integer id){
        return null;
    }

    @RequestMapping("/update.do")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        return null;
    }
}
