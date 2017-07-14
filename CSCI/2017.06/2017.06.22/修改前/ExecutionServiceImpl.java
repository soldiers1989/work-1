package com.zz.service.br.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.br.ExecutionService;

import jxl.common.Logger;

/**
 * 法院被执行人——— 个人操作类
 */
@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger logger = Logger.getLogger(ExecutionServiceImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public int insertExecution(Map<String, String> parmes) {
        int i = 0;
        try {
            logger.info("法院被执行人——— 个人操作正在添加数据....");
            i = myBatisSessionTemplate.insert("orm.mapper.zz.br.ExecutionMapper.insert", parmes);
            if (i <= 0) {
                logger.error("法院被执行人——— 个人操作正在添加数据失败");
                return 0;
            }
        } catch (MybatisDataAccessException e) {
            // TODO Auto-generated catch block
            logger.error("法院被执行人——— 个人异常" + e);
        }
        return i;
    }

}
