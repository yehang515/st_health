package com.itheima.service;

import com.itheima.pojo.Member;
import java.util.List;
import java.util.Map;

public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findCountByMonths(List<String> months);

    List<String> findAllsetmealNames();

    List<Map<String, Object>> findAllSetmealCount();
}
