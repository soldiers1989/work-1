package com.zz.service.br;

import java.util.Map;

/**
 * 
 * @ClassName: TelStateCTCCyService
 * @Description: 电信手机在网状态接口类
 * @author wyj
 * @date 2017年4月22日 下午6:33:33
 *
 */
public interface TelStateCTCCyService {

    /**
     * 
     * @Title: insert
     * @Description: 新增
     * @author wyj
     * @date 2017年4月22日 下午4:50:22
     * @param params
     */
    public void insert(Map<String, String> params);

    /**
     * 
     * @Title: insertData
     * @Description: 添加子表
     * @author wyj
     * @date 2017年4月22日 下午6:55:28
     * @param params
     */
    public void insertData(Map<String, String> params);

    public void insertS(Map<String, String> params);

    public void updateS(Map<String, String> params);

}
