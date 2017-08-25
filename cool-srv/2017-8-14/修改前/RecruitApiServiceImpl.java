package com.cf.consoleApi.service.impl;

import com.cf.consoleApi.constants.Constants;
import com.cf.consoleApi.model.RecruitVo;
import com.cf.consoleApi.service.IRecruitApiService;
import com.cf.consoleApi.util.BaseMethod;
import com.cf.mybatis.impl.template.MyBatisSessionTemplate;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huizi
 * @date 2017/7/14 0014 下午 3:55
 * @since 1.0
 */
@Service
public class RecruitApiServiceImpl extends BaseMethod implements IRecruitApiService {

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public List<RecruitVo> listRecruit(HashMap<String, String> params) {
        List<RecruitVo> recruitVos = new ArrayList<>();
        List list = new ArrayList();
        RecruitVo recruitVo = new RecruitVo();
        HashMap<String, String> banner = super.getParams(params, Constants.RECRUIT_MODULE_ONE);
        recruitVo.setBanner(super.getBanner(banner));
        HashMap<String, String> description = super.getParams(params, Constants.RECRUIT_MODULE_TWO);
        List<Map<String, String>> recruitDescription = this.myBatisSessionTemplate.selectList("orm.api.description.queryDescription", description);
        recruitVo.setRecruitDescription(recruitDescription);
        List<Map<String, String>> recruit = this.myBatisSessionTemplate.selectList("orm.api.recruit.listRecruit",params);
        for(Map m:recruit){
            list.add( m.get("JOB_DESC"));
        }
        String key = "";
        String[] job = {};
        for(int i = 0 ; i < list.size() ; i++) {
          Map<String, String> tmpMap = recruit.get(i);
          String s = (String) list.get(i);
          job = s.replace("\r\n","").split("<br>");
          for (int j =0;j<job.length;j++){
              key = "JOB_DESC"+j;
              String decjob = job[j];
              tmpMap.put(key,decjob);
          }
          recruit.set(i,tmpMap);
        }
        recruitVo.setRecruit(recruit);
        recruitVos.add(recruitVo);
        return recruitVos;
    }
}
