package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 持久层Dao接口
 */
public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);

    Page<CheckGroup> selectByCondition(String queryString);
    long selectGroupAndSetmeal(Integer id);
    void deleteGroupById(Integer id);


    CheckGroup findById(Integer id);

    void updateGroup(CheckGroup checkGroup);

    void addGroupAndItem(@Param("checkgroup_id") int id, @Param("checkitem_id") int id2);

    List<Integer> findItemByGroupIds(Integer id);

    void deleteGroupAndItemByGroupId(Integer id);

}
