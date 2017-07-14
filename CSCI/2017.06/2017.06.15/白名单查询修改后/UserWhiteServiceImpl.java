package com.zz.service.lt.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.lt.UserWhiteService;

/**
 * 
 * @ClassName: UserWhiteServiceImpl
 * @Description: TODO(白名单查询)
 * @author ly
 * @date 2017年6月15日 下午2:49:57
 *
 */
@Service("userWhiteService")
public class UserWhiteServiceImpl implements UserWhiteService {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    public void insert_UserWhite(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.lt.UserWhiteMapper.insertUserWhite", params);
    }

    public void insert_UserWhite_S(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.lt.UserWhiteMapper.insertUserWhiteS", params);
    }

    public void update_UserWhite_S(Map<String, String> params) {
        myBatisSessionTemplate.update("orm.mapper.zz.lt.UserWhiteMapper.updateUserWhiteS", params);
    }
}