package com.zz.service.bc.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.bc.VehInsuranceService;

/**
 * 车辆续保信息查询
 */
@Service("vehInsuranceService")
public class VehInsuranceServiceImpl implements VehInsuranceService {
    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    public void insert(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.bc.BCVehInsuranceMapper.insert", params);
    }
}
