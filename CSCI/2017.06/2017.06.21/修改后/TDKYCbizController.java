package com.zz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.common.utils.json.JsonFormts;
import com.cf.common.utils.json.JsonUtil;
import com.cf.common.utils.security.Util;
import com.utils.http.HttpsUtil;
import com.zz.service.TransDtlService;
import com.zz.service.kyc.ActiveNightService;
import com.zz.service.kyc.BadRecordsService;
import com.zz.service.kyc.BankCardService;
import com.zz.service.kyc.BankingTagService;
import com.zz.service.kyc.BlacklistAndInfoOutService;
import com.zz.service.kyc.BlacklistService;
import com.zz.service.kyc.CertificateService;
import com.zz.service.kyc.CreditService;
import com.zz.service.kyc.CrimeService;
import com.zz.service.kyc.DisDebtorInfoService;
import com.zz.service.kyc.ForeignInvestService;
import com.zz.service.kyc.HighRiskService;
import com.zz.service.kyc.HouseInfoDetailService;
import com.zz.service.kyc.HouseInfoServcie;
import com.zz.service.kyc.IdentiCardService;
import com.zz.service.kyc.InterRiskIndexService;
import com.zz.service.kyc.InterestAppService;
import com.zz.service.kyc.MultiBlacklistService;
import com.zz.service.kyc.OftenPlaceService;
import com.zz.service.kyc.PopulationTagService;
import com.zz.service.kyc.PortraitService;
import com.zz.service.kyc.PreferenceTagService;
import com.zz.service.kyc.UserBankingService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/kyc")
@SuppressWarnings("static-access")

public class TDKYCbizController {

    private static final Logger logger = Logger.getLogger(TDKYCbizController.class);

    @Autowired
    private CertificateService certificateService;// 职业资格证书查询service

    @Autowired
    private PortraitService portraitService;// 用户金融画像服务service

    @Autowired
    private IdentiCardService ident;// 身份证查询服务

    @Autowired
    private BlacklistService blackList;// 黑名单查询

    @Autowired
    private MultiBlacklistService muLti;// 多平台借贷黑名单查询服务

    @Autowired
    private HouseInfoServcie houseInfo;// 户籍信息查询服务

    @Autowired
    private HouseInfoDetailService house;// 户籍查询详情服务

    @Autowired
    private CrimeService crimeService;// 犯罪详情查询服务

    @Autowired
    private DisDebtorInfoService disDebtorInfoService;// 失信被执行人信息查询服务

    @Autowired
    private HighRiskService highRiskService;// 社交平台高危客户验证服务

    @Autowired
    private BankCardService bankCardService;// 银行卡三（四）要素认证服务

    @Autowired
    private BadRecordsService badRecordsService;// 犯罪吸毒记录查询服务

    @Autowired
    private CreditService creditService;// 失信综合查询服务

    @Autowired
    private BlacklistAndInfoOutService blacklistAndInfoOutService;// 贷前信用黑名单-身份信息泄漏名单查询服务

    @Autowired
    private InterRiskIndexService interRiskIndexService;// 中介风险指数查询服务

    @Autowired
    private ForeignInvestService foreignInvestService;// 中介风险指数查询服务

    @Autowired
    private ActiveNightService activeNightService;// 夜间活跃区域查询

    @Autowired
    private OftenPlaceService oftenPlaceService;// 夜间活跃区域查询

    @Autowired
    private PreferenceTagService preferenceTagService;// 线下消费偏好标签查询服务

    @Autowired
    private InterestAppService interestAppService;// 金融标签查询服务

    @Autowired
    private BankingTagService bankingTagService;// 金融标签查询服务

    @Autowired
    private UserBankingService userBankingService;// 金融标签查询服务

    @Autowired
    private TransDtlService transDtlService;// 记录流水

    @Autowired
    private PopulationTagService populationTagService;// 人口属性标签查询服务

    @Value("${postKYCUrl}")
    private String postKYCUrl;

    private static HttpsUtil httpsUtil = new HttpsUtil();

    /**
     * 户籍信息查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryHouseInfo", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryHouseInfo(HttpServletRequest request) {
        String retvalue = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.error("正在执行户籍信息查询服务......");
            String paramData = request.getParameter("params");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "HouseInfo");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("户籍信息信息查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCHouseInfo");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("户籍信息查询，插入请求数源流水失败！ ");
            }

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postKYCUrl + "QueryHouseInfos", "paramData=" + jsonObjectTr.toString());
            // retvalue = "";

            // retvalue = httpsUtil.postMsg(postKYCUrl + "QueryHouseInfos",
            // "paramData=" + paramData.toString());
            if (retvalue != null) {

                // 数据入库
                Map<String, String> map = JsonFormts.getInstantiation().getmap(retvalue);
                map.put("IDS", raid);
                map.put("identity_name", jsonObject.getString("identity_name"));
                map.put("identity_code", jsonObject.getString("identity_code"));
                houseInfo.Insert_HouseInfo(map);

                // 更新流水收费标志
                if (String.valueOf(map.get("code")).equals("0") || String.valueOf(map.get("code")).equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("户籍信息查询服务异常:" + e);
        }
        return retvalue;
    }

    /**
     * 户籍信息详情查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryHouseInfoDetail", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryHouseInfoDetail(HttpServletRequest request) {
        String retvalue = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行户籍信息详情查询服务。。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            tmap.put("API", "HouseInfoDetail");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("户籍信息详情查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCHouseInfoDetail");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("户籍信息详情查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postKYCUrl + "QueryHouseInfoDetail", "paramData=" + jsonObjectTr.toString());
            if (retvalue == null)
                logger.error("户籍信息详情查询服务返回为空");
            else {
                logger.info("正在执行户籍查询详情入库操作");
                // 数据入库
                Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
                map.put("ids", raid);
                house.Insert_HouseInfo(map);
                if (retvalue.indexOf("[") > 0) {
                    List<Map<String, String>> list = JsonFormts.getInstantiation().getLists(retvalue);
                    for (int i = 0; i < list.size(); i++) {
                        map.put("id", Util.getUUID());
                        map.put("fid", raid);
                        house.Insert_HouseInfoOut(map);
                    }
                }

                // 更新流水收费标志
                if (String.valueOf(map.get("code")).equals("0") || String.valueOf(map.get("code")).equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("户籍信息详情查询服务异常:" + e);
        }
        return retvalue;
    }

    /**
     * 职业资格证书查询服务
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ICPC", produces = "text/html;charset=UTF-8")
    public String ICPC(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行职业资格证书查询！。。。。");
            String paramData = request.getParameter("paramData");
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
            tmap.put("API", "ICPC");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("职业资格证书查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCICPC");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("职业资格证书查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = certificateService.skipTo(jsonObjectTr.toString(), postKYCUrl);

            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys, 1, flagmap);
                if (flagmap.get("CODE").equals("0") || flagmap.get("CODE").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return portrait_result;
    }

    /**
     * 用户金融画像服务（银行卡）
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/dataQuery", produces = "text/html;charset=UTF-8")
    public String dataQuery(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            String paramData = request.getParameter("paramData");
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
            tmap.put("API", "dataQuery");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error(" 用户金融画像服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCdataQuery");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error(" 用户金融画像服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = portraitService.skipTo(jsonObjectTr.toString(), postKYCUrl);

            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys, 1, flagmap);
                if (flagmap.get("CODE").equals("0")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return portrait_result;
    }

    /**
     * 
     * @Title: ForeignInvest
     * @Description: 个人对外投资查询服务
     * @author wyj
     * @date 2017年4月24日 下午5:18:29
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/ForeignInvest", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ForeignInvest(HttpServletRequest request) {
        String portrait_result = "";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("请求个人对外投资查询服务-------");
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String id = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "ForeignInvest");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", id);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error(" 个人对外投资查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCForeignInvest");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", id);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error(" 个人对外投资查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postKYCUrl + "ForeignInvestPost", "paramData=" + jsonObjectTr.toString());
            map.put("ID", id);

            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys4 = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys4, 1, flagmap);
                if (flagmap.get("CODE").equals("0") || flagmap.get("CODE").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", id);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", id);
                }
                transDtlService.updateTransDtl(flagmap);
            }

            // -----------json解析修改start------------
            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(portrait_result, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            if (!map.get("code".toUpperCase()).equals("0")) {
                return portrait_result;
            }
            // -----------json解析修改end------------
            foreignInvestService.insert(map);

            // 子表插入
            for (int i = 0; i < listMap.size(); i++) {
                map = (HashMap<String, String>) listMap.get(i);
                String tableName = map.get("listC");
                logger.info("-----------个人对外投资查询服务子表" + tableName + "开始插入-------------");
                map.put("ID", Util.getUUID());
                map.put("PARENTID", id);
                if (tableName.equalsIgnoreCase("caseInfos")) {// 行政处罚历史信息
                    foreignInvestService.insertCI(map);
                } else if (tableName.equalsIgnoreCase("corporateManagers")) {// 企业主要管理人员信息
                    foreignInvestService.insertCM(map);
                } else if (tableName.equalsIgnoreCase("corporateShareholders")) {// 企业股东信息
                    foreignInvestService.insertCS(map);
                } else if (tableName.equalsIgnoreCase("corporates")) {// 企业法人信息
                    foreignInvestService.insertCP(map);
                } else if (tableName.equalsIgnoreCase("punishBreaks")) {// 失信被执行人信息
                    foreignInvestService.insertPB(map);
                } else if (tableName.equalsIgnoreCase("punished")) {// 被执行人信息
                    foreignInvestService.insertPD(map);
                }
                logger.info("-----------个人对外投资查询服务子表" + tableName + "插入成功-------------");
            }
        } catch (Exception e) {
            logger.error("ForeignInvest_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }

    /**
     * 学历信息查询服务
     * 
     * @return
     */
    @RequestMapping("/QueryEduInfo")
    @ResponseBody
    public String QueryEduInfo() {
        try {
            System.out.println("请求转发中");
            HttpsUtil httpsUtil = new HttpsUtil();
            httpsUtil.postMsg(postKYCUrl, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 身份证查验服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryIdentiCard", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryIdentiCard(HttpServletRequest request) {
        String retvalue = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行 身份证查验服务.....");
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "IdentiCard");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("个人信息不良查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCIdentiCard");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("个人信息不良查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postKYCUrl + "QueryIdentiCard", "paramData=" + jsonObjectTr.toString());
            if (retvalue == null)
                logger.error("身份证查验服务返回值为空....");
            else {
                // 更新流水收费标志
                String[] keys = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvalue, keys, 1, flagmap);
                if (flagmap.get("CODE").equals("0")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);

                Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
                map.put("id", raid);
                map.put("identity_code", jsonObject.getString("identity_code"));
                map.put("identity_name", jsonObject.getString("identity_name"));
                ident.Insert_IdentiCard(map);
            }
        } catch (Exception e) {
            logger.error("身份证查验服务异常:" + e);
        }
        return retvalue;
    }

    /**
     * 
     * @Title: Crime
     * @Description: 犯罪详情查询服务
     * @author wyj
     * @date 2017年4月24日 下午7:36:20
     * @param request
     * @return
     * 
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/Crime", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Crime(HttpServletRequest request) {
        String portrait_result = "";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("请求犯罪详情查询服务-------");
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String id = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "Crime");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", id);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("犯罪详情查询服务查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCCrime");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", id);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("犯罪详情查询服务查询，插入请求数源流水失败！ ");
            }

            // 将json格式的字符串转换成JSONObject
            map.put("ID", id);
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postKYCUrl + "CrimePost", "paramData=" + jsonObjectTr.toString());

            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys, 1, flagmap);
                if (flagmap.get("CODE").equals("0") || flagmap.get("CODE").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", id);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", id);
                }
                transDtlService.updateTransDtl(flagmap);
            }
            // -----------json解析注释start------------
            // 将返回信息插入数据库
            // String[] keys = { "data", "status", "result" };
            // map = (HashMap<String, String>)
            // JsonUtil.getJsonObject(portrait_result, keys, 2, map);
            // String[] keys1 = { "data", "input", "identity_code",
            // "identity_name" };
            // map = (HashMap<String, String>)
            // JsonUtil.getJsonObject(portrait_result, keys1, 3, map);
            // // 获取身份核查结果
            // String result = map.get("RESULT");
            // if (result.equalsIgnoreCase("match")) {
            // String[] keys2 = { "data", "output", "checkDesc", "checkType" };
            // map = (HashMap<String, String>)
            // JsonUtil.getJsonObject(portrait_result, keys2, 3, map);
            // }
            // -----------json解析注释end------------
            // -----------json解析修改start------------
            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(portrait_result, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            if (!map.get("code".toUpperCase()).equals("0")) {
                return portrait_result;
            }
            // -----------json解析修改end------------
            crimeService.insert(map);
            // 子表插入
            for (int i = 0; i < listMap.size(); i++) {
                map = (HashMap<String, String>) listMap.get(i);
                String tableName = map.get("listC");
                logger.info("-----------犯罪详情查询服务" + tableName + "开始插入-------------");
                map.put("ID", Util.getUUID());
                map.put("PARENTID", id);
                if (tableName.equalsIgnoreCase("checkDetail")) {// 失信被执行人
                    crimeService.insertCD(map);
                }
                logger.info("-----------犯罪详情查询服务" + tableName + "插入成功-------------");
            }

            // 获取身份核查结果
            // if (result.equalsIgnoreCase("match")) {
            // String[] keysCD = { "data", "output", "checkDetail",
            // "caseSource", "caseTime", "caseType" };
            // listMap = JsonUtil.getJsonArray(listMap, portrait_result, 3,
            // keysCD);
            // if (null != listMap && listMap.size() > 0) {
            // for (int i = 0; i < listMap.size(); i++) {
            // mapData = listMap.get(i);
            // mapData.put("ID", Util.getUUID()); // 此处的IDyaoyongUUID生成，待修改
            // mapData.put("PARENTID", id);
            // crimeService.insertCD(mapData);
            // }
            // }
            // }
        } catch (Exception e) {
            logger.error("Crime_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }

    /**
     * 失信被执行人信息查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryDisDebtorInfo", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryDisDebtorInfo(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        String retvale = null;

        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {

            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "DisDebtorInfo");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("失信被执行人查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCDisDebtorInfo");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("失信被执行人查询，插入请求数源流水失败！ ");
            }

            logger.info("正在执行失信被执行人信息查询服务.....");
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            // retvale =
            // "{'code':0,'msg':'调用成功','data':{'input':{'identity_code':'32058219810425261X','identity_name':'赵建国'},'output':[{'age':'34','areaName':'江苏','bussinessEntity':'','caseCode':'(2014)张执字第01865号','courtName':'张家港市人民法院','disruptTypeName':'其他有履行能力而拒不履行生效法律文书确定义务','duty':'被告应给付原告761014.13元','gistId':'（2014）张商初字第1164号','gistUnit':'张家港法院','partyTypeName':'自然人','performance':'全部未履行','publishDate':'2015年05月12日','regDate':'20141107','sex':'','unPerformPart':''},{'age':'33','areaName':'江苏','bussinessEntity':'','caseCode':'(2014)张执字第01705号','courtName':'张家港市人民法院','disruptTypeName':'其他有履行能力而拒不履行生效法律文书确定义务','duty':'截止2014年9月19日尚结欠申请执行人中国银行股份有限公司张家港分行160453元','gistId':'(2014)张商初字第00563号','gistUnit':'张家港市人民法院','partyTypeName':'自然人','performance':'全部未履行','publishDate':'2014年12月18日','regDate':'20141015','sex':'','unPerformPart':''},{'age':'34','areaName':'江苏','bussinessEntity':'','caseCode':'(2014)园执字第02362号','courtName':'苏州工业园区人民法院','disruptTypeName':'其他有履行能力而拒不履行生效法律文书确定义务','duty':'暂无','gistId':'(2014)园商初字第01330号','gistUnit':'苏州市工业园区人民法院','partyTypeName':'自然人','performance':'全部未履行','publishDate':'2015年06月08日','regDate':'20140815','sex':'','unPerformPart':''}],'result':'match','status':true},'seq':'2053fea8735a4af18071b6647e9a0232'}";
            retvale = httpsUtil.postMsg(postKYCUrl + "QueryDisDebtorInfo", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("失信被执行人信息查询无返回 ");
                return "";
            }
            String[] keys = { "code", "msg", "seq" };
            flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, keys, 1, flagmap);
            if (flagmap.get("CODE").equals("0") || flagmap.get("CODE").equals("1")) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
            List<Map<String, String>> listMapPD = new ArrayList<Map<String, String>>();
            Map<String, String> mapData = null;
            String[] key = { "data", "status", "result" };
            Map<String, String> map1 = new HashMap<>();
            map1 = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, key, 2, map1);
            String result = map1.get("RESULT");
            if (result.equalsIgnoreCase("match")) {
                // 行政处罚历史信息
                String[] keyss = { "data", "output", "age", "areaName", "bussinessEntity", "caseCode", "courtName", "disruptTypeName", "duty", "gistId", "gistUnit", "partyTypeName", "performance", "publishDate", "regDate", "sex", "unPerformPart" };
                listMapPD = JsonUtil.getJsonArray(listMapPD, retvale, 2, keyss);
                if (null != listMapPD && listMapPD.size() > 0) {
                    for (int i = 0; i < listMapPD.size(); i++) {
                        mapData = listMapPD.get(i);
                        mapData.put("ID", Util.getUUID());
                        mapData.put("PID", raid);
                        disDebtorInfoService.insertCD(mapData);
                    }
                }
            }
            Map<String, String> params = JsonFormts.getInstantiation().getmap(retvale);
            params.put("ID", raid);
            if ("0".equals(params.get("CODE")) || "1".equals(params.get("CODE"))) {
                disDebtorInfoService.insert(params);
            }
        } catch (Exception e) {
            logger.error("失信被执行人信息查询服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 社交平台高危客户验证服务
     * 
     * @return
     */
    @RequestMapping(value = "/ValidationHighRisk", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ValidationHighRisk(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行社交平台高危客户验证服务.....");
            HttpsUtil httpsUtil = new HttpsUtil();

            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "HighRisk");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("社交平台高危客户验证服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCHighRisk");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("社交平台高危客户验证服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "ValidationHighRisk", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("社交平台高危客户验证服务无返回 ");
                return "";
            }
            Map<String, String> params = JsonFormts.getInstantiation().getmap(retvale);
            params.put("ID", raid);
            if ("1200".equals(params.get("CODE"))) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
                JSONObject jsStr = JSONObject.fromObject(paramData);
                params.put("MOBILE", jsStr.getString("mobile"));
                highRiskService.insert(params);
            } else if ("1230".equals(params.get("CODE"))) {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
                JSONObject jsStr = JSONObject.fromObject(paramData);
                params.put("MOBILE", jsStr.getString("mobile"));
                highRiskService.insert(params);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("社交平台高危客户验证服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 银行卡三（四）要素认证服务
     * 
     * @return
     */
    @RequestMapping(value = "/ValidationBankCard", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ValidationBankCard(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行银行卡三（四）要素认证服务.....");
            HttpsUtil httpsUtil = new HttpsUtil();

            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "BankCard");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("银行卡三（四）要素认证服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCBankCard");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("银行卡三（四）要素认证服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "ValidationBankCard", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("银行卡三（四）要素认证服务无返回 ");
                return "";
            }
            Map<String, String> params = JsonFormts.getInstantiation().getmap(retvale);
            params.put("ID", raid);
            if ("2001".equals(params.get("CODE")) || "2003".equals(params.get("CODE"))) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
                JSONObject jsStr = JSONObject.fromObject(paramData);
                params.put("IDCODE", jsStr.getString("idcard"));
                params.put("BANKCARD", jsStr.getString("bankcard"));
                params.put("NAME", jsStr.getString("name"));
                params.put("MOBILE", jsStr.getString("mobile"));
                bankCardService.insert(params);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("银行卡三（四）要素认证服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 黑名单信息查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryBlacklist", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBlacklist(HttpServletRequest request, HttpServletResponse response) {
        String retvalue = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {

            logger.info("正在执行黑名单信息查询服务.....");
            String parmes = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "Blacklist");
            tmap.put("REQ_PARAM", parmes);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("行黑名单信息查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCBlacklist");
            mapp.put("REQ_PARAM", parmes);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("行黑名单信息查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postKYCUrl + "QueryBlack", "paramData=" + jsonObjectTr.toString());

            if (retvalue == null)
                logger.error("黑名单信息查询服务返回值为空.....");
            else {
                Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
                map.put("id", raid);
                blackList.Insert_Blacklist(map);
                if (retvalue.indexOf("[") > 0) {
                    List<Map<String, String>> listMap = JsonFormts.getInstantiation().getLists(retvalue);
                    for (int i = 0; i < listMap.size(); i++) {
                        Map<String, String> getMap = listMap.get(i);
                        getMap.put("id", Util.getUUID());
                        getMap.put("fid", raid);
                        blackList.Insert_BLack(getMap);
                    }
                }

                if ("0".equals(String.valueOf(map.get("code"))) || "0".equals(String.valueOf(map.get("code")))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);

                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("黑名单信息查询服务异常:" + e);
        }
        return retvalue;
    }

    /**
     * 多平台借贷黑名单查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryMultiBlacklist", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryMultiBlacklist(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        JSONObject json = JSONObject.fromObject(paramData);
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        String retvale = null;
        try {
            System.out.println("请求转发中");
            HttpsUtil httpsUtil = new HttpsUtil();

            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "MultiBlacklist");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("多平台借贷黑名单查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCMultiBlacklist");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("多平台借贷黑名单查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "QueryMultiBlack", "paramData=" + jsonObjectTr.toString());
            if (retvale != null) {
                Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvale);
                map.put("id", raid);
                map.put("enc_m", json.getString("enc_m"));
                muLti.Insert_MultiBlacklist(map);
                if ("1200".equals(String.valueOf(map.get("code")))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);

                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            } else
                logger.error("多平台借贷黑名单查询服务返回值为空");

        } catch (Exception e) {
            logger.error("多平台借贷黑名单查询服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 犯罪吸毒记录查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryBadRecords", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBadRecords(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            tmap.put("API", "BadRecords");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("犯罪吸毒记录查询服务，插入请求流水失败！ ");
            }
            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCBadRecords");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("犯罪吸毒记录查询服务，插入请求数源流水失败！ ");
            }
            logger.info("正在执行犯罪吸毒记录查询服务.....");
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "QueryBadRecords", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("犯罪吸毒记录查询无返回 ");
                return "";
            }
            List<Map<String, String>> listMapPD = new ArrayList<Map<String, String>>();
            Map<String, String> mapData = null;
            String[] key = { "data", "status", "result" };
            Map<String, String> map1 = new HashMap<>();
            map1 = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, key, 2, map1);
            String result = map1.get("RESULT");
            if (result.equalsIgnoreCase("match")) {
                // 行政处罚历史信息
                String[] keys = { "data", "output", "checkDetail", "caseSource", "caseTime", "caseType" };
                listMapPD = JsonUtil.getJsonArray(listMapPD, retvale, 3, keys);
                if (null != listMapPD && listMapPD.size() > 0) {
                    for (int i = 0; i < listMapPD.size(); i++) {
                        mapData = listMapPD.get(i);
                        mapData.put("ID", Util.getUUID());
                        mapData.put("PID", raid);
                        badRecordsService.insertcd(mapData);
                    }
                }

            }
            Map<String, String> map = JsonFormts.getInstantiation().getmap(retvale);
            map.put("ID", raid);
            String code = map.get("CODE");
            if ("0".equals(code) || "1".equals(code)) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
                badRecordsService.insert(map);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("犯罪吸毒记录查询服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 失信综合查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryCredit", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryCredit(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行失信综合查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "Credit");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("失信综合查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCCredit");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("失信综合查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "QueryCredit", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("失信综合查询服务无返回 ");
                return "";
            }
            Map<String, String> params = JsonFormts.getInstantiation().getmap(retvale);
            if ("2001".equals(params.get("CODE")) || "2003".equals(params.get("CODE"))) {
                params.put("ID", raid);
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
                JSONObject jsStr = JSONObject.fromObject(paramData);
                params.put("CODE", jsStr.getString("identity_code"));
                params.put("NAME", jsStr.getString("identity_name"));
                creditService.insert(params);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("失信综合查询服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 贷前信用黑名单-身份信息泄漏名单查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryBlacklistAndInfoOut", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBlacklistAndInfoOut(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行贷前信用黑名单-身份信息泄漏名单查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "BlacklistAndInfoOut");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error(" 贷前信用黑名单，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCBlacklistAndInfoOut");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error(" 贷前信用黑名单，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "QueryBlacklistAndInfoOut", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("贷前信用黑名单服务无返回 ");
                return "";
            }
            Map<String, String> params = JsonFormts.getInstantiation().getmap(retvale);
            if ("2001".equals(params.get("CODE")) || "2003".equals(params.get("CODE"))) {
                params.put("ID", raid);
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
                JSONObject jsStr = JSONObject.fromObject(paramData);
                params.put("CODE", jsStr.getString("identity_code"));
                params.put("NAME", jsStr.getString("identity_name"));
                blacklistAndInfoOutService.insert(params);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("贷前信用黑名单-身份信息泄漏名单查询服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 中介风险指数查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryInterRiskIndex", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryInterRiskIndex(HttpServletRequest request, HttpServletResponse response) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行中介风险指数查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "InterRiskIndex");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("中介风险指数查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCInterRiskIndex");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("中介风险指数查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "QueryInterRiskIndex", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("中介风险指数查询服务无返回 ");
                return "";
            }
            Map<String, String> params = JsonFormts.getInstantiation().getmap(retvale);
            if ("1100".equals(params.get("CODE"))) {
                params.put("ID", raid);
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
                interRiskIndexService.insert(params);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("中介风险指数查询服务异常:" + e);
        }
        return retvale;
    }

    /**
     * 金融标签查询服务
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/BankingTag", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryFinancial(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行金融标签查询服务查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "BankingTag");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("金融标签查询服务查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCBankingTag");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("金融标签查询服务查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "kycBankingTag", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("金融标签查询服务查询服务无返回 ");
                return "";
            } else {
                logger.info(retvale + ":" + retvale);
                String[] keys4 = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, keys4, 1, flagmap);
                if ("2001".equals(flagmap.get("CODE"))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
            // Map<String, String> params =
            // JsonFormts.getInstantiation().getmap(retvale);

            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(retvale, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            map.put("UUID", raid);
            // -----------json解析修改end------------
            JSONObject jsStr = JSONObject.fromObject(paramData);
            map.put("ID", jsStr.getString("id"));
            map.put("TYPE", jsStr.getString("type"));
            bankingTagService.insert(map);
            // 子表插入
            if (listMap.size() > 0) {
                for (int i = 0; i < listMap.size(); i++) {
                    map = (HashMap<String, String>) listMap.get(i);
                    String tableName = map.get("listC");
                    map.put("ID", Util.getUUID());
                    map.put("FID", raid);
                    if (tableName.equalsIgnoreCase("tags")) {// 行政处罚历史信息
                        bankingTagService.insertCD(map);
                    }
                }
                logger.info("-----------金融标签查询服务查询服务子表" + map.get("listC") + "插入成功-------------");
            }
        } catch (Exception e) {
            logger.error("金融标签查询服务查询服务:" + e);
        }
        return retvale;
    }

    /**
     * 夜间活跃区域查询
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/ActiveNight", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryActiveNight(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行夜间活跃区域查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "ActiveNight");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("夜间活跃区域查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCActiveNight");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("夜间活跃区域查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "kycActiveNight", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("夜间活跃区域查询服务无返回 ");
                return "";
            }
            Map<String, String> params = JsonFormts.getInstantiation().getmap(retvale);
            if ("2001".equals(params.get("CODE"))) {
                params.put("ZZID", raid);
                JSONObject jsStr = JSONObject.fromObject(paramData);
                params.put("ID", jsStr.getString("id"));
                params.put("TYPE", jsStr.getString("type"));
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
                activeNightService.insert(params);
                logger.info("夜间活跃区域查询服务插入成功！ ");
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("夜间活跃区域查询:" + e);
        }
        return retvale;
    }

    /**
     * 常去场所类型查询服务
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/OftenPlace", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryFrequented(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行常去场所类型查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "OftenPlace");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("常去场所类型查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCOftenPlace");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("常去场所类型查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "kycOftenPlace", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("常去场所类型查询服务无返回 ");
                return "";
            } else {
                logger.info(retvale + ":" + retvale);
                String[] keys4 = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, keys4, 1, flagmap);
                if ("2001".equals(flagmap.get("CODE"))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
            // Map<String, String> params =
            // JsonFormts.getInstantiation().getmap(retvale);

            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(retvale, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            map.put("UUID", raid);
            // -----------json解析修改end------------
            JSONObject jsStr = JSONObject.fromObject(paramData);
            map.put("ID", jsStr.getString("id"));
            map.put("TYPE", jsStr.getString("type"));
            oftenPlaceService.insert(map);
            // 子表插入
            if (listMap.size() > 0) {
                for (int i = 0; i < listMap.size(); i++) {
                    map = (HashMap<String, String>) listMap.get(i);
                    String tableName = map.get("listC");
                    map.put("ID", Util.getUUID());
                    map.put("FID", raid);
                    if (tableName.equalsIgnoreCase("tags")) {// 行政处罚历史信息
                        oftenPlaceService.insertCD(map);
                    }
                    logger.info("-----------常去场所类型查询服务子表" + map.get("listC") + "插入成功-------------");
                }
            }
        } catch (Exception e) {
            logger.error("常去场所类型查询服务:" + e);
        }
        return retvale;
    }

    /**
     * 用户金融 画像 服务 (一)
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/UserBanking", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryuserBanking(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行用户金融 画像 服务 (一).....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "UserBanking");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("用户金融 画像 服务 (一)，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCUserBanking");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("用户金融 画像 服务 (一)，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "kycOftenPlace", "paramData=" + jsonObjectTr.toString());
            // retvale =
            // "{'code':2001,'msg':'ok','data':{'tags':[{'weight':'0.3','name':'阅读','label':'020103'},{'weight':'0.3','name':'诗词名著','label':'02010308'}],'tdid':'3e5c0f4ca2647c5dbcb80fe96c46772'},'seq':'1fa44350-57a0-4dfb-a26e-9cd9452db9d3'}";

            if ("".equals(retvale) || null == retvale) {
                logger.error("用户金融 画像 服务 (一)无返回 ");
                return "";
            } else {
                logger.info(retvale + ":" + retvale);
                String[] keys4 = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, keys4, 1, flagmap);
                if ("0".equals(flagmap.get("CODE"))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
            // Map<String, String> params =
            // JsonFormts.getInstantiation().getmap(retvale);

            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(retvale, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            map.put("UUID", raid);
            // -----------json解析修改end------------
            JSONObject jsStr = JSONObject.fromObject(paramData);
            map.put("ID", jsStr.getString("id"));
            map.put("TYPE", jsStr.getString("type"));
            userBankingService.insert(map);
            // 子表插入
            if (listMap.size() > 0) {
                for (int i = 0; i < listMap.size(); i++) {
                    map = (HashMap<String, String>) listMap.get(i);
                    String tableName = map.get("listC");
                    map.put("ID", Util.getUUID());
                    map.put("FID", raid);
                    if (tableName.equalsIgnoreCase("tags")) {// 行政处罚历史信息
                        oftenPlaceService.insertCD(map);
                    }
                }
                logger.info("-----------用户金融 画像 服务 (一)查询服务子表" + map.get("listC") + "插入成功-------------");
            }
        } catch (Exception e) {
            logger.error("用户金融 画像 服务 (一)查询服务查询服务:" + e);
        }
        return retvale;
    }

    /**
     * 应用兴趣标签查询服
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/InterestApp", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryinterestApp(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行应用兴趣标签查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "InterestApp");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("应用兴趣标签查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCInterestApp");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("应用兴趣标签查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "kycInterestApp", "paramData=" + jsonObjectTr.toString());
            // retvale =
            // "{'code':2001,'msg':'ok','data':{'tags':[{'weight':'0.3','name':'阅读','label':'020103'},{'weight':'0.3','name':'诗词名著','label':'02010308'}],'tdid':'3e5c0f4ca2647c5dbcb80fe96c46772'},'seq':'1fa44350-57a0-4dfb-a26e-9cd9452db9d3'}";
            if ("".equals(retvale) || null == retvale) {
                logger.error("应用兴趣标签查询服务无返回 ");
                return "";
            } else {
                logger.info(retvale + ":" + retvale);
                String[] keys4 = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, keys4, 1, flagmap);
                if ("2001".equals(flagmap.get("CODE"))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
            // Map<String, String> params =
            // JsonFormts.getInstantiation().getmap(retvale);

            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(retvale, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            map.put("UUID", raid);
            // -----------json解析修改end------------
            JSONObject jsStr = JSONObject.fromObject(paramData);
            map.put("ID", jsStr.getString("id"));
            map.put("TYPE", jsStr.getString("type"));
            interestAppService.insert(map);
            // 子表插入
            if (listMap.size() > 0) {
                for (int i = 0; i < listMap.size(); i++) {
                    map = (HashMap<String, String>) listMap.get(i);
                    String tableName = map.get("listC");
                    map.put("ID", Util.getUUID());
                    map.put("FID", raid);
                    if (tableName.equalsIgnoreCase("tags")) {// 行政处罚历史信息
                        interestAppService.insertCD(map);
                    }
                }
                logger.info("-----------应用兴趣标签查询服务子表" + map.get("listC") + "插入成功-------------");
            }
        } catch (Exception e) {
            logger.error("应用兴趣标签查询服:" + e);
        }
        return retvale;
    }

    /**
     * 线下消费偏好标签查询服务
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/PreferenceTag", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String querypreferenceTag(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行线下消费偏好标签查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            tmap.put("API", "PreferenceTag");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("线下消费偏好标签查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCPreferenceTag");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("线下消费偏好标签查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "kycPreferenceTag", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("线下消费偏好标签查询服务无返回 ");
                return "";
            } else {
                logger.info(retvale + ":" + retvale);
                String[] keys4 = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, keys4, 1, flagmap);
                if ("0".equals(flagmap.get("CODE"))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
            // Map<String, String> params =
            // JsonFormts.getInstantiation().getmap(retvale);

            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(retvale, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            map.put("UUID", raid);
            // -----------json解析修改end------------
            JSONObject jsStr = JSONObject.fromObject(paramData);
            map.put("ID", jsStr.getString("id"));
            map.put("TYPE", jsStr.getString("type"));
            preferenceTagService.insert(map);
            // 子表插入
            if (listMap.size() > 0) {
                for (int i = 0; i < listMap.size(); i++) {
                    map = (HashMap<String, String>) listMap.get(i);
                    String tableName = map.get("listC");
                    map.put("ID", Util.getUUID());
                    map.put("FID", raid);
                    if (tableName.equalsIgnoreCase("tags")) {// 行政处罚历史信息
                        preferenceTagService.insertCD(map);
                    }
                }
                logger.info("-----------线下消费偏好标签查询服务子表" + map.get("listC") + "插入成功-------------");
            }
        } catch (Exception e) {
            logger.error("线下消费偏好标签查询服务:" + e);
        }
        return retvale;
    }

    /**
     * 
     * @Title: querypopulationTag
     * @Description: 人口属性标签查询服务
     * @author ly
     * @date 2017年6月21日 上午11:43:51
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/PopulationTag", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String querypopulationTag(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        String retvale = null;
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行人口属性标签查询服务.....");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvale = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvale;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            tmap.put("API", "PopulationTag");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("人口属性标签查询服务，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "KYCPopulationTag");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("人口属性标签查询服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvale = httpsUtil.postMsg(postKYCUrl + "kycPopulationTag", "paramData=" + jsonObjectTr.toString());
            if ("".equals(retvale) || null == retvale) {
                logger.error("人口属性标签查询服务无返回 ");
                return "";
            } else {
                logger.info(retvale + ":" + retvale);
                String[] keys4 = { "code", "msg", "seq" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvale, keys4, 1, flagmap);
                if ("0".equals(flagmap.get("CODE"))) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
            // Map<String, String> params =
            // JsonFormts.getInstantiation().getmap(retvale);

            Map<String, Object> mapC = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            mapC = JsonUtil.getJsonMapAndArray(retvale, map, null);
            map = (HashMap<String, String>) mapC.get("map");
            listMap = (List<Map<String, String>>) mapC.get("listMap");
            map.put("UUID", raid);
            // -----------json解析修改end------------
            JSONObject jsStr = JSONObject.fromObject(paramData);
            map.put("ID", jsStr.getString("id"));
            map.put("TYPE", jsStr.getString("type"));
            populationTagService.insert(map);
            // 子表插入
            if (listMap.size() > 0) {
                for (int i = 0; i < listMap.size(); i++) {
                    map = (HashMap<String, String>) listMap.get(i);
                    String tableName = map.get("listC");
                    map.put("ID", Util.getUUID());
                    map.put("FID", raid);
                    if (tableName.equalsIgnoreCase("tags")) {// 行政处罚历史信息
                        populationTagService.insertCD(map);
                    }
                }
                logger.info("-----------人口属性标签查询服务子表" + map.get("listC") + "插入成功-------------");
            }
        } catch (Exception e) {
            logger.error("人口属性标签查询服务:" + e);
        }
        return retvale;
    }
}
