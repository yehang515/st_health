package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //根据REdis中保存的两个集合计算差值,获取垃圾图片差值集合,sdiff获取两个集合差值
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //然后根据集合中的图片名称删除七牛云服务器的图片
        if(sdiff!=null && sdiff.size()>0){
            for (String imgName : sdiff) {
                //删除七牛云中的垃圾图片
                QiniuUtils.deleteFileFromQiniu(imgName);
                //删除Redis中的垃圾图片
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
                System.out.println("删除图片"+imgName);
            }
        }
    }
}
