package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private OrderDao orderDao;


    public Member findByTelephone(String telephone) {
        Member member = memberDao.findByTel(telephone);
        return member;
    }


    public void add(Member member) {
        //判断用户是否在其他表单页面注册的时候是否提供密码，如果有明文密码，对其加密
        String password = member.getPassword();
        if(password != null){
             password = MD5Utils.md5(password);
             member.setPassword(password);
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findCountByMonths(List<String> months) {
        List<Integer> list = new ArrayList();
        for (String month : months) {
            //获取当月以前的所有会员数量
            String formatMonth = month + ".31";
            Integer memberCount = memberDao.findMemberCountBeforeDate(formatMonth);
            list.add(memberCount);
        }
        return list;
    }

    @Override
    public List<String> findAllsetmealNames() {
        List<String> setmealNames = setmealDao.findAllsetmealNames();
        return setmealNames;
    }

    @Override
    public List<Map<String, Object>> findAllSetmealCount() {
        List<Map<String,Object>> setmealCount  = orderDao.findAllSetmealCount();
        return setmealCount;
    }
}
