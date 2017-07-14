package com.zz.service.lt.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.lt.UserWhiteService;

/**
 * 
 * @ClassName: UserWhiteServiceImpl
 * @Description: TODO(工作地验证)
 * @author ly
 * @date 2017年6月14日 下午2:38:27
 *
 */
@Service("UserWhiteService")
public class UserWhiteServiceImpl implements UserWhiteService {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    public void insert(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.lt.UserWhiteMapper.insert", params);
    }
}