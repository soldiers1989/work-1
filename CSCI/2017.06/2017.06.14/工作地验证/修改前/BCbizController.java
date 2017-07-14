package com.zz.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.common.utils.json.JsonUtil;
import com.cf.common.utils.security.Util;
import com.utils.http.HttpsUtil;
import com.zz.service.TransDtlService;
import com.zz.service.bc.InquiryService;
import com.zz.service.bc.ResidenceService;
import com.zz.service.bc.VehInsuranceService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/bc")
@SuppressWarnings("static-access")
public class BCbizController {

    private static final Logger logger = Logger.getLogger(BCbizController.class);

    @Autowired
    private ResidenceService residenceService; // 查询个人户籍信息

    @Autowired
    private InquiryService inquiryService; // 房屋价格查询

    @Autowired
    private TransDtlService transDtlService;// 记录流水

    @Autowired
    private VehInsuranceService vehInsuranceService;// 车辆续保信息查询

    @Value("${postBCUrl}")
    private String postBCUrl;

    private static HttpsUtil httpsUtil = new HttpsUtil();

    /**
     * 
     * @Title: getResidenceFaith
     * @Description: 查询个人户籍信息
     * @author wyj
     * @date 2017年4月22日 下午2:57:07
     * @param request
     * @return
     */

    @RequestMapping(value = "/faith_Residence", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getResidenceFaith(HttpServletRequest request) {
        String portrait_result = "";
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            logger.info("正在执行个人户籍信息查询！。。。。");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("Residence", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("个人户籍信息查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("bcResidence", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("个人户籍信息查询，插入请求数源流水失败！ ");
            }

            map.put("SID", raid);
            map.put("ID", jsonObject.getString("id"));
            map.put("CELL", jsonObject.getString("cell"));
            map.put("NAME", jsonObject.getString("name"));

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postBCUrl + "postBC_Residence", "paramData=" + jsonObjectTr.toString());
            // -----------json解析修改start------------
            String[] keyRep = { "code;last", "api_status;last" };
            map = (HashMap<String, String>) JsonUtil.getJsonMap(portrait_result, map, keyRep);
            // 如果查询无数据或者查询失败则不入库
            if (!map.get("code").equals("600000")) {
                return portrait_result;
            }

            logger.info("getResidenceFaith_parmData 输出--->:" + portrait_result);
            // -----------json解析修改end------------
            residenceService.insert(map);
            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys1 = { "flag", "flag_residence" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys1, 2, flagmap);
                if (flagmap.get("FLAG_RESIDENCE").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }

        } catch (Exception e) {
            logger.error("getResidenceFaith_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }

    // /**
    // * 房屋价格查询
    // *
    // * @return
    // */
    // @RequestMapping("/Inquiry")
    // public String Inquiry() {
    // try {
    // httpsUtil.postMsg(postBCUrl, "");
    // } catch (Exception e) {
    // logger.error(e);
    // }
    // return "";
    // }

    /**
     * 小区信息模糊查询
     * 
     * @return
     */
    @RequestMapping("/CommFuz")
    public String CommFuz() {
        try {
            httpsUtil.postMsg(postBCUrl, "");
        } catch (Exception e) {
            logger.error(e);
        }
        return "";
    }

    /**
     * 车辆续保信息查询
     * 
     * @return
     */
    @RequestMapping("/VehInsurance")
    public String VehInsurance() {
        try {
            httpsUtil.postMsg(postBCUrl, "");
        } catch (Exception e) {
            logger.error(e);
        }
        return "";
    }

    /**
     * 
     * @Title: faith_Inquiry
     * @Description: 房屋价格查询
     * @author
     * @date
     * @param request
     * @return
     */
    @RequestMapping("/faith_Inquiry")
    @ResponseBody
    public String AccountChange(HttpServletRequest request) {
        String portrait_result = "";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("server 处理房屋价格查询请求");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String sid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("Inquiry", paramData, sid, "1", merId);
            if (re != 1) {
                logger.error("房屋价格查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("bcInquiry", paramData, sid, "2", merId);
            if (ret != 1) {
                logger.error("房屋价格查询，插入请求数源流水失败！ ");
            }
            map.put("SID", sid);
            jsonObject = jsonObject.discard("merchantId");
            // 去后置请求数据
            portrait_result = httpsUtil.postMsg(postBCUrl + "postBC_Inquiry", "paramData=" + jsonObject.toString());

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            // String marking_cod =
            // transDtlService.insertMarkingCode(jsonObject, "AccountChange");
            // map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
            String tim = Util.getCurrentDateTime();
            map.put("CREATE_TIME", tim);
            map.put("CITY_NAME", jsonObject.getString("city_name"));
            map.put("HOUSE_TYPE", jsonObject.getString("house_type"));
            map.put("FILTER", jsonObject.getString("filter"));
            map.put("AREA", jsonObject.getString("area"));
            // -----------json解析修改start------------
            map = JsonUtil.getJsonMap(portrait_result, map, null);

            // 更新请求报文的返回状态及返回时间
            int rett = transDtlService.updateRequestMsg(map.get("code".toUpperCase()), sid);
            if (rett < 0) {
                logger.info("更新报文返回状态及时间失败");
            }

            if (!map.get("code".toUpperCase()).equals("600000")) {
                return portrait_result;
            }
            // -----------json解析修改end------------
            inquiryService.insert(map);
            // 流水表更新FLAG_INQUIRY
            if (map.get("FLAG_INQUIRY").equals("1")) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", sid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", sid);
            }
            transDtlService.updateTransDtl(flagmap);

        } catch (Exception e) {
            logger.error("房屋价格查询 输出--->:" + e);
        }
        return portrait_result;
    }

    /**
     * 
     * @Title: VehInsurance
     * @Description: 车辆续保信息查询
     * @author ly
     * @date 2017年6月6日 上午11:27:13
     * @param request
     * @return
     */

    @RequestMapping(value = "/VehInsurance", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String VehInsurance(HttpServletRequest request) {
        String portrait_result = "";
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            logger.info("正在执行车辆续保信息查询！。。。。");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "VehInsurance");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("车辆续保信息查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "bcVehInsurance");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("车辆续保信息查询，插入请求数源流水失败！ ");
            }

            map.put("SID", raid);
            map.put("carNO", jsonObject.getString("carNO"));
            map.put("cityCode", jsonObject.getString("cityCode"));
            map.put("userName", jsonObject.getString("userName"));
            map.put("certCode", jsonObject.getString("certCode"));
            map.put("carCode", jsonObject.getString("carCode"));
            map.put("carDriverNO", jsonObject.getString("carDriverNO"));

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postBCUrl + "postBC_VehInsurance", "paramData=" + jsonObjectTr.toString());
            // -----------json解析修改start------------
            map = (HashMap<String, String>) JsonUtil.getJsonMap(portrait_result, map, null);
            // 如果查询无数据或者查询失败则不入库
            if (!map.get("CODE").equals("600000")) {
                return portrait_result;
            }

            logger.info("getVehInsuranceFaith_parmData 输出--->:" + portrait_result);
            // -----------json解析修改end------------
            vehInsuranceService.insert(map);
            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys1 = { "flag", "flag_vehinsurance" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys1, 2, flagmap);
                if (flagmap.get("FLAG_VEHINSURANCE").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("getVehInsuranceFaith_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }
}
