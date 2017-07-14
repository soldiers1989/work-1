package com.cf.biz.service.zz;

import java.util.HashMap;
import java.util.Map;

import com.cfibatis.impl.data.Page;

/**
 * 
 * @ClassName: TbMerchantDetailService
 * @Description: TODO(合作机构维护)
 * @author ly
 * @date 2017年6月8日 下午5:08:21
 *
 */
public interface TbMerchantDetailService {

    /**
     * 分页查询合作机构
     * @param params
     * @return
     */
    public Page<Map<String, String>> queryMerchant(HashMap<String, String> params);

    /**
     * 删除合作机构记录
     * @param params
     * @return
     */
    Result<T> delMerchant(Map<String, String> params)throws Exception;

}