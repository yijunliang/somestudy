package com.yjl.dao;

import org.springframework.data.repository.CrudRepository;

import com.yjl.entity.UserInfo;

public interface UserInfoDao extends CrudRepository<UserInfo,Long> {
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username);
}