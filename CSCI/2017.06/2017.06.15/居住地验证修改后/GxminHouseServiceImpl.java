package com.zz.service.lt.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.lt.GxminHouseService;

/**
 * 
 * @ClassName: GxminHouseServiceImpl
 * @Description: TODO(居住地验证)
 * @author ly
 * @date 2017年6月15日 下午5:17:48
 *
 */
@Service("gxminHouseService")
public class GxminHouseServiceImpl implements GxminHouseService {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    public void insert_GxminHouse(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.lt.GxminHouseMapper.insertGxminHouse", params);
    }

    public void insert_GxminHouse_S(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.lt.GxminHouseMapper.insertGxminHouseS", params);
    }

    public void update_GxminHouse_S(Map<String, String> params) {
        myBatisSessionTemplate.update("orm.mapper.zz.lt.GxminHouseMapper.updateGxminHouseS", params);
    }
}