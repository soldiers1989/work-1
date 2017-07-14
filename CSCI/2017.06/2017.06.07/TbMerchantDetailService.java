package com.cf.biz.service.zz;

import java.util.HashMap;
import java.util.Map;

import com.cfibatis.impl.data.Page;

public interface TbMerchantDetailService {
    public Page<Map<String, String>> queryMerchant(HashMap<String, String> params);
}
