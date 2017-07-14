package com.cf.biz.service.zz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.biz.domain.Result;
import com.cf.biz.service.sys.IRollBackService;
import com.cf.biz.service.zz.IMerchantMaintainCheckService;
import com.cf.common.base.BaseSupport;
import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.data.Page;
import com.cfibatis.impl.template.MyBatisSessionTemplate;

@Service("IMerchantMaintainCheckService")
public class MerchantMaintainCheckServiceImpl implements IMerchantMaintainCheckService {

    private static final Logger logger = Logger.getLogger(MerchantMaintainCheckServiceImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Autowired
    private IRollBackService iRollBackService;

    /**
     * 合作机构信息审核查询:
     * 
     * @return
     */
    @Override
    public Page<Map<String, String>> queryMerchant(HashMap<String, String> map) {
        Page<Map<String, String>> list = null;
        try {
            list = myBatisSessionTemplate.selectPage("orm.zz.tbMerchantTempMapper.queryMerchantTemp", map);
        } catch (MybatisDataAccessException e) {
            logger.error("获取数据异常" + e);
        }
        return list;
    }

    /**
     * 合作机构信息审核成功
     * 
     * @return
     * @throws Exception
     */
    @Override
    public Result<T> succRemittance(Map<String, String> params) throws Exception {
        Result<T> rs = new Result<>();

        int ret = -1;

        try {
            Map<String, String> map = (Map<String, String>) this.myBatisSessionTemplate.selectOne("orm.zz.tbMerchantTempMapper.queryMerchanttempByROLE_ID", params);
            // tbrole.setOP_FLAG(params.get("OP_FLAG"));
            // ret = rollBackService.addRole(tbrole, params);
        } catch (Exception e) {
            logger.error("succRemittance-->" + e);
            throw e;
        }
        if (ret > 0) {// ret大于0 操作数据库成功
            rs.setSuccess(true);
        } else {
            rs.setSuccess(false);
            rs.setRetMessage("审核失败");
        }
        return rs;
    }

    /**
     * 合作机构信息审核失败
     * 
     * @return
     * @throws Exception
     */
    public Result<T> loseRemittance(Map<String, String> params) throws Exception {
        Result<T> rs = new Result<>();
        try {
            rollBackService.delMerchant(params);
            rs.setSuccess(true);
            return rs;
        } catch (Exception e) {
            logger.error("loseRemittance-->" + e);
            rs.setSuccess(false);
            rs.setRetMessage("审核失败");
            return rs;
        }

    }
}
