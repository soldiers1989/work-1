package com.zz.service.lt.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.zz.service.lt.GxminWorkService;

/**
 * 
 * @ClassName: GxminWorkServiceImpl
 * @Description: TODO(工作地验证)
 * @author ly
 * @date 2017年6月14日 下午2:38:27
 *
 */
@Service("gxminWorkService")
public class GxminWorkServiceImpl implements GxminWorkService {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    public void insert_GxminWork(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.lt.GxminWorkMapper.insertGxminWork", params);
    }

    public void insert_GxminWork_S(Map<String, String> params) {
        myBatisSessionTemplate.insert("orm.mapper.zz.lt.GxminWorkMapper.insertGxminWorkS", params);
    }

    public void update_GxminWork_S(Map<String, String> params) {
        myBatisSessionTemplate.update("orm.mapper.zz.lt.GxminWorkMapper.updateGxminWorkS", params);
    }
}