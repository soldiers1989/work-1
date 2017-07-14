package com.cf.biz.service.zz.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.biz.service.zz.BizTranDetailService;
import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.data.Page;
import com.cfibatis.impl.template.MyBatisSessionTemplate;

@Service("bizTranDetailService")
public class TbMerchantDetailServiceImpl implements BizTranDetailService {

    private static final Logger logger = Logger.getLogger(TbMerchantDetailServiceImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public Page<Map<String, String>> queryTrans(HashMap<String, String> map) {
        Page<Map<String, String>> list = null;
        try {
            list = myBatisSessionTemplate.selectPage("orm.zz.TbMerchantDetailMapper.selectSeries", map);
        } catch (MybatisDataAccessException e) {
            logger.error("获取数据异常" + e);
        }
        return list;
    }

}
