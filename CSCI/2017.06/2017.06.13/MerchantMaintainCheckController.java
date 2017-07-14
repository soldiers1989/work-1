package com.cf.biz.controller.zz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.biz.domain.Result;
import com.cf.biz.service.zz.IMerchantMaintainCheckService;
import com.cf.common.utils.http.UHttpServlet;
import com.cfibatis.impl.data.Page;

/**
 * 
 * @ClassName: MerchantMaintainCheckController
 * @Description: TODO(合作机构维护)
 * @author ly
 * @date 2017年6月7日 下午5:53:49
 *
 */
@Controller
public class MerchantMaintainCheckController {

    private static final Logger logger = Logger.getLogger(MerchantMaintainCheckController.class);

    private String retMessage;

    private boolean success;

    private int total;

    private List<Map<String, String>> lstoreList;

    private Result<T> result = new Result<T>();

    @Autowired
    private IMerchantMaintainCheckService merchantMaintainCheckService;

    /**
     * 
     * @Title: queryLogin
     * @Description: TODO(查询待审核的合作机构信息)
     * @author ly
     * @date 2017年6月13日 下午5:23:50
     * @param request
     * @return
     */
    @RequestMapping(value = "/merchantMaintainCheckAction!queryMerchant", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Result<T> queryLogin(HttpServletRequest request) {
        try {
            HashMap<String, String> params = UHttpServlet.GetRequestParameters(request);
            Page<Map<String, String>> storeList = merchantMaintainCheckService.queryMerchant(params);// pageobj.getResult();
            lstoreList = storeList.getResult();
            total = storeList.getTotalCount();
            success = true;
            if (lstoreList.size() > 0) {
                retMessage = "查询成功";
            } else {
                retMessage = "查询无记录";
            }
            result.setDataList(lstoreList);
        } catch (Exception e) {
            logger.error("TbMerchantDetail-->" + e);
            retMessage = "查询失败-服务器异常";
            success = false;
        }
        result.setTotal(total);
        result.setRetMessage(retMessage);
        result.setSuccess(success);
        return result;
    }

    /**
     * 审核合作机构（成功）
     * 
     * @author ly
     */
    @RequestMapping(value = "/merchantMaintainCheckAction!auditSuccess", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Result<T> auditSuccess(HttpServletRequest request) {
        try {
            HashMap<String, String> params = UHttpServlet.GetRequestParameters(request);
            Result<T> rs = merchantMaintainCheckService.succRemittance(params);
            if (rs.isSuccess()) {
                success = true;
                retMessage = "审核已成功";
            } else {
                retMessage = rs.getRetMessage();
                success = false;
            }
        } catch (Exception e) {
            logger.error("merchantMaintainCheckAction!auditSuccess-->" + e);
            retMessage = "审核失败-服务器异常";
            success = false;
        }
        result.setRetMessage(retMessage);
        result.setSuccess(success);
        return result;
    }

    /**
     * 
     * @Title: auditFail
     * @Description: TODO(审核合作机构（失败）)
     * @author ly
     * @date 2017年6月13日 下午5:14:44
     * @param request
     * @return
     */
    @RequestMapping(value = "/merchantMaintainCheckAction!auditFail", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Result<T> auditFail(HttpServletRequest request) {
        try {
            HashMap<String, String> params = UHttpServlet.GetRequestParameters(request);
            Result<T> rs = merchantMaintainCheckService.loseRemittance(params);
            if (rs.isSuccess()) {
                retMessage = "审核已拒绝";
                success = true;
            } else {
                retMessage = rs.getRetMessage();
                success = false;
            }
        } catch (Exception e) {
            logger.error("merchantMaintainCheckAction!auditFail-->" + e);
            retMessage = "审核失败-服务器异常";
            success = false;
        }
        result.setRetMessage(retMessage);
        result.setSuccess(success);
        return result;
    }
}
