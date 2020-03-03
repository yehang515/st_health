package com.itheima.dao;

import com.itheima.pojo.Member;

public interface MemberDao {
    Member findByTel(String telephone);

    void add(Member member);

    Integer findMemberCountBeforeDate(String formatMonth);

    Integer getCountByRegtime(String today);

    Integer getTotalMember();

    Integer getThisWeekNewMember(String monday);

    Integer getThisMonthNewMember(String firstDayOfMonth);
}
