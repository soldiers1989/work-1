package com.zz.service.br.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.br.FaceCompServ;

@Service
public class FacesInsertImpl implements FaceCompServ {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    private static final Logger logger = Logger.getLogger(FacesInsertImpl.class);

    @Override
    public int insert_faces(Map<String, String> map) {
        int i = 0;
        if (map == null) {
            logger.error("传入参数为空!");
        } else {
            try {
                logger.info("人脸识别对比服务正在入库.....");
                i = myBatisSessionTemplate.insert("orm.mapper.zz.br.FaceCompMapper.insert", map);
            } catch (MybatisDataAccessException e) {
                logger.error("人脸识别对比服务入库异常:" + e);
            }
        }

        return i;
    }


    public void insertS(Map<String,String> map) {
        myBatisSessionTemplate.insert("orm.mapper.zz.br.FaceCompMapper.insertS", map);
    }

    public void updateS(Map<String,String> map) {
        myBatisSessionTemplate.update("orm.mapper.zz.br.FaceCompMapper.updateS", map);
    }
}
