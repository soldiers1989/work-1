package com.zz.service.kyc;

import java.util.Map;

/**
 * 
 * @ClassName: PopulationTagService
 * @Description: 人口属性标签查询服务
 * @author ly
 * @date 2017年6月21日 上午11:04:29
 *
 */
public interface PopulationTagService {

    public void insert(Map<String, String> params);

    public void insertCD(Map<String, String> params);
}
