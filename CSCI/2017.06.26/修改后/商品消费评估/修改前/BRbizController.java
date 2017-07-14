package com.zz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.zz.service.BaseService;
import com.zz.service.TransDtlService;
import com.zz.service.br.AccountChangeDer;
import com.zz.service.br.AccountChangeS;
import com.zz.service.br.AccountChangeuService;
import com.zz.service.br.BankFourProService;
import com.zz.service.br.BankThreeService;
import com.zz.service.br.ConsumptioncService;
import com.zz.service.br.CrimeInfo;
import com.zz.service.br.EduLevel;
import com.zz.service.br.ExecutionService;
import com.zz.service.br.FaceCompServ;
import com.zz.service.br.FaceRecogServ;
import com.zz.service.br.IdTwophotoServ;
import com.zz.service.br.IdTwoserver;
import com.zz.service.br.LocationService;
import com.zz.service.br.PerInvestqService;
import com.zz.service.br.SpecialListcServcie;
import com.zz.service.br.TelPeriodCMCCfService;
import com.zz.service.br.TelPeriodCTCCyService;
import com.zz.service.br.TelPeriodCUCCdService;
import com.zz.service.br.TelStateCTCCyService;
import com.zz.service.br.TelStateCUCCdService;
import com.zz.service.br.TelStatusCMCCfServcie;
import com.zz.service.br.TelStatusService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/br")
@SuppressWarnings("static-access")
public class BRbizController {

    private static final Logger logger = Logger.getLogger(BRbizController.class);

    @Autowired
    private BaseService baseService;// 公共基础service

    @Autowired
    private CrimeInfo crimeInfo;// 个人不良信息查询service

    @Autowired
    private TelPeriodCMCCfService telPeriodCMCCfService;// 移动手机在线时长service

    @Autowired
    private TelPeriodCUCCdService telPeriodCUCCdService;// 联通手机在线时长service

    @Autowired
    private TelPeriodCTCCyService telPeriodCTCCyService;// 电信手机在线时长service

    @Autowired
    private TelStatusCMCCfServcie telStatusCMCCfServcie;// 移动手机在线状态service

    @Autowired
    private TelStateCUCCdService telStateCUCCdService;// 联通手机在网状态接口类

    @Autowired
    private PerInvestqService perInvestqService;// 个人对外投资v2

    @Autowired
    private TelStateCTCCyService telStateCTCCyService;// 电信手机在网状态接口类

    @Autowired
    private SpecialListcServcie listcServcie;// 特殊名单核查

    @Autowired
    private ConsumptioncService conSumptionc;// 商品消费评估接口类

    @Autowired
    private TelStatusService statusService;// 手机在网状态接口类

    @Autowired
    private ExecutionService exEcution;// 法院被执行人 ——— 个人接口类

    @Autowired
    private TransDtlService transDtlService;// 记录流水

    @Autowired
    private AccountChangeDer accountChangeDer;// 收支等级小额信贷版评估

    @Autowired
    private IdTwoserver idtwo;// 身份证二要素验证

    @Autowired
    private IdTwophotoServ idTwoser;// 身份证验证及照片查询

    @Autowired
    private FaceCompServ faceComp;// 人脸识别对比服务

    @Autowired
    private BankFourProService bankFourProService;// 银行卡四要素查询

    @Autowired
    private BankThreeService bankThreeService;// 银行卡三要素查询

    @Autowired
    private LocationService locationService;// 地址信息核查

    @Autowired
    private AccountChangeS accountChangeS;// 收支等级评估专业版

    @Autowired
    private AccountChangeuService accountChangeuService;// 月度收支等级评估

    @Autowired
    private FaceRecogServ faceRecog;// 身份证人脸识别验证

    @Autowired
    private EduLevel EduLevel;// 学历查询service

    @Value("${postBRUrl}")
    private String postBRUrl;

    private static HttpsUtil httpsUtil = new HttpsUtil();

    /**
     * 
     * @Title: EduLevel
     * @Description: 百融学历查询
     * @author mekio
     * @date 2017年6月2日 下午4:14:39
     * @param request
     * @return
     */
    @RequestMapping(value = "/EduLevel", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String EduLevel(HttpServletRequest request) {
        String portrait_result = "";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("server 处理学历信息查询请求");
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
            int re = transDtlService.insertTransDtl("EduLevel", paramData, sid, "1", merId);
            if (re != 1) {
                logger.error("学历信息查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brEduLevel", paramData, sid, "2", merId);
            if (ret != 1) {
                logger.error("学历信息查询，插入请求数源流水失败！ ");
            }
            map.put("ZZID", sid);
            jsonObject = jsonObject.discard("merchantId");
            // 去后置请求数据
            portrait_result = httpsUtil.postMsg(postBRUrl + "EduLevel", "paramData=" + jsonObject.toString());

            String tim = Util.getCurrentDateTime();
            map.put("CREATE_TIME", tim);
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
            EduLevel.insert(map);
            // 流水表更新
            if (map.get("flag_edulevel".toUpperCase()).equals("1")) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", sid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", sid);
            }
            transDtlService.updateTransDtl(flagmap);

        } catch (Exception e) {
            logger.error("学历信息查询 输出--->:" + e);
        }
        return portrait_result;
    }

    /**
     * 
     * @Title: AccountChange
     * @Description: 月度收支等级评估接口
     * @author wyj
     * @date 2017年5月17日 上午11:26:37
     * @param request
     * @return
     */
    @RequestMapping(value = "/AccountChange", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String AccountChange(HttpServletRequest request) {
        String portrait_result = "";
        String flag = "";
        String backTime = "0";
        Map<String, String> map = new HashMap<String, String>();
        try {
            logger.info("server 处理月度收支等级评估版请求");
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
            int re = transDtlService.insertTransDtl("AccountChange", paramData, sid, "1", merId);
            if (re != 1) {
                logger.error("月度收支等级评估，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brAccountChange", paramData, sid, "2", merId);
            if (ret != 1) {
                logger.error("月度收支等级评估，插入请求数源流水失败！ ");
            }

            // 去后置请求数据
            jsonObject = jsonObject.discard("merchantId");

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObject, "AccountChange");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                portrait_result = httpsUtil.postMsg(postBRUrl + "AccountChange_u", "paramData=" + jsonObject.toString());
                backTime = portrait_result.substring(portrait_result.lastIndexOf("}") + 1);
                // -----------json解析修改start------------
                map = JsonUtil.getJsonMap(portrait_result, map, null);
                // -----------json解析修改end------------

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("code".toUpperCase()), sid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                map.put("TIME", Util.getCurrentDateTime());
                map.put("SID", sid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中

                // 取flag
                flag = map.get("flag_accountchange_u".toUpperCase());
                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("code".toUpperCase()).trim().equals("00")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    accountChangeuService.insert(map);
                    if (type == 0) {
                        accountChangeuService.insert_S(map);
                    } else {
                        accountChangeuService.update_S(map);// 数据库入表实时表更新
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
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.AccountChangeuMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            portrait_result = JsonUtil.getJsonStr(mapM, jsonObject, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------

            transDtlService.updateTransDtl_N(sid, flag, type);// 流水表更新

        } catch (Exception e) {
            logger.error("月度收支等级评估 输出--->:" + e);
        }
        return portrait_result + backTime;
    }

    /**
     * 特殊名单核查——jyx
     * 
     * @return
     */

    @RequestMapping(value = "/SpecialList_c", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String SpecialList_c(HttpServletRequest request) {
        String retvalue = null;
        String backTime = "0";
        Map<String, String> map = new HashMap<>();
        try {
            logger.info("server 处理特殊名单核查请求");
            String parmes = request.getParameter("parmes");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return retvalue;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("SpecialList_c", parmes, raid, "1", merId);
            if (re != 1) {
                logger.error("特殊名单核查，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brSpecialList_c", parmes, raid, "2", merId);
            if (ret != 1) {
                logger.error("特殊名单核查，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "SpecialListc");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                // retvalue =
                // "{'code':'00','flag_specialList_c':'1','sl_id_court_bad':'0','sl_id_court_executed':'0','swift_number':'3000659_20170519165528_2391'}";
                retvalue = httpsUtil.postMsg(postBRUrl + "brSpecialList_c", "paramData=" + jsonObjectTr.toString());
                backTime = retvalue.substring(retvalue.lastIndexOf("}") + 1);

                logger.info("特殊名单核查返回值正在进行入库");
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
                map.put("TIME", Util.getCurrentDateTime());
                map.put("UID", raid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // -----------json解析修改end------------

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("code"), raid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("code").equals("00")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    listcServcie.Insert_SpecialList(map);
                    if (type == 0) {
                        listcServcie.Insert_SpecialList_S(map);
                    } else {
                        listcServcie.update_S(map);// 数据库入表实时表更新
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
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.SpecialListcMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            retvalue = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(raid, map.get("flag_speciallist_c"), type);// 流水表更新
        } catch (Exception e) {
            logger.error("特殊名单核查异常:" + e);
        }
        return retvalue + backTime;
    }

    /**
     * 
     * @Title: Execution
     * @Description: 法院被执行人—个人
     * @author ly
     * @date 2017年6月22日 下午5:51:20
     * @param request
     * @return
     */
    @RequestMapping(value = "/Execution", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Execution(HttpServletRequest request) {
        String retvalue = null;
        String backTime = "0";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行Execution。。。。。");
            String parmes = request.getParameter("parmes");
            // 将json格式的字符串转换成JSONObject
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
            int re = transDtlService.insertTransDtl("Execution", parmes, raid, "1", merId);
            if (re != 1) {
                logger.error("法院被执行人—个人，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brExecution", parmes, raid, "2", merId);
            if (ret != 1) {
                logger.error("法院被执行人—个人，插入请求数源流水失败！ ");
            }

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brExecution", "paramData=" + jsonObjectTr.toString());
            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "Execution");
            String marking_cod = map_code.get("MARKING_CODE");
            String tim = Util.getCurrentDateTime();
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                // retvalue =
                // "{'code':'00','flag_specialList_c':'1','sl_id_court_bad':'0','sl_id_court_executed':'0','swift_number':'3000659_20170519165528_2391'}";

                retvalue = httpsUtil.postMsg(postBRUrl + "brExecution", "paramData=" + jsonObjectTr.toString());
                backTime = retvalue.substring(retvalue.lastIndexOf("}") + 1);

                logger.info("法院被执行人—个人返回值正在进行入库");
                logger.info(retvalue + ":" + retvalue);
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(retvalue);
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
                    Map<String, String> onemap = new HashMap<>();
                    Map<String, String> twomap = new HashMap<>();
                    Map<String, String> threemap = new HashMap<>();
                    Map<String, String> fourmap = new HashMap<>();
                    Map<String, String> fivemap = new HashMap<>();
                    Map<String, String> sixmap = new HashMap<>();
                    Map<String, String> sevenmap = new HashMap<>();
                    Map<String, String> eightmap = new HashMap<>();
                    Map<String, String> ninetmap = new HashMap<>();
                    Map<String, String> tenmap = new HashMap<>();
                    Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> maps = iterator.next();
                        if (maps.getKey().indexOf("1") > 0) {
                            onemap.put(maps.getKey(), maps.getValue());
                        }
                        if (maps.getKey().indexOf("2") > 0) {
                            twomap.put(maps.getKey().replace("2", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("3") > 0) {
                            threemap.put(maps.getKey().replace("3", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("4") > 0) {
                            fourmap.put(maps.getKey().replace("4", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("5") > 0) {
                            fivemap.put(maps.getKey().replace("5", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("6") > 0) {
                            sixmap.put(maps.getKey().replace("6", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("7") > 0) {
                            sevenmap.put(maps.getKey().replace("7", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("8") > 0) {
                            eightmap.put(maps.getKey().replace("8", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("9") > 0) {
                            ninetmap.put(maps.getKey().replace("9", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("10") > 0) {
                            tenmap.put(maps.getKey().replace("10", "1"), maps.getValue());
                        }
                    }
                    if (onemap.size() > 0) {
                        onemap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        onemap.put("CREATE_TIME", tim);
                        onemap.put("identification", raid);
                        onemap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(onemap);
                    }

                    if (twomap.size() > 0) {
                        twomap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        twomap.put("CREATE_TIME", tim);
                        twomap.put("identification", raid);
                        twomap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(twomap);
                    }
                    if (threemap.size() > 0) {
                        threemap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        threemap.put("CREATE_TIME", tim);
                        threemap.put("identification", raid);
                        threemap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(threemap);
                    }
                    if (fourmap.size() > 0) {
                        fourmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        fourmap.put("CREATE_TIME", tim);
                        fourmap.put("identification", raid);
                        fourmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(fourmap);
                    }
                    if (fivemap.size() > 0) {
                        fivemap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        fivemap.put("CREATE_TIME", tim);
                        fivemap.put("identification", raid);
                        fivemap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(fivemap);
                    }
                    if (sixmap.size() > 0) {
                        sixmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        sixmap.put("CREATE_TIME", tim);
                        sixmap.put("identification", raid);
                        sixmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(sixmap);
                    }
                    if (sevenmap.size() > 0) {
                        sevenmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        sevenmap.put("CREATE_TIME", tim);
                        sevenmap.put("identification", raid);
                        sevenmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(sevenmap);
                    }
                    if (eightmap.size() > 0) {
                        eightmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        eightmap.put("CREATE_TIME", tim);
                        eightmap.put("identification", raid);
                        eightmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(eightmap);
                    }
                    if (ninetmap.size() > 0) {
                        ninetmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        ninetmap.put("CREATE_TIME", tim);
                        ninetmap.put("identification", raid);
                        ninetmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(ninetmap);
                    }
                    if (tenmap.size() > 0) {
                        tenmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        tenmap.put("CREATE_TIME", tim);
                        tenmap.put("identification", raid);
                        tenmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(tenmap);
                    }

                    if (type == 0) {
                        if (onemap.size() > 0) {
                            exEcution.insertExecutionS(onemap);
                        }

                        if (twomap.size() > 0) {
                            exEcution.insertExecutionS(twomap);
                        }
                        if (threemap.size() > 0) {
                            exEcution.insertExecutionS(threemap);
                        }
                        if (fourmap.size() > 0) {
                            exEcution.insertExecutionS(fourmap);
                        }
                        if (fivemap.size() > 0) {
                            exEcution.insertExecutionS(fivemap);
                        }
                        if (sixmap.size() > 0) {
                            exEcution.insertExecutionS(sixmap);
                        }
                        if (sevenmap.size() > 0) {
                            exEcution.insertExecutionS(sevenmap);
                        }
                        if (eightmap.size() > 0) {
                            exEcution.insertExecutionS(eightmap);
                        }
                        if (ninetmap.size() > 0) {
                            exEcution.insertExecutionS(ninetmap);
                        }
                        if (tenmap.size() > 0) {
                            exEcution.insertExecutionS(tenmap);
                        }
                    } else {
                        exEcution.deleteExecutionS(map_code);// 数据库入表实时表更新
                        if (onemap.size() > 0) {
                            exEcution.insertExecutionS(onemap);
                        }

                        if (twomap.size() > 0) {
                            exEcution.insertExecutionS(twomap);
                        }
                        if (threemap.size() > 0) {
                            exEcution.insertExecutionS(threemap);
                        }
                        if (fourmap.size() > 0) {
                            exEcution.insertExecutionS(fourmap);
                        }
                        if (fivemap.size() > 0) {
                            exEcution.insertExecutionS(fivemap);
                        }
                        if (sixmap.size() > 0) {
                            exEcution.insertExecutionS(sixmap);
                        }
                        if (sevenmap.size() > 0) {
                            exEcution.insertExecutionS(sevenmap);
                        }
                        if (eightmap.size() > 0) {
                            exEcution.insertExecutionS(eightmap);
                        }
                        if (ninetmap.size() > 0) {
                            exEcution.insertExecutionS(ninetmap);
                        }
                        if (tenmap.size() > 0) {
                            exEcution.insertExecutionS(tenmap);
                        }
                    }

                    // 更新流水收费标志
                    if (String.valueOf(map.get("FLAG_EXECUTION")).equals("1")) {
                        flagmap.put("IS_FEE", "1");
                        flagmap.put("TID", raid);
                    } else {
                        flagmap.put("IS_FEE", "0");
                        flagmap.put("TID", raid);
                    }
                    transDtlService.updateTransDtl(flagmap);

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
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.ExecutionMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            retvalue = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(raid, map.get("status"), type);// 流水表更新
        } catch (Exception e) {
            logger.error("法院被执行人—个人异常:" + e);
        }
        return retvalue + backTime;
    }

    /**
     * 个人不良信息查询
     * 
     * @return
     */
    @RequestMapping(value = "/CrimeInfo", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String CrimeInfo(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        logger.info("server 处理个人不良信息请求！。。。。");
        String paramData = request.getParameter("paramData");
        // 将json格式的字符串转换成JSONObject
        JSONObject jsonObject = JSONObject.fromObject(paramData);
        String raid = Util.getUUID();

        // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
        String merId = jsonObject.getString("merchantId");
        int MerRet = transDtlService.seleMerchant(merId);
        if (MerRet < 0) {
            portrait_result = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
            return portrait_result;// 直接返回错误信息给接口调用方
        }

        // 记录客户请求流水
        int re = transDtlService.insertTransDtl("CrimeInfo", paramData, raid, "1", merId);
        if (re != 1) {
            logger.error("个人信息不良查询，插入请求流水失败！ ");
        }

        // 记录请求数源流水
        int ret = transDtlService.insertTransDtl("brCrimeInfo", paramData, raid, "2", merId);
        if (ret != 1) {
            logger.error("个人信息不良查询，插入请求数源流水失败！ ");
        }

        // 去后置请求数据
        portrait_result = crimeInfo.skipTo(request, paramData, postBRUrl, raid);

        return portrait_result;
    }

    /**
     * 
     * @Title: AccountChangeDer
     * @author mekio
     * @date 2017年4月25日 下午6:51:55
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/AccountChangeDer", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String AccountChangeDer(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        try {
            logger.info("正在执行个人收支等级评估查询！。。。。");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("AccCDer", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("收支等级评估，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brAccCDer", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("收支等级评估，插入请求数源流水失败！ ");
            }
            portrait_result = accountChangeDer.skipTo(paramData, postBRUrl, raid);

        } catch (Exception e) {
            logger.error("收支等级评估异常:" + e);
        }
        return portrait_result;
    }

    /**
     * 移动手机在网时长
     * 
     * @return
     */
    @RequestMapping(value = "/TelPeriodCMCCf", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelPeriodCMCCf(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        try {
            logger.info("正在执行移动手机在网时长查询！。。。。");
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
            int re = transDtlService.insertTransDtl("TelPeriodCMCC_f", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("移动手机在网时长查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brTelPeriodCMCC_f", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("移动手机在网时长查询，插入请求数源流水失败！ ");
            }
            portrait_result = telPeriodCMCCfService.skipTo(paramData, postBRUrl, raid);

        } catch (Exception e) {
            logger.error("移动手机在网时长查询异常:" + e);
        }
        return portrait_result;
    }

    /**
     * 联通手机在网时长
     * 
     * @return
     */
    @RequestMapping(value = "/TelPeriodCUCCd", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelPeriodCUCCd(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        try {
            logger.info("联通手机在网时长信息不良查询！。。。。");
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
            int re = transDtlService.insertTransDtl("TelPeriodCUCC_d", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("联通手机在网时长查询，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brTelPeriodCUCC_d", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("联通手机在网时长查询，插入请求数源流水失败！ ");
            }

            portrait_result = telPeriodCUCCdService.skipTo(paramData, postBRUrl, raid);

        } catch (Exception e) {
            logger.error("联通手机在网时长查询异常:" + e);
        }
        return portrait_result;
    }

    /**
     * 电信手机在网时长
     * 
     * @return
     */
    @RequestMapping(value = "/TelPeriodCTCCy", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelPeriodCTCCy(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        try {
            logger.info("电信手机在网时长 ！。。。。");
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
            int re = transDtlService.insertTransDtl("TelPeriodCTCC_y", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("电信手机在网时长查询，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brTelPeriodCTCC_y", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("电信手机在网时长查询，插入请求数源流水失败！ ");
            }
            portrait_result = telPeriodCTCCyService.skipTo(paramData, postBRUrl, raid);

        } catch (Exception e) {
            logger.error("电信手机在网时长查询异常:" + e);
        }
        return portrait_result;
    }

    /**
     * 移动手机在网状态
     * 
     * @return
     */
    @RequestMapping(value = "/TelStatusCMCCf", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelStatusCMCCf(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        try {
            logger.info("移动手机在网状态查询！。。。。");
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
            int re = transDtlService.insertTransDtl("TelStatusCMCC_f", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("移动手机在网状态查询，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brTelStatusCMCC_f", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("移动手机在网状态查询，插入请求数源流水失败！ ");
            }
            portrait_result = telStatusCMCCfServcie.skipTo(paramData, postBRUrl, raid);

        } catch (Exception e) {
            logger.error("移动手机在网状态查询异常:" + e);
        }
        return portrait_result;
    }

    /**
     * 商品评估
     * 
     * @return
     */
    @RequestMapping(value = "/Consumption_c", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Consumption_c(HttpServletRequest request) {
        logger.info("server-------->跳转server后：" + System.currentTimeMillis());
        String ret_value = null;
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行商品消费评估");
            String parmes = request.getParameter("parmes");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String raid = Util.getUUID();

            long startHZBH = System.currentTimeMillis();
            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                ret_value = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return ret_value;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("Consumption_c", parmes, raid, "1", merId);
            if (re != 1) {
                logger.error("商品消费评估查询，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brConsumption_c", parmes, raid, "2", merId);
            if (ret != 1) {
                logger.error("商品消费评估查询，插入请求数源流水失败！ ");
            }
            long endHZBH = System.currentTimeMillis();
            logger.info("请求流水时间：" + (endHZBH - startHZBH));
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            logger.info("server-------->跳转后置前：" + System.currentTimeMillis());
            ret_value = httpsUtil.postMsg(postBRUrl + "brConsumption_c", "paramData=" + jsonObjectTr.toString());

            logger.info("-------->整个后置处理时间：" + System.currentTimeMillis());
            JSONObject jsonObj = JSONObject.fromObject(ret_value);
            String code = (String) jsonObj.get("code");
            if (!code.equals("00")) {
                return ret_value;
            }

            logger.info("正在执行商品评估返回值入库");
            logger.info(ret_value + ":" + ret_value);
            long start = System.currentTimeMillis();
            Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(ret_value);
            long end = System.currentTimeMillis();
            logger.info("--------json解析时间：" + (end - start) + "-----------");
            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            long startMK = System.currentTimeMillis();
            String marking_cod = transDtlService.insertMarkingCode(jsonObject, "Consumption_c");
            long endMK = System.currentTimeMillis();
            logger.info("marking_cod获取时间：" + (endMK - startMK));
            String time = Util.getCurrentDateTime();
            map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
            map.put("CREATE_TIME", time);
            map.put("ids", raid);
            conSumptionc.intsertConsumptionc(map);
            logger.info("插入业务表时间：" + (System.currentTimeMillis() - endMK));
            // 更新流水收费标志
            long startF = System.currentTimeMillis();
            if (String.valueOf(map.get("flag_consumption_c")).equals("1")) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
            long endF = System.currentTimeMillis();
            logger.info(" 更新流水收费标志时间：" + (endF - startF));

        } catch (Exception e) {
            logger.info("商品评估异常:" + e);
        }
        return ret_value;
    }

    /**
     * 手机在网状态
     * 
     * @return
     */
    @RequestMapping(value = "/TelStatus", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelStatus(HttpServletRequest request) {
        String retvalue = null;
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行手机在网状态");
            String parmes = request.getParameter("parmes");
            // 将json格式的字符串转换成JSONObject
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
            int re = transDtlService.insertTransDtl("TelStatus", parmes, raid, "1", merId);
            if (re != 1) {
                logger.error("手机在网状态查询，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brTelStatus", parmes, raid, "2", merId);
            if (ret != 1) {
                logger.error("手机在网状态查询，插入请求数源流水失败！ ");
            }
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brTelStatus", "paramData=" + jsonObjectTr.toString());

            Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
            if (!(String.valueOf(map.get("code")).equals("600000"))) {
                return retvalue;
            }

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            String marking_cod = transDtlService.insertMarkingCode(jsonObject, "TelStatus");
            String time = Util.getCurrentDateTime();
            map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
            map.put("CREATE_TIME", time);
            map.put("ids", raid);
            statusService.insertTelStatus(map);

            // 更新流水收费标志
            String[] keys = { "flag", "flag_telstatus" };
            flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(retvalue, keys, 2, flagmap);
            if (flagmap.get("FLAG_TELSTATUS").equals("1")) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);

        } catch (

        Exception e) {
            e.printStackTrace();
            logger.error("手机在网状态在网状态异常:" + e);
        }
        return retvalue;
    }

    /**
     * 
     * @Title: TelStateCUCC_d
     * @Description: 联通手机在网状态
     * @author wyj
     * @date 2017年4月25日 上午9:16:04
     * @param request
     * @return
     * 
     */
    @RequestMapping(value = "/TelStateCUCC_d", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelStateCUCC_d(HttpServletRequest request) {
        String portrait_result = "";
        HashMap<String, String> flagmap = new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        try {
            logger.info("请求联通手机在网状态-------");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'msg':'合作机构编号错误','code':700000 }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("TelStateCUCC_d", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("联通手机在网状态查询，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brTelStateCUCC_d", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("联通手机在网状态查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postBRUrl + "TelStateCUCC_dPost", "paramData=" + jsonObjectTr.toString());

            map.put("SID", raid);
            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            String marking_cod = transDtlService.insertMarkingCode(jsonObject, "TelStateCUCCd");
            String time = Util.getCurrentDateTime();
            map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
            map.put("CREATE_TIME", time);
            // -----------json解析修改start------------
            map = JsonUtil.getJsonMap(portrait_result, map, null);
            if (!map.get("code".toUpperCase()).equals("600000")) {
                return portrait_result;
            }
            // -----------json解析修改end------------
            telStateCUCCdService.insert(map);

            // 更新流水收费标志
            if (map.get("FLAG_TELSTATECUCC_D").equals("1")) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);

        } catch (Exception e) {
            logger.error("TelStateCUCC_d_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }

    /**
     * 
     * @Title: TelStateCTCC_y
     * @Description: 电信手机在网状态
     * @author wyj
     * @date 2017年4月25日 上午9:16:41
     * @param request
     * @return
     * 
     */
    @RequestMapping("/TelStateCTCC_y")
    @ResponseBody
    public String TelStateCTCC_y(HttpServletRequest request) {
        String portrait_result = "";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("请求电信手机在网状态-------");
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
            int re = transDtlService.insertTransDtl("TelStateCTCC_y", paramData, sid, "1", merId);
            if (re != 1) {
                logger.error("电信手机在网状态查询，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brTelStateCTCC_y", paramData, sid, "2", merId);
            if (ret != 1) {
                logger.error("电信手机在网状态查询，插入请求数源流水失败！ ");
            }

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            map.put("SID", sid);
            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            String marking_cod = transDtlService.insertMarkingCode(jsonObject, "telstatectccy");
            String time = Util.getCurrentDateTime();
            map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
            map.put("CREATE_TIME", time);

            portrait_result = httpsUtil.postMsg(postBRUrl + "TelStateCTCC_yPost", "paramData=" + jsonObjectTr.toString());

            // -----------json解析修改start------------
            String[] keyRep = { "code;last" };
            map = JsonUtil.getJsonMap(portrait_result, map, keyRep);
            if (!map.get("CODE").equals("600000")) {
                return portrait_result;
            }
            // -----------json解析修改end------------
            telStateCTCCyService.insert(map);

            if (map.get("FLAG_TELSTATECTCC_Y").equals("1")) {
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", sid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", sid);
            }
            transDtlService.updateTransDtl(flagmap);

        } catch (Exception e) {
            logger.error("TelStateCTCC_y_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }

    /**
     * 
     * @Title: PerInvest_q
     * @Description: 个人对外投资v2
     * @author wyj
     * @date 2017年4月24日 上午9:47:21
     * @return
     * 
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/PerInvest_q", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String PerInvest_q(HttpServletRequest request) {
        String portrait_result = "";
        String flag = "";
        String ParentId = "";
        Map<String, String> map = new HashMap<String, String>();
        String[] paramName = { "PerInvestqPBMapper", "PerInvestqPDMapper", "PerInvestqPFMapper", "PerInvestqPPMapper", "PerInvestqPSMapper" };
        try {
            logger.info("server 处理个人对外投资v2 请求");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String sid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {// 直接返回错误信息给接口调用方
                portrait_result = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return portrait_result;
            }
            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("PerInvest_q", paramData, sid, "1", merId);
            if (re != 1) {
                logger.error("个人对外投资v2，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brPerInvest_q", paramData, sid, "2", merId);
            if (ret != 1) {
                logger.error("个人对外投资v2，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "PerInvestq");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            map.put("SID", sid);
            map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入主表map中
            map.put("TIME", Util.getCurrentDateTime());

            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                portrait_result = "{'swift_number':'4000013_20170207155245_8258','code':600000,'flag':{'flag_perinvest_q':1},'product':{'Api_status':{'Api_Status':'200','Message':'查询成功','result':{'PunishBreakInfoList':[{'CaseCode':'(2016)浙0402执409号','Name':'王亮','Type':'自然人','Sex':'男','Age':'26','CardNum':'411526199103201614','Ysfzd':'河南省潢川县','BusinessEntity':'王强','RegDateClean':'2016-02-05','PublishDateClean':'2016-10-09','CourtName':'嘉兴市南湖区人民法院','AreaNameClean':'浙江','GistId':'(2015)嘉南商初字第01267号','GistUnit':'嘉兴南湖法院','Duty':'确定的义务','DisruptTypeName':'违反财产报告制度,其他有履行能力而拒不履行生效法律文书确定义务','Performance':'全部未履行','PerformedPart':'已支付10000元','UnperformPart':'未支付10000元','FocusNumber':'0'}],'PunishedInfoList':[{'CaseNo':'(2016)浙0402执409号','Name':'王亮','CardNum':'411526199103201614','Sex':'男','Age':'26','Province':'河南省','IdentityDeparture':'河南省潢川县','CourtName':'嘉兴市南湖区人民法院','RegDateClean':'2016-02-05','ExecMoney':'61292.33','FocusNumber':'0','CaseStatus':'执行中'}],'RyPosShaInfoList':[{'RyName':'王亮','EntName':'万元人民币','Currency':'上海璎珞生物科技有限公司','EntStatus':'吊销','EntType':'有限责任公司(自然人投资或控股)','FundedRatio':'51.00%','RegCap':'50.00','RegCapCur':'万元人民币','RegNo':'310120001568313','ConForm':'货币','SubConAmt':'25.50'}],'RyPosPerInfoList':[{'RyName':'王亮','EntName':'桐乡市濮院实在人羊毛衫厂','RegNo':'330483620229663','EntType':'个体','RegCap':'10.000000','RegCapCur':'人民币','EntStatus':'在营（开业）','Position':'董事长'}],'RyPosFrInfoList':[{'RyName':'王亮','EntName':'桐乡市濮院实在人羊毛衫厂','RegNo':'330483620229663','EntType':'个体','RegCap':'10.000000','RegCapCur':'人民币','EntStatus':'在营（开业）'}]}},'costTime':1}}";
                // portrait_result =
                // "{'swift_number':'4000013_20170207155245_8258','code':600000,'flag':{'flag_perinvest_q':1},'product':{'Api_status':{'Api_Status':'200','Message':'查询成功，无数据','result':{'PunishBreakInfoList':'','PunishedInfoList':'','RyPosShaInfoList':'','RyPosPerInfoList':'','RyPosFrInfoList':''}},'costTime':1}}";
                // httpsUtil.postMsg(postBRUrl + "PerInvest_qPost", "paramData="
                // + jsonObjectTr.toString());

                // -----------json解析修改start------------
                Map<String, Object> mapC = new HashMap<String, Object>();
                List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
                mapC = JsonUtil.getJsonMapAndArray(portrait_result, map, null);
                map = (HashMap<String, String>) mapC.get("map");
                listMap = (List<Map<String, String>>) mapC.get("listMap");
                // 取flag
                flag = map.get("FLAG_PERINVEST_Q");
                // -----------json解析修改end------------

                // 更新请求第三方接口的报文的返回状态及返回时间-------------
                int rett = transDtlService.updateRequestMsg(map.get("code".toUpperCase()), sid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("code".toUpperCase()).equals("600000")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，子表先删再插，历史表直接插入
                    // 主表插入
                    perInvestqService.insert(map);// 数据库入表历史表插入
                    if (type == 0) {
                        perInvestqService.insert_S(map);// 数据库入表实时表插入
                    } else {
                        // 判断是否有数据,有则将sid获取便于后面根据子表根据sid进行更新
                        List<Map<String, String>> temp = baseService.selectByStr("orm.mapper.zz.br.PerInvestqMapper.selectByCode", marking_cod);
                        if (null != temp && temp.size() > 0) {
                            ParentId = temp.get(0).get("SID");
                            // 根据parentID删除所有实时子表
                            for (int i = 0; i < paramName.length; i++) {
                                baseService.deleteByStr("orm.mapper.zz.br." + paramName[i] + ".deleteByID", ParentId);
                            }
                        }
                        perInvestqService.update_S(map);// 数据库入表实时表更新
                    }

                    // 子表插入
                    for (int i = 0; i < listMap.size(); i++) {
                        map = (HashMap<String, String>) listMap.get(i);
                        String tableName = map.get("listC");
                        logger.info("-----------个人对外投资v2子表" + tableName + "开始插入-------------");
                        map.put("ID", Util.getUUID());
                        map.put("PARENTID", sid);
                        if (tableName.equalsIgnoreCase("PunishBreakInfoList")) {// 失信被执行人
                            perInvestqService.insertPB(map);// 历史表插入
                            perInvestqService.insertPB_S(map);// 实时表插入
                        } else if (tableName.equalsIgnoreCase("PunishedInfoList")) {// 被执行人信息
                            perInvestqService.insertPD(map);
                            perInvestqService.insertPD_S(map);// 实时表插入
                        } else if (tableName.equalsIgnoreCase("RyPosShaInfoList")) {// 企业股东信息
                            perInvestqService.insertPS(map);
                            perInvestqService.insertPS_S(map);// 实时表插入
                        } else if (tableName.equalsIgnoreCase("RyPosPerInfoList")) {// 企业主要管理
                            perInvestqService.insertPP(map);
                            perInvestqService.insertPP_S(map);// 实时表插入
                        } else if (tableName.equalsIgnoreCase("RyPosFrInfoList")) {// 企业法人信息
                            perInvestqService.insertPF(map);
                            perInvestqService.insertPF_S(map);// 实时表插入
                        }
                        logger.info("-----------个人对外投资v2子表" + tableName + "插入成功-------------");
                    }
                } else {
                    // 接口调用异常
                    portrait_result = "{\"code\":\"2005\" ,\"msg\":\"接口异常\"}";
                    if (type == 0) {
                        return portrait_result;
                    }
                }
                // -----------编码表的插入end------------
            }
            // -----------从数据库中获取数据进行json字符串拼接start------------
            // 根据markingCode找到最新的一条主表数据
            Map<String, String> mapM = new HashMap<>();
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.PerInvestqMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            // 根据主表的id找到子表数据
            Map<String, List<Map<String, String>>> mapS = new HashMap<>();
            String[] keyName = { "PunishBreakInfoList", "PunishedInfoList", "RyPosFrInfoList", "RyPosPerInfoList", "RyPosShaInfoList" };
            List<Map<String, String>> list = null;
            for (int i = 0; i < paramName.length; i++) {
                list = baseService.selectByStr("orm.mapper.zz.br." + paramName[i] + ".selectByID", mapM.get("SID"));
                mapS.put(keyName[i], list);
            }
            portrait_result = JsonUtil.getJsonStr(null, jsonObjectTr, mapS);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(sid, flag, type);

        } catch (Exception e) {
            logger.error("PerInvest_q_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }

    /**
     * 身份证二要素验证
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "IdTwoserver", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getIdTwo_z(HttpServletRequest request) {
        String retvalue = "";
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        logger.info("正在请求身份证二要素验证........");
        try {
            HttpsUtil httpsUtil = new HttpsUtil();
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }
            String sid = Util.getUUID();
            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("IdTwo_z", paramData, sid, "1", merId);
            if (re != 1) {
                logger.error("身份证二要素验证，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brIdTwo_z", paramData, sid, "2", merId);
            if (ret != 1) {
                logger.error("身份证二要素验证，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brIdTwo", "paramData=" + jsonObjectTr.toString());
            if (retvalue == null) {
                logger.error("身份证二要素验证返回值为空");
                return "";
            } else {
                logger.info("身份证二要素验证返回值正在进行入库");
                logger.info(retvalue + ":" + retvalue);

                // 更新流水收费标志
                flagmap = (HashMap<String, String>) JsonFormts.getInstantiation().getmapLoweer(retvalue);
                if (String.valueOf(flagmap.get("FLAG_BANKTHREE")).equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", sid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", sid);
                }
                transDtlService.updateTransDtl(flagmap);

                if (retvalue == null)
                    logger.error("身份证二要素验证返回值为空");
                else {
                    logger.info("身份证二要素验证返回值正在执行入库。。。。。");
                    Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
                    // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
                    // marking_code(暂时以uuid的方式来生成marking_code)
                    // 将jsonObject作为参数传入进行
                    // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
                    String marking_cod = transDtlService.insertMarkingCode(jsonObject, "getIdTwo_z");
                    String tim = Util.getCurrentDateTime();
                    map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                    map.put("CREATE_TIME", tim);
                    map.put("zzid", sid);
                    idtwo.insert_idtwo(map);
                }
            }
        } catch (Exception e) {
            logger.error("身份证二要素验证异常:" + e);
        }
        return retvalue;
    }

    /**
     * 银行卡三要素验证
     * 
     * @return
     */
    @RequestMapping(value = "/BankThree", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String BankThree(HttpServletRequest request) {
        String retvalue = null;
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行BankThree。。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject jsStr = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsStr.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("BankThree", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("银行卡三要素验证，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brBankThree", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("银行卡三要素验证，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsStr.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brBankThree", "paramData=" + jsonObjectTr.toString());
            if (retvalue == null) {
                logger.error("银行卡三要素验证返回值为空");
                return "";
            } else {
                logger.info("银行卡三要素验证返回值正在进行入库");
                logger.info(retvalue + ":" + retvalue);

                // 更新流水收费标志
                flagmap = (HashMap<String, String>) JsonFormts.getInstantiation().getmapLoweer(retvalue);
                if (String.valueOf(flagmap.get("FLAG_BANKTHREE")).equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);

                // 数据入库
                Map<String, String> map = JsonFormts.getInstantiation().getmap(retvalue);
                if (String.valueOf(map.get("CODE")).equals("600000")) {
                    map.put("ID", jsStr.getString("id"));
                    map.put("NAME", jsStr.getString("name"));
                    map.put("BANKCARD", jsStr.getString("bank_id"));
                    map.put("UID", raid);
                    bankThreeService.insert(map);
                }
            }
        } catch (Exception e) {
            logger.error("银行卡三要素验证异常:" + e);
        }
        return retvalue;
    }

    /**
     * 身份证验证及照片查询
     */
    @RequestMapping(value = "IdTwophotoServ", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getIdTwo_photo(HttpServletRequest request) {
        String retvalue = "";
        HashMap<String, String> flagmap = new HashMap<String, String>();
        logger.info("正在请求 身份证验证及照片查询........");
        logger.info("请求到server时间----****---->" + System.currentTimeMillis());
        try {
            HttpsUtil httpsUtil = new HttpsUtil();
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            String raid = Util.getUUID();
            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("BankThree", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("身份证验证及照片查询，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brBankThree", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("身份证验证及照片查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brIdTwophotoServ", "paramData=" + jsonObjectTr.toString());

            if (retvalue == null)
                logger.error("身份证验证及照片查询返回值为空");
            else {
                Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
                String code = map.get("code") + "";
                if (!code.equals("600000")) {
                    return retvalue;
                }

                map.put("zzid", raid);
                // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
                // marking_code(暂时以uuid的方式来生成marking_code)
                // 将jsonObject作为参数传入进行
                // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
                String marking_cod = transDtlService.insertMarkingCode(jsonObject, "getIdTwo_photo");
                String tim = Util.getCurrentDateTime();
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                map.put("CREATE_TIME", tim);
                idTwoser.insert_IdTwophoto(map);
                if (String.valueOf(map.get("flag_idtwo_z")).equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("身份证验证及照片查询请求异常:" + e);
        }

        return retvalue;
    }

    /**
     *
     * 人脸识别对比服务
     */
    @RequestMapping(value = "FaceCompServ", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getFaceComp(HttpServletRequest request) {
        String retvalue = "";
        HashMap<String, String> flagmap = new HashMap<String, String>();
        logger.info("正在请求人脸识别对比服务........");
        try {
            HttpsUtil httpsUtil = new HttpsUtil();
            String paramData = request.getParameter("paramData");
            JSONObject json = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = json.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("FaceCompServ", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("人脸识别对比服务，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brFaceCompServ", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("人脸识别对比服务，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = json.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brFaceCompServ", "paramData=" + jsonObjectTr.toString());
            if (retvalue == null)
                logger.error("人脸识别返回值为空");
            else {

                Map<String, String> map = JsonFormts.getInstantiation().getmapLoweer(retvalue);

                String code = map.get("code") + "";
                if (!code.equals("600000")) {
                    return retvalue;
                }

                // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
                String marking_cod = transDtlService.insertMarkingCode(jsonObjectTr, "getFaceComps");
                String time = Util.getCurrentDateTime();
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                map.put("CREATE_TIME", time);
                if (paramData.indexOf("id_photo") > 0)
                    map.put("id_photo", json.getString("id_photo"));
                if (paramData.indexOf("daily_photo") > 0)
                    map.put("daily_photo", json.getString("daily_photo"));
                if (paramData.indexOf("id_type") > 0)
                    map.put("id_type", json.getString("id_type"));
                if (paramData.indexOf("daily_type") > 0)
                    map.put("daily_type", json.getString("daily_type"));
                map.put("zzid", raid);
                faceComp.insert_faces(map);

                if (String.valueOf(map.get("flag_idtwo_z")).equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("人脸识别对比服务请求异常:" + e);
        }

        return retvalue;
    }

    /**
     * 银行卡四要素验证
     * 
     * @return
     */
    @RequestMapping(value = "/BankFourPro", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String BankFourPro(HttpServletRequest request) {
        String retvalue = null;
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行BankFourPro。。。。。");
            String paramData = request.getParameter("paramData");
            String raid = Util.getUUID();
            JSONObject jsStr = JSONObject.fromObject(paramData);

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsStr.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("BankFourPro", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("银行卡四要素验证，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brBankFourPro", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("银行卡四要素验证，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsStr.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brBankFourPro", "paramData=" + jsonObjectTr.toString());
            if (retvalue == null) {
                logger.error("银行卡四要素验证返回值为空");
                return "";
            } else {
                logger.info("银行卡四要素验证返回值正在进行入库");
                logger.info(retvalue + ":" + retvalue);

                // 数据入库
                Map<String, String> map = JsonFormts.getInstantiation().getmap(retvalue);
                if (String.valueOf(map.get("CODE")).equals("600000")) {
                    map.put("ID", jsStr.getString("id"));
                    map.put("CELL", jsStr.getString("cell"));
                    map.put("NAME", jsStr.getString("name"));
                    map.put("BANKCARD", jsStr.getString("bank_id"));
                    map.put("UID", raid);
                    bankFourProService.insert(map);

                    if (String.valueOf(map.get("FLAG_BANKFOURPRO")).equals("1")) {
                        flagmap.put("IS_FEE", "1");
                        flagmap.put("TID", raid);
                    } else {
                        flagmap.put("IS_FEE", "0");
                        flagmap.put("TID", raid);
                    }
                    transDtlService.updateTransDtl(flagmap);
                } else {
                    return retvalue;
                }

            }
        } catch (Exception e) {
            logger.error("银行卡四要素验证异常:" + e);
        }
        return retvalue;
    }

    /**
     * 身份证人脸识别验证
     * 
     * @return
     */
    @RequestMapping(value = "/FaceRecog", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String FaceRecog(HttpServletRequest request) {
        String retvalue = null;
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行FaceRecog。。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();
            // jsonObject.remove("daily_photo");

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("FaceRecog", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("身份证人脸识别验证，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brFaceRecog", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("身份证人脸识别验证，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brFaceRecog", "paramData=" + jsonObjectTr.toString());
            // retvalue="{'swift_number':'444333_20160413142444_6974','code':600000,'flag':{'flag_facerecog':1},'product':{'message':'OK','rtn':0,'verify_result':{'similarity':83.15202223781212},'costTime':1041}}";
            if (retvalue == null) {
                logger.error("身份证人脸识别验证返回值为空");
                return "";
            } else {
                logger.info("身份证人脸识别验证返回值正在进行入库");
                logger.info(retvalue + ":" + retvalue);

                // 数据入库
                Map<String, String> map = JsonFormts.getInstantiation().getmap(retvalue);
                if (String.valueOf(map.get("CODE")).equals("600000")) {
                    JSONObject jsStr = JSONObject.fromObject(paramData);
                    map.put("ID", jsStr.getString("id"));
                    map.put("CELL", jsStr.getString("cell"));
                    map.put("NAME", jsStr.getString("name"));
                    map.put("DAILY_PHOTO", jsStr.getString("daily_photo"));
                    map.put("ZZID", raid);
                    faceRecog.insert_facerecog(map);
                    if (String.valueOf(map.get("FLAG_FACERECOG")).equals("1")) {
                        flagmap.put("IS_FEE", "1");
                        flagmap.put("TID", raid);
                    } else {
                        flagmap.put("IS_FEE", "0");
                        flagmap.put("TID", raid);
                    }
                    transDtlService.updateTransDtl(flagmap);
                } else {
                    return retvalue;
                }
            }
        } catch (Exception e) {
            logger.error("身份证人脸识别验证异常:" + e);
        }
        return retvalue;
    }

    /**
     * 
     * @Title: Location
     * @Description: 地址信息核查
     * @author mekio
     * @date 2017年5月8日 下午7:58:05
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/Location", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String Location(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("地址信息核查！。。。。");
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
            int re = transDtlService.insertTransDtl("Location", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("地址信息核查，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brLocation", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("地址信息核查，插入请求数源流水失败！ ");
            }
            portrait_result = locationService.skipTo(paramData, postBRUrl, raid);

            JSONObject jsonObj = JSONObject.fromObject(portrait_result);
            if (jsonObj.get("code").equals("00")) {// 如果返回码为00 则数据入库
                flagmap.put("IS_FEE", "1");
                flagmap.put("TID", raid);
            } else {
                flagmap.put("IS_FEE", "0");
                flagmap.put("TID", raid);
            }
            transDtlService.updateTransDtl(flagmap);
        } catch (Exception e) {
            logger.error("地址信息核查:" + e);
        }
        return portrait_result;
    }

    /**
     * 
     * @Title: AccountChange_s
     * @Description: 收支等级评估专业版
     * @author xt
     * @date 2017年5月17日 下午4:40:35
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/AccountChange_s", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String AccountChange_s(HttpServletRequest request, HttpServletResponse response) {
        String portrait_result = "";
        Map<String, String> map = null;
        String backTime = "0";
        try {
            logger.info("server 处理收支等级评估专业版");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return portrait_result;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("AccountChange_s", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("收支等级评估专业版，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brAccountChange_s", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("收支等级评估专业版，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "AccountChange_s");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                portrait_result = httpsUtil.postMsg(postBRUrl + "brAccountChange_s", "paramData=" + jsonObjectTr.toString());
                backTime = portrait_result.substring(portrait_result.lastIndexOf("}") + 1);
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(portrait_result);
                map.put("TIME", Util.getCurrentDateTime());
                map.put("UID", raid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // -----------json解析修改end------------

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("CODE"), raid);
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
                    accountChangeS.insert(map);
                    if (type == 0) {
                        accountChangeS.insert_S(map);
                    } else {
                        accountChangeS.update_S(map);// 数据库入表实时表更新
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

            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.AccountChangeSMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            portrait_result = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------

            transDtlService.updateTransDtl_N(raid, String.valueOf(map.get("FLAG_ACCOUNTCHANGE_S")), type);// 更新流水收费标志
        } catch (Exception e) {
            logger.error("收支等级评估专业版:" + e);
        }
        return portrait_result + backTime;
    }
}
