package com.zz.service.br.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.br.ConsumptioncService;

import jxl.common.Logger;

/**
 * 商品消费评估操作类
 */
@Service
public class ConsumptioncServiceImpl implements ConsumptioncService {

    private static final Logger logger = Logger.getLogger(ExecutionServiceImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public int intsertConsumptionc(Map<String, String> parmes) {
        int i = 0;
        try {
            logger.info("商品消费评估操作正在添加数据....");
            i = myBatisSessionTemplate.insert("orm.mapper.zz.br.ConsumptioncMapper.insert", parmes);
            if (i <= 0) {
                logger.error("商品消费评估添加数据失败");
                return 0;
            }
        } catch (MybatisDataAccessException e) {
            // TODO Auto-generated catch block
            logger.error("商品消费评估异常" + e);
        }
        return i;
    }
}
