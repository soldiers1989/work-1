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
import com.cf.biz.service.zz.TbMerchantDetailService;
import com.cf.common.utils.http.UHttpServlet;
import com.cfibatis.impl.data.Page;

/**
 * 
 * @ClassName: tbMerchantDetailController
 * @Description: TODO(合作机构维护)
 * @author ly
 * @date 2017年6月7日 下午5:53:49
 *
 */
@Controller
public class tbMerchantDetailController {

    private static final Logger logger = Logger.getLogger(tbMerchantDetailController.class);

    private String retMessage;

    private boolean success;

    private int total;

    private List<Map<String, String>> lstoreList;

    private Result<T> result = new Result<T>();

    @Autowired
    private TbMerchantDetailService tbMerchantDetailService;

    @RequestMapping(value = "/merchantDetail", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Result<T> queryLogin(HttpServletRequest request) {
        try {
            HashMap<String, String> params = UHttpServlet.GetRequestParameters(request);
            Page<Map<String, String>> storeList = tbMerchantDetailService.queryMerchant(params);// pageobj.getResult();
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
}
