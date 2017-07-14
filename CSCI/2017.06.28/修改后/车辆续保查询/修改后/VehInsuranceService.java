package com.zz.service.bc;

import java.util.Map;

/**
 * 车辆续保信息查询
 * @author Administrator
 *
 */
public interface VehInsuranceService {
    /**
     * 
     * @Title: insert
     * @Description: 新增
     * @author ly
     * @date 2017年6月2日 16:47:05
     * @param params
     */
    public void insert(Map<String, String> params);

    public void insertS(Map<String, String> params);

    public void updateS(Map<String, String> params);
}
