package com.zz.service.br.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.br.FaceRecogServ;

@Service
public class FaceRecogImpl implements FaceRecogServ {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    private static final Logger logger = Logger.getLogger(FaceRecogImpl.class);

    @Override
    public int insert_facerecog(Map<String, String> map) {
        int i = 0;
        if (map == null) {
            logger.error("传入参数为空!");
        } else {
            try {
                logger.info("身份证人脸识别验证正在入库.....");
                i = myBatisSessionTemplate.insert("orm.mapper.zz.br.FaceRecogMapper.insert", map);
            } catch (MybatisDataAccessException e) {
                logger.error("身份证人脸识别验证入库异常:" + e);
            }
        }

        return i;
    }

    @Override
    public void insertS(Map<String,String> map) {
        myBatisSessionTemplate.insert("orm.mapper.zz.br.FaceRecogMapper.insertS", map);
    }

    @Override
    public void updateS(Map<String,String> map) {
        myBatisSessionTemplate.update("orm.mapper.zz.br.FaceRecogMapper.updateS", map);
    }
}
