package com.zz.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.https.HttpsUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/ltCtl")
@SuppressWarnings("static-access")
public class LTCtl {

    private static final Logger logger = Logger.getLogger(LTCtl.class);

    @Value("${LTServiceUrl}")
    private String LTServiceUrl;

    private HttpsUtil httpsUtil = new HttpsUtil();

    @RequestMapping(value = "/Residence", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getResidencePre(HttpServletRequest request) {
        String portrait_result = "";

        return portrait_result;
    }

    /**
     * 
     * @Title: getGxminWork
     * @Description: TODO(工作地验证)
     * @author ly
     * @date 2017年6月14日 下午2:45:14
     * @param request
     * @param sendTelNo
     * @param longitude
     * @param latitude
     * @param merchantId
     * @return
     */
    @RequestMapping(value = "/GxminWork", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getGxminWork(HttpServletRequest request, String sendTelNo, String longitude, String latitude, String merchantId) {
        String portrait_result = "";
        try {
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("sendTelNo", sendTelNo);
            paramData.put("longitude", longitude);
            paramData.put("latitude", latitude);
            // 将json格式的对象转换成字符串 faith_GxminWork
            portrait_result = httpsUtil.postMsg(LTServiceUrl + "GxminWork", "paramData=" + paramData.toString());
            logger.info("getGxminWork_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getGxminWork_ERROR 输出--->:" + e);
        }
        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: getUserWhite
     * @Description: TODO(白名单查询)
     * @author ly
     * @date 2017年6月15日 下午2:44:24
     * @param request
     * @param sendTelNo
     * @param merchantId
     * @return
     */
    @RequestMapping(value = "/UserWhite", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserWhite(HttpServletRequest request, String sendTelNo, String merchantId) {
        String portrait_result = "";
        try {
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("sendTelNo", sendTelNo);
            // 将json格式的对象转换成字符串 faith_UserWhite
            portrait_result = httpsUtil.postMsg(LTServiceUrl + "UserWhite", "paramData=" + paramData.toString());
            logger.info("getUserWhite_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getUserWhite_ERROR 输出--->:" + e);
        }
        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: getGxminHouse
     * @Description: TODO(居住地验证)
     * @author ly
     * @date 2017年6月15日 下午2:44:24
     * @param request
     * @param sendTelNo
     * @param merchantId
     * @return
     */
    @RequestMapping(value = "/GxminHouse", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getGxminHouse(HttpServletRequest request, String sendTelNo, String longitude, String latitude, String merchantId) {
        String portrait_result = "";
        try {
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("sendTelNo", sendTelNo);
            paramData.put("longitude", longitude);
            paramData.put("latitude", latitude);
            // 将json格式的对象转换成字符串 faith_GxminHouse
            portrait_result = httpsUtil.postMsg(LTServiceUrl + "GxminHouse", "paramData=" + paramData.toString());
            logger.info("getGxminHouse_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getGxminHouse_ERROR 输出--->:" + e);
        }
        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }
}
