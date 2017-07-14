package com.zz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.zz.service.lt.GxminWorkService;
import com.zz.service.lt.UserWhiteService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/lt")
@SuppressWarnings("static-access")
public class LTbizController {

    private static final Logger logger = Logger.getLogger(LTbizController.class);

    @Autowired
    private TransDtlService transDtlService;// 记录流水

    @Autowired
    private GxminWorkService gxminWorkService;// 工作地点验证

    @Autowired
    private UserWhiteService userWhiteService;// 白名单查询

    @Value("${postLTUrl}")
    private String postLTUrl;

    @Autowired
    private BaseService baseService;

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
     * @Title: gxminWork
     * @Description: 工作地验证
     * @author ly
     * @date 2017年6月14日 上午11:27:13
     * @param request
     * @return
     */

    @RequestMapping(value = "/GxminWork", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String GxminWork(HttpServletRequest request) {
        String retvalue = null;
        String backTime = "0";

        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();

        try {
            logger.info("正在执行工作地验证");
            String parmes = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String[] orgMsg = baseService.getSysParam(request, "9005").split(";");
            String orgCode = orgMsg[0];
            String orgPwd = orgMsg[1];
            jsonObject.put("orgCode", orgCode);
            jsonObject.put("orgPwd", orgPwd);

            String raid = Util.getUUID();

            // 判别机构代码是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "GxminWork");
            tmap.put("REQ_PARAM", parmes);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("工作地验证，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            mapp.put("API", "ltGxminWork");
            mapp.put("REQ_PARAM", parmes);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("工作地验证，插入请求数源流水失败！ ");
            }

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            // sendTelNo longitude latitude 入IDENTI_MAPPING
            // 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "GxminWork");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                // retvalue =
                // "{'code':'00','flag_specialList_c':'1','sl_id_court_bad':'0','sl_id_court_executed':'0','swift_number':'3000659_20170519165528_2391'}";

                retvalue = httpsUtil.postMsg(postLTUrl + "gxminWork", "paramData=" + jsonObjectTr.toString());
                backTime = retvalue.substring(retvalue.lastIndexOf("}") + 1);

                logger.info("工作地验证返回值正在进行入库");
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(retvalue);
                map.put("TIME", Util.getCurrentDateTime());
                map.put("ID", raid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // -----------json解析修改end------------

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("code"), raid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("CODE").equals("00")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    gxminWorkService.insert_GxminWork(map);
                    if (type == 0) {
                        gxminWorkService.insert_GxminWork_S(map);
                    } else {
                        gxminWorkService.update_GxminWork_S(map);// 数据库入表实时表更新
                    }
                } else {
                    // 接口调用异常
                    retvalue = "{\"code\":\"2005\" ,\"msg\":\"接口异常\"}";
                    if (type == 0) {
                        return retvalue + backTime;
                    }
                }
                // -----------编码表的插入end------------

            }
            // -----------从数据库中获取数据进行json字符串拼接start------------
            // 根据markingCode找到最新的一条主表数据
            Map<String, String> mapM = new HashMap<>();
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.lt.GxminWorkMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            retvalue = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(raid, map.get("status"), type);// 流水表更新
        } catch (Exception e) {
            logger.error("工作地验证异常:" + e);
        }
        return retvalue + backTime;
    }

    @RequestMapping(value = "/UserWhite", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String UserWhite(HttpServletRequest request) {
        String retvalue = null;
        String backTime = "0";

        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();

        try {
            logger.info("正在执行白名单查询");
            String parmes = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String[] orgMsg = baseService.getSysParam(request, "9005").split(";");
            String orgCode = orgMsg[0];
            String orgPwd = orgMsg[1];
            jsonObject.put("orgCode", orgCode);
            jsonObject.put("orgPwd", orgPwd);

            String raid = Util.getUUID();

            // 判别机构代码是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "UserWhite");
            tmap.put("REQ_PARAM", parmes);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("白名单查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            mapp.put("API", "ltUserWhite");
            mapp.put("REQ_PARAM", parmes);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("白名单查询，插入请求数源流水失败！ ");
            }

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            // sendTelNo longitude latitude 入IDENTI_MAPPING
            // 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "UserWhite");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                // retvalue =
                // "{'code':'00','flag_specialList_c':'1','sl_id_court_bad':'0','sl_id_court_executed':'0','swift_number':'3000659_20170519165528_2391'}";

                retvalue = httpsUtil.postMsg(postLTUrl + "userWhite", "paramData=" + jsonObjectTr.toString());
                backTime = retvalue.substring(retvalue.lastIndexOf("}") + 1);

                logger.info("白名单查询返回值正在进行入库");
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(retvalue);
                map.put("TIME", Util.getCurrentDateTime());
                map.put("ID", raid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // -----------json解析修改end------------

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("code"), raid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("CODE").equals("00")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    userWhiteService.insert_UserWhite(map);
                    if (type == 0) {
                        userWhiteService.insert_UserWhite_S(map);
                    } else {
                        userWhiteService.update_UserWhite_S(map);// 数据库入表实时表更新
                    }
                } else {
                    // 接口调用异常
                    retvalue = "{\"code\":\"2005\" ,\"msg\":\"接口异常\"}";
                    if (type == 0) {
                        return retvalue + backTime;
                    }
                }
                // -----------编码表的插入end------------

            }
            // -----------从数据库中获取数据进行json字符串拼接start------------
            // 根据markingCode找到最新的一条主表数据
            Map<String, String> mapM = new HashMap<>();
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.lt.UserWhiteMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            retvalue = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(raid, map.get("status"), type);// 流水表更新
        } catch (Exception e) {
            logger.error("白名单查询异常:" + e);
        }
        return retvalue + backTime;
    }

}