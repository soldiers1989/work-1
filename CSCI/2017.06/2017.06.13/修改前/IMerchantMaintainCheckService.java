package com.cf.biz.service.zz;

import java.util.Map;

import com.cfibatis.impl.data.Page;

/**
 * 
 * @ClassName: IMerchantMaintainCheckService
 * @Description: TODO(合作机构信息审核)
 * @author ly
 * @date 2017年6月13日 上午11:58:40
 *
 */
public interface IMerchantMaintainCheckService {

    /**
     * 分页查询合作机构
     * 
     * @param params
     * @return
     */
    public Page<Map<String, String>> queryMerchant(Map<String, String> params) throws Exception;
    /**
     * 合作机构信息审核(成功):
     * 
     * @return
     */
    public Result<T> succRemittance(Map<String,String> params) throws Exception;
    /**
     * 合作机构信息审核(失败):
     * 
     * @return
     */
    public Result<T> loseRemittance(Map<String,String> params) throws Exception;
}
