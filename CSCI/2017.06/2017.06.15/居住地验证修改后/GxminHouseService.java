package com.zz.service.lt;

import java.util.Map;

/**
 * 
 * @ClassName: GxminHouseService
 * @Description: TODO(居住地验证)
 * @author ly
 * @date 2017年6月15日 下午5:14:09
 *
 */
public interface GxminHouseService {

    /**
     * 
     * @Title: insert_GxminHouse
     * @Description: TODO(居住地验证信息插入GxminHouse)
     * @author ly
     * @date 2017年6月15日 下午5:14:29
     * @param params
     */
    public void insert_GxminHouse(Map<String, String> params);

    /**
     * 
     * @Title: insert_GxminHouse_S
     * @Description: TODO(居住地验证信息插入GxminHouse_S)
     * @author ly
     * @date 2017年6月15日 下午5:14:54
     * @param params
     */
    public void insert_GxminHouse_S(Map<String, String> params);

    /**
     * 
     * @Title: update_GxminHouse_S
     * @Description: TODO(更新GxminHouse_S)
     * @author ly
     * @date 2017年6月15日 下午5:15:10
     * @param params
     */
    public void update_GxminHouse_S(Map<String, String> params);
}
