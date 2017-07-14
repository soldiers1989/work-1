package com.zz.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.https.HttpsUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/tDKYCCtl")
@SuppressWarnings("static-access")
public class TDKYCCtl {

    private static final Logger logger = Logger.getLogger(TDKYCCtl.class);

    @Value("${KYCServiceUrl}")
    private String KYCServiceUrl;

    private HttpsUtil httpsUtil = new HttpsUtil();

    /**
     * 
     * @Title: ICPC
     * @Description: 职业资格证书查询服务
     * @author mekio
     * @date 2017年4月20日 下午6:13:33
     * @param request
     * @return
     */
    @RequestMapping(value = "/ICPC", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String ICPC(HttpServletRequest request) {

        String portrait_result = "";
        try {
            logger.info("请求 ICBC 职业资格证书查询服务接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            // 将json格式的对象转换成字符串
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            portrait_result = httpsUtil.postMsg(KYCServiceUrl + "ICPC", "paramData=" + paramData.toString());
            logger.info("ICPC输出：--->" + portrait_result);
            System.out.println("ICPC输出：--->" + portrait_result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return portrait_result;

    }

    /**
     * @Title: dataQuery
     * @Description: 用户金融画像服务
     * @author mekio
     * @date 2017年4月20日 下午6:19:14
     * @param request
     * @return
     */
    @RequestMapping(value = "/dataQuery", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String dataQuery(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求 dataQuery 用户金融画像服务服务接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("phone", request.getParameter("phone"));
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(KYCServiceUrl + "dataQuery", "paramData=" + paramData.toString());
            logger.info("dataQuery输出：--->" + portrait_result);
            System.out.println("dataQuery输出：--->" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return portrait_result;

    }

    /**
     * 户籍查询服务
     * 
     * @return 2017/4/21
     */
    @RequestMapping(value = "/QueryHouseInfose", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryHouseInfo(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求户籍查询服务...... ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.accumulate("service", "qDomicileDetail");
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryHouseInfo", "params=" + paramData.toString());
        } catch (Exception e) {
            logger.error("户籍查询服务异常:" + e);
        }
        return ret_value;
    }

    /**
     * 户籍查询详情服务
     * 
     * @return 2017/4/21
     */
    @RequestMapping(value = "/HouseInfoDetail", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String HouseInfoDetail(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求户籍查询详情服务...... ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("service", "qDomicileDetail");
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryHouseInfoDetail", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("户籍查询详情服务异常:" + e);
        }
        return ret_value;
    }

    /**
     * 身份查验服务
     */

    @RequestMapping(value = "IdentiCard", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String IdentiCard(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求户身份查验服务...... ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryIdentiCard", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("身份查验服务异常:" + e);
        }
        return ret_value;
    }

    /**
     * 黑名单信息查询服务
     */

    @RequestMapping(value = "Blacklist", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Blacklist(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求黑名单信息查询服务务...... ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("service", "qDomicileDetail");
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryBlacklist", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("黑名单信息查询服务异常:" + e);
        }
        return ret_value;
    }

    /**
     * 多平台借贷黑名单查询服务
     */

    @RequestMapping(value = "MultiBlacklist", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String MultiBlacklistService(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求多平台借贷黑名单查询服务...... ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("enc_m", request.getParameter("enc_m"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryMultiBlacklist", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("多平台借贷黑名单查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 
     * @Title: ForeignInvest
     * @Description: 个人对外投资查询服务
     * @author wyj
     * @date 2017年4月22日 下午9:04:00
     * @return
     */

    @RequestMapping(value = "/ForeignInvest", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ForeignInvest(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求个人对外投资查询服务...... ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("identity_code", request.getParameter("identity_code"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "ForeignInvest", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("正在请求个人对外投资查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 
     * @Title: Crime
     * @Description: 犯罪详情查询服务
     * @author wyj
     * @date 2017年4月24日 下午7:33:01
     * @return
     */
    @RequestMapping(value = "/Crime", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Crime(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求犯罪吸毒记录查询服务...... ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("identity_code", request.getParameter("identity_code"));
            paramData.put("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "Crime", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("犯罪吸毒记录查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 失信被执行人信息查询服务
     * 
     * @return
     */
    @RequestMapping(value = "/DisDebtorInfo", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryDisDebtorInfo(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求失信被执行人信息查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryDisDebtorInfo", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("失信被执行人信息查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 社交平台高危客户验证服务
     * 
     * @return
     */
    @RequestMapping(value = "/ValiHighRisk", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ValidationHighRisk(HttpServletRequest request, HttpServletResponse response) {
        String ret_value = null;
        try {
            logger.info("正在请求社交平台高危客户验证服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("mobile", request.getParameter("mobile"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "ValidationHighRisk", "paramData=" + paramData);
        } catch (Exception e) {
            logger.error("社交平台高危客户验证服务:" + e);
        }
        return ret_value;
    }

    /**
     * 银行卡三（四）要素认证服务
     * 
     * @return
     */
    @RequestMapping(value = "/ValiBankCard", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ValidationBankCard(HttpServletRequest request, HttpServletResponse response) {
        String ret_value = null;
        try {
            logger.info("正在请求银行卡三（四）要素认证服务..... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("name", new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.accumulate("bankcard", request.getParameter("bankcard"));
            paramData.accumulate("idcard", request.getParameter("idcard"));
            paramData.accumulate("mobile", request.getParameter("mobile"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "ValidationBankCard", "paramData=" + paramData);
        } catch (Exception e) {
            logger.error("银行卡三（四）要素认证服务:" + e);
        }
        return ret_value;
    }

    /**
     * @Title: QueryBlacklist
     * @Description: 犯罪吸毒记录查询服务
     * @author xt
     * @date 2017年4月21日 上午9:16:08
     * @param request
     * @return
     */
    @RequestMapping(value = "/BadRecords", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBadRecords(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求犯罪吸毒记录查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryBadRecords", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("犯罪吸毒记录查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * @Title: QueryCredit
     * @Description: 失信综合查询服务
     * @author xt
     * @date 2017年4月21日 上午9:46:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/Credit", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryCredit(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求失信综合查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryCredit", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("失信综合查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * @Title: QueryBlacklistAndInfoOut
     * @Description: 贷前信用黑名单-身份信息泄漏名单查询服务
     * @author xt
     * @date 2017年4月21日 上午9:47:01
     * @param request
     * @return
     */
    @RequestMapping(value = "/BlacklistAndInfoOut", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBlacklistAndInfoOut(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求贷前信用黑名单-身份信息泄漏名单查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("identity_code", request.getParameter("identity_code"));
            paramData.accumulate("identity_name", new String(request.getParameter("identity_name").getBytes("iso-8859-1"), "utf-8"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryBlacklistAndInfoOut", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("贷前信用黑名单-身份信息泄漏名单查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * @Title: QueryInterRiskIndex
     * @Description: 中介风险指数查询服务
     * @author xt
     * @date 2017年4月21日 上午9:49:02
     * @param request
     * @return
     */
    @RequestMapping(value = "/InterRiskIndex", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryInterRiskIndex(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求中介风险指数查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            JSONObject obj = new JSONObject();
            JSONObject b = new JSONObject();
            JSONObject o = new JSONObject();
            JSONArray j = new JSONArray();
            o.accumulate("phone", request.getParameter("Uphone"));
            o.accumulate("type", Integer.parseInt(request.getParameter("type")));
            o.accumulate("startTime", Integer.parseInt(request.getParameter("startTime")));
            o.accumulate("duratioin", request.getParameter("duratioin"));
            j.add(0, o);
            b.accumulate("phone", request.getParameter("phone"));
            b.accumulate("imei", request.getParameter("imei"));
            b.accumulate("records", j);
            obj.accumulate("data", b);
            obj.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "QueryInterRiskIndex", "paramData=" + obj.toString());
        } catch (Exception e) {
            logger.error("中介风险指数查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 金融标签查询服务
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/BankingTag", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryFinancial(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求金融标签查询服务查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("id", request.getParameter("id"));
            paramData.accumulate("type", request.getParameter("type"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "BankingTag", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("金融标签查询服务查询服务:" + e);
        }
        return ret_value;
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
        String ret_value = null;
        try {
            logger.info("正在请求夜间活跃区域查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("id", request.getParameter("id"));
            paramData.accumulate("type", request.getParameter("type"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "ActiveNight", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("夜间活跃区域查询服务异常:" + e);
        }
        return ret_value;
    }

    /**
     * 常去场所 类型 查询服务
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/OftenPlace", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryFrequented(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求常去场所 类型 查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("id", request.getParameter("id"));
            paramData.accumulate("type", request.getParameter("type"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "OftenPlace", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("常去场所 类型 查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 用户金融 画像 服务 (一)
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/UserBanking", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryuserBanking(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求用户金融 画像 服务 (一) ...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("mobileid", request.getParameter("mobileid"));
            paramData.accumulate("name", new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.accumulate("identityNo", request.getParameter("identityNo"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "UserBanking", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("用户金融 画像 服务 (一) 查询服务查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 应用兴趣标签查询服
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/InterestApp", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String querynightActive(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求应用兴趣标签查询服...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("id", request.getParameter("id"));
            paramData.accumulate("type", request.getParameter("type"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "InterestApp", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("应用兴趣查询服务异常:" + e);
        }
        return ret_value;
    }

    /**
     * 线下消费偏好标签查询服务
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/PreferenceTag", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String querypreferenceTag(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求线下消费偏好标签查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("id", request.getParameter("id"));
            paramData.accumulate("type", request.getParameter("type"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "PreferenceTag", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("线下消费偏好标签查询服务:" + e);
        }
        return ret_value;
    }

    /**
     * 
     * @Title: querypopulationTag
     * @Description: 人口属性标签查询服务
     * @author ly
     * @date 2017年6月21日 上午11:10:55
     * @param request
     * @return
     */
    @RequestMapping(value = "/PopulationTag", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String querypopulationTag(HttpServletRequest request) {
        String ret_value = null;
        try {
            logger.info("正在请求人口属性标签查询服务...... ");
            HttpsUtil httpsUtil = new HttpsUtil();
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.accumulate("id", request.getParameter("id"));
            paramData.accumulate("type", request.getParameter("type"));
            ret_value = httpsUtil.postMsg(KYCServiceUrl + "PopulationTag", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("人口属性标签查询服务:" + e);
        }
        return ret_value;
    }
}
