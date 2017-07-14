package com.cf.biz.service.zz.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.biz.domain.Result;
import com.cf.biz.service.zz.TbMerchantDetailService;
import com.cf.cfsecurity.domain.TbRole;
import com.cf.common.base.BaseSupport;
import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.data.Page;
import com.cfibatis.impl.template.MyBatisSessionTemplate;

@Service("tbMerchantDetailService")
public class TbMerchantDetailServiceImpl implements TbMerchantDetailService {

    private static final Logger logger = Logger.getLogger(TbMerchantDetailServiceImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Override
    public Page<Map<String, String>> queryMerchant(HashMap<String, String> map) {
        Page<Map<String, String>> list = null;
        try {
            list = myBatisSessionTemplate.selectPage("orm.zz.tbMerchantDetailMapper.selectSeries", map);
        } catch (MybatisDataAccessException e) {
            logger.error("获取数据异常" + e);
        }
        return list;
    }

    @Override
    public Result<T> delMerchant(Map<String, String> params) throws Exception {
        Result<T> rs = new Result<T>();
        // Integer merchantCount = (Integer)
        // myBatisSessionTemplate.selectOne("orm.zz.tbMerchantDetailMapper.queryUsedRole",
        // params);
        // if (merchantCount > 0) {
        // rs.setRetMessage("角色还在使用中，不能删除！");
        // rs.setSuccess(false);
        // return rs;
        // }
        Integer count = (Integer) this.myBatisSessionTemplate.selectOne("orm.sys.tbMerchantTemp.MerchanttempByMerchant_id", params);
        if (count > 0) {
            rs.setSuccess(false);
            rs.setRetMessage("该记录正在进行审核");
            return rs;
        }
        TbRole tbRole = (TbRole) this.myBatisSessionTemplate.selectOne("orm.sys.tbRole.queryRoleByROLE_ID", params);
        try {
            tbRole.setOP_FLAG("删除");
            BaseSupport.myBatisSessionTemplate.insert("orm.sys.tbRoleTemp.CopyRole", tbRole);
            rs.setSuccess(true);
            rs.setRetMessage("请求已提交，请等待审核");
        } catch (Exception e) {
            logger.error("delMerchant-->" + e);
            rs.setSuccess(false);
            rs.setRetMessage("删除角色失败！");
        }
        return rs;
    }
}
