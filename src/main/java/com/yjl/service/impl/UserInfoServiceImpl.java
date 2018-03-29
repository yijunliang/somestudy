package com.yjl.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yjl.dao.UserInfoDao;
import com.yjl.entity.UserInfo;
import com.yjl.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("进入查找用户方法:UserInfoServiceImpl.findByUsername()");
        return userInfoDao.findByUsername(username);
    }
}