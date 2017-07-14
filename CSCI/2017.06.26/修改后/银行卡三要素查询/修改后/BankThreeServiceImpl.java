package com.zz.service.br.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.br.BankThreeService;

import jxl.common.Logger;

/**
 * 
 * @ClassName: BankThreeServiceImpl
 * @Description: 银行卡三要素查询操作类
 * @author ly
 * @date 2017年6月26日 下午2:55:12
 *
 */
@Service
public class BankThreeServiceImpl implements BankThreeService {

    private static final Logger logger = Logger.getLogger(BankThreeServiceImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public int insert(Map<String, String> parmes) {
        int i = 0;
        try {
            logger.info("银行卡三要素查询操作正在添加数据....");
            i = myBatisSessionTemplate.insert("orm.mapper.zz.br.BankThreeMapper.insert", parmes);
            if (i <= 0) {
                logger.error("银行卡三要素查询操作正在添加数据失败");
                return 0;
            }
        } catch (MybatisDataAccessException e) {
            // TODO Auto-generated catch block
            logger.error("银行卡三要素查询异常" + e);
        }
        return i;
    }

    @Override
    public void insertS(Map<String, String> parmes) {
        myBatisSessionTemplate.insert("orm.mapper.zz.br.BankThreeMapper.insertS", parmes);
    }

    @Override
    public void updateS(Map<String, String> parmes) {
        myBatisSessionTemplate.update("orm.mapper.zz.br.BankThreeMapper.updateS", parmes);
    }
}
