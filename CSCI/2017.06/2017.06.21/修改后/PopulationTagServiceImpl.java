package com.zz.service.kyc.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.kyc.PopulationTagService;

/**
 * 
 * @ClassName: PopulationTagServiceImpl
 * @Description: 人口属性标签查询服务
 * @author ly
 * @date 2017年6月21日 上午11:07:47
 *
 */
@Service("populationTagService")
public class PopulationTagServiceImpl implements PopulationTagService {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public void insert(Map<String, String> params) {
        // TODO Auto-generated method stub
        myBatisSessionTemplate.insert("orm.mapper.zz.kyc.PopulationTagMapper.insert", params);
    }

    @Override
    public void insertCD(Map<String, String> params) {
        // TODO Auto-generated method stub
        myBatisSessionTemplate.insert("orm.mapper.zz.kyc.PopulationTagMapper.insertCD", params);
    }
}
