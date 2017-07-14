package com.zz.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONObject;

import com.cf.common.utils.json.JsonUtil;
import com.cf.common.utils.security.Util;
import com.utils.http.HttpsUtil;
import com.zz.service.TransDtlService;
import com.zz.service.lt.GxminWorkService;


@Controller
@RequestMapping("/lt")
@SuppressWarnings("static-access")
public class LTbizController {

    private static final Logger logger = Logger.getLogger(LTbizController.class);

    @Autowired
    private TransDtlService transDtlService;// 记录流水

    @Autowired
    private GxminWorkService gxminWorkService;// 车辆续保信息查询

    @Value("${postLTUrl}")
    private String postLTUrl;

    private static HttpsUtil httpsUtil = new HttpsUtil();

    @RequestMapping("/CommFuz")
    public String CommFuz() {
        try {
            httpsUtil.postMsg(postLTUrl, "");
        } catch (Exception e) {
            logger.error(e);
        }
        return "";
    }

    /**
     * 
     * @Title: GxminWork
     * @Description: 工作地验证
     * @author ly
     * @date 2017年6月14日 上午11:27:13
     * @param request
     * @return
     */

    @RequestMapping(value = "/GxminWork", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String GxminWork(HttpServletRequest request) {
        String portrait_result = "";
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            logger.info("正在执行工作地验证！。。。。");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别机构代码是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'code':700000 ,'msg':'机构代码错误' }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "GxminWork");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("工作地验证，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "ltGxminWork");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("工作地验证，插入请求数源流水失败！ ");
            }

            map.put("SID", raid);
            map.put("sendTelNo", jsonObject.getString("sendTelNo"));
            map.put("longitude", jsonObject.getString("longitude"));
            map.put("latitude", jsonObject.getString("latitude"));
            map.put("orgCode", jsonObject.getString("orgCode"));
            map.put("curTime", jsonObject.getString("curTime"));
            map.put("orgSeq", jsonObject.getString("orgSeq"));
            map.put("sequence", jsonObject.getString("sequence"));

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postLTUrl + "postLT_GxminWork", "paramData=" + jsonObjectTr.toString());
            // -----------json解析修改start------------
            map = (HashMap<String, String>) JsonUtil.getJsonMap(portrait_result, map, null);
            // 如果查询无数据或者查询失败则不入库
            if (!map.get("CODE").equals("600000")) {
                return portrait_result;
            }

            logger.info("getGxminWorkFaith_parmData 输出--->:" + portrait_result);
            // -----------json解析修改end------------
            gxminWorkService.insert(map);
            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys1 = { "flag", "flag_gxminWork" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys1, 2, flagmap);
                if (flagmap.get("FLAG_GXMINWORK").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("getGxminWorkFaith_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }

}
