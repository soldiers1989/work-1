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
import com.cf.biz.service.zz.TbMerchantDetailService;
import com.cf.common.base.BaseSupport;
import com.cfibatis.exception.MybatisDataAccessException;
import com.cfibatis.impl.data.Page;
import com.cfibatis.impl.template.MyBatisSessionTemplate;

@Service("tbMerchantDetailService")
public class TbMerchantDetailServiceImpl implements TbMerchantDetailService {

    private static final Logger logger = Logger.getLogger(TbMerchantDetailServiceImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    @Autowired
    private IRollBackService iRollBackService;

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
    public Result<T> saveMerchant(Map<String, String> params, List list) throws Exception {
        Result<T> rs = new Result<T>();
        Integer count = (Integer) this.myBatisSessionTemplate.selectOne("orm.zz.tbMerchantTempMapper.MerchanttempByMerchant_id", params);
        if (count > 0) {
            rs.setSuccess(false);
            rs.setRetMessage("该记录正在进行审核");
            return rs;
        }
        // 转换布尔型
        // String isENABLED = params.get("ENABLED");
        // if ("true".equalsIgnoreCase(isENABLED)) {
        // params.put("ENABLED", "1");
        // } else {
        // params.put("ENABLED", "0");
        // }
        String saveFlag = params.get("saveFlag");
        int ret = -1;
        int save = -1;
        if ("mod".equals(saveFlag)) {
            Map<String, String> map = (Map<String, String>) this.myBatisSessionTemplate.selectOne("orm.zz.tbMerchantDetailMapper.queryMerchantByMerchant_ID", params);
            map.put("OP_FLAG", "修改");
            try {
                ret = iRollBackService.modMerchant(map, params, list);
                save = 1;
            } catch (Exception e) {
                logger.error("saveRole-->" + e);
                rs.setSuccess(false);
                rs.setRetMessage("审核失败，数据库操作异常");
                return rs;
            }
        }
        if ("add".equals(saveFlag)) {
            Integer merchantCount = (Integer) this.myBatisSessionTemplate.selectOne("orm.zz.tbMerchantDetailMapper.selectMerchantCount", params);
            if (merchantCount == 0) {
                params.put("OP_FLAG", "增加");
                save = iRollBackService.addMerchant(params);
                ret = 1;
            } else {
                rs.setRetMessage("合作机构代码已存在，不能新增！");
                rs.setSuccess(false);
                return rs;
            }
        }
        if (ret > 0 && save > 0) {// 库操作成功
            rs.setSuccess(true);
        } else {
            rs.setRetMessage("保存失败！");
            rs.setSuccess(false);
        }
        return rs;
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
        Integer count = (Integer) this.myBatisSessionTemplate.selectOne("orm.zz.tbMerchantTempMapper.MerchanttempByMerchant_id", params);
        if (count > 0) {
            rs.setSuccess(false);
            rs.setRetMessage("该记录正在进行审核");
            return rs;
        }
        Map<String, String> map = (Map<String, String>) this.myBatisSessionTemplate.selectOne("orm.zz.tbMerchantDetailMapper.queryMerchantByMerchant_ID", params);
        try {
            map.put("OP_FLAG", "删除");
            BaseSupport.myBatisSessionTemplate.insert("orm.zz.tbMerchantTempMapper.CopyMerchant", map);
            rs.setSuccess(true);
            rs.setRetMessage("请求已提交，请等待审核");
        } catch (Exception e) {
            logger.error("delMerchant-->" + e);
            rs.setSuccess(false);
            rs.setRetMessage("删除合作机构失败！");
        }
        return rs;
    }

}
