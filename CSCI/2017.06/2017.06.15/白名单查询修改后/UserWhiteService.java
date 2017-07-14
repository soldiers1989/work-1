package com.zz.service.lt;

import java.util.Map;

/**
 * 
 * @ClassName: UserWhiteService
 * @Description: TODO(白名单查询)
 * @author ly
 * @date 2017年6月15日 下午2:47:28
 *
 */
public interface UserWhiteService {

    /**
     * 
     * @Title: insert_UserWhite
     * @Description: TODO(插入表UserWhite)
     * @author ly
     * @date 2017年6月15日 下午2:47:42
     * @param params
     */
    public void insert_UserWhite(Map<String, String> params);

    /**
     * 
     * @Title: insert_UserWhite_S
     * @Description: TODO(插入表UserWhite_S)
     * @author ly
     * @date 2017年6月15日 下午2:47:56
     * @param params
     */
    public void insert_UserWhite_S(Map<String, String> params);

    /**
     * 
     * @Title: update_UserWhite_S
     * @Description: TODO(更新表UserWhite)
     * @author ly
     * @date 2017年6月15日 下午2:48:17
     * @param params
     */
    public void update_UserWhite_S(Map<String, String> params);
}
