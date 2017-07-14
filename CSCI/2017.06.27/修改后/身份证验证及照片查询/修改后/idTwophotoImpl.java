package com.zz.service.br.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.br.IdTwophotoServ;

@Service
public class idTwophotoImpl implements IdTwophotoServ {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    private static final Logger logger = Logger.getLogger(IdtwoImpl.class);

    @Override
    public int insert_IdTwophoto(Map<String, String> map) {
        int i = 0;
        if (map == null) {
            logger.error("传入参数为空!");
        } else {
            try {
                logger.info("身份证验证及照片正在入库.....");
                i = myBatisSessionTemplate.insert("orm.mapper.zz.br.idTwophotoMapper.insert", map);
            } catch (MybatisDataAccessException e) {
                logger.error("身份证验证及照片入库异常:" + e);
            }
        }

        return i;
    }

    @Override
    public void insertS(Map<String, String> map) {
        myBatisSessionTemplate.insert("orm.mapper.zz.br.idTwophotoMapper.insertS", map);
    }

    @Override
    public void updateS(Map<String, String> map) {
        myBatisSessionTemplate.update("orm.mapper.zz.br.idTwophotoMapper.updateS", map);
    }

}
