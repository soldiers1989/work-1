package com.zz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.common.utils.json.JsonFormts;
import com.cf.common.utils.json.JsonUtil;
import com.cf.common.utils.security.Util;
import com.utils.http.HttpsUtil;
import com.zz.service.BaseService;
import com.zz.service.TransDtlService;
import com.zz.service.bc.InquiryService;
import com.zz.service.bc.ResidenceService;
import com.zz.service.bc.VehInsuranceService;

@Controller
@RequestMapping("/bc")
@SuppressWarnings("static-access")
public class BCbizController {

    private static final Logger logger = Logger.getLogger(BCbizController.class);

    @Autowired
    private BaseService baseService;// 公共基础service

    @Autowired
    private ResidenceService residenceService; // 查询个人户籍信息

    @Autowired
    private InquiryService inquiryService; // 房屋价格查询

    @Autowired
    private TransDtlService transDtlService;// 记录流水

    @Autowired
    private VehInsuranceService vehInsuranceService;// 记录车辆续保

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
     * 
     * @Title: faith_Inquiry
     * @Description: 房屋价格查询
     * @author
     * @date
     * @param request
     * @return
     */
    @RequestMapping(value = "/faith_Inquiry", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String AccountChange(HttpServletRequest request) {
        String portrait_result = "";
        String backTime = "0";
        Map<String, String> map = new HashMap<String, String>();
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
                portrait_result = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
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
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "faith_Inquiry");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                portrait_result = httpsUtil.postMsg(postBCUrl + "postBC_Inquiry", "paramData=" + jsonObjectTr.toString());
                backTime = portrait_result.substring(portrait_result.lastIndexOf("}") + 1);
                portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(portrait_result);
                map.put("CREATE_TIME", Util.getCurrentDateTime());
                map.put("SID", sid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // -----------json解析修改end------------

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("CODE"), sid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("code".toUpperCase()).equals("600000")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    inquiryService.insert(map);
                    if (type == 0) {
                        inquiryService.insert_S(map);
                    } else {
                        inquiryService.update_S(map);// 数据库入表实时表更新
                    }
                } else {
                    // 接口调用异常
                    portrait_result = "{\"code\":\"2005\" ,\"msg\":\"接口异常\"}";
                    if (type == 0) {
                        return portrait_result + backTime;
                    }
                }
                // -----------编码表的插入end------------
            }

            // -----------从数据库中获取数据进行json字符串拼接start------------
            // 根据markingCode找到最新的一条主表数据
            Map<String, String> mapM = new HashMap<>();

            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.bc.HousePriceMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            portrait_result = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------

            transDtlService.updateTransDtl_N(sid, String.valueOf(map.get("FLAG_INQUIRY")), type);// 更新流水收费标志
        } catch (Exception e) {
            logger.error("房屋价格查询 :" + e);
        }
        return portrait_result + backTime;
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
