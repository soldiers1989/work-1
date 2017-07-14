package com.zz.service.br.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.br.TelStateCTCCyService;

/**
 * 
 * @ClassName: TelStateCTCCyServiceImpl
 * @Description: 电信手机在网状态操作类
 * @author wyj
 * @date 2017年4月22日 下午6:33:59
 *
 */
@Service("telStateCTCCyService")
public class TelStateCTCCyServiceImpl implements TelStateCTCCyService {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public void insert(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.br.TelStateCTCCyMapper.insert", params);
    }

    @Override
    public void insertData(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.br.TelStateCTCCyDataMapper.insert", params);

    }

    @Override
    public void insertS(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.br.TelStateCTCCyMapper.insertS", params);
    }
    @Override
    public void updateS(Map<String, String> params) {
        myBatisSessionTemplate.update("orm.mapper.zz.br.TelStateCTCCyMapper.updateS", params);
    }
}
