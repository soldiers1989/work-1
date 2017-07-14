package com.zz.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.configurer.Token;

import net.sf.json.JSONObject;

@Controller
public class postKYCController {

    private static final Logger logger = Logger.getLogger(postKYCController.class);

    /**
     * 
     * @Title: ForeignInvest
     * @Description: 个人对外投资查询服务
     * @author wyj
     * @date 2017年4月22日 下午2:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/ForeignInvestPost", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ForeignInvest(HttpServletRequest request) {
        String portrait_result = "";
        try {
            // 获取参数并且将参数json格式的参数转换成JSONObject参数进行获取
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String identity_code = jsonObject.getString("identity_code");
            portrait_result = Token.getForeignInvest(identity_code);
            logger.info("请求KYC个人对外投资查询服务返回------->>>" + portrait_result);
        } catch (Exception e) {
            logger.error("请求KYC个人对外投资查询服务返回------->>>" + e);
        }
        return portrait_result;
    }

    /**
     * 
     * @Title: Crime
     * @Description: 犯罪详情查询服务
     * @author wyj
     * @date 2017年4月22日 下午2:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/CrimePost", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Crime(HttpServletRequest request) {
        String portrait_result = "";
        try {
            // 获取参数并且将参数json格式的参数转换成JSONObject参数进行获取
            String paramData = request.getParameter("paramData");
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String identity_code = jsonObject.getString("identity_code");
            String identity_name = jsonObject.getString("identity_name");
            portrait_result = Token.getCrime(identity_code, identity_name);
            logger.info("请求KYC犯罪详情查询服务返回------->>>" + portrait_result);
        } catch (Exception e) {
            logger.error("请求KYC犯罪详情查询服务返回------->>>" + e);
        }
        return portrait_result;
    }

    /**
     * @Title: QueryHouseInfo
     * @Description: 户籍信息查询服务
     * @author xt
     * @date 2017年4月20日 下午7:46:57
     * @param request
     * @return
     */
    @RequestMapping(value = "/QueryHouseInfos", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryHouseInfo(HttpServletRequest request) {

        String pames = request.getParameter("paramData");
        JSONObject obj = JSONObject.fromObject(pames);
        String name = obj.getString("identity_name");
        String id = obj.getString("identity_code");
        String token = Token.getHouseInfoFromKYC(id, name);
        logger.info("请求获取KYC中的token------->>>" + token);
        return token;
    }

    /**
     * @Title: QueryDisDebtorInfo
     * @Description: 失信被执行人信息查询服务
     * @author xt
     * @date 2017年4月20日 下午7:46:43
     * @param request
     * @return
     */
    @RequestMapping(value = "/QueryDisDebtorInfo", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryDisDebtorInfo(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行 失信被执行人信息查询服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String name = obj.getString("identity_name");
            String id = obj.getString("identity_code");
            retvalue = Token.getQueryDisDebtorInfoFromKYC(id, name);
            if (retvalue == null)
                logger.error("失信被执行人信息查询服务返回为空!");
        } catch (Exception e) {
            logger.error(" 失信被执行人信息查询服务异常:" + e);
        }
        return retvalue;
    }

    /**
     * @Title: ValidationHighRisk
     * @Description: 社交平台高危客户验证服务
     * @author xt
     * @date 2017年4月21日 上午9:10:20
     * @param request
     * @return
     */
    @RequestMapping(value = "/ValidationHighRisk", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ValidationHighRisk(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行社交平台高危客户验证服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String mobile = obj.getString("mobile");
            retvalue = Token.getValidationHighRiskFromKYC(mobile);
            if (retvalue == null)
                logger.error("社交平台高危客户验证服务返回为空!");
        } catch (Exception e) {
            logger.error("社交平台高危客户验证服务异常:" + e);
        }
        return retvalue;
    }

    /**
     * 户籍信息查询详情服务
     * 
     * @return
     */
    @RequestMapping(value = "/QueryHouseInfoDetail", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryHouseInfoDetail(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行户籍信息查询详情服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String name = obj.getString("identity_name");
            String id = obj.getString("identity_code");
            retvalue = Token.getQueryHouseInfoDetailKYC(name, id);
            if (retvalue == null)
                logger.error("户籍查询详情服务返回为空!");
        } catch (Exception e) {
            logger.error("执行户籍信息查询详情服务异常:" + e);
        }
        return retvalue;
    }

    /**
     * 黑名单信息查询服务
     * 
     * @return 2017/4/21
     */
    @RequestMapping(value = "/QueryBlack", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBlacklist(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行黑名单信息查询服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String id = obj.getString("identity_code");
            retvalue = Token.WX_KEN(id);
            if (retvalue == null)
                logger.error("黑名单信息查询服务返回为空!");
        } catch (Exception e) {
            logger.error("执行黑名单信息查询服务异常----->>>" + e);
        }
        return retvalue;
    }

    /**
     * 多平台借贷黑名单查询服务
     * 
     * @return 2017/4/21
     */
    @RequestMapping(value = "/QueryMultiBlack", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryMultiBlacklist(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行多平台借贷黑名单查询服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String phoneNum = obj.getString("enc_m");
            retvalue = Token.queryMultiBlacklist(phoneNum);
            if (retvalue == null)
                logger.error("多平台借贷黑名单查询服务返回为空!");
        } catch (Exception e) {
            logger.error("多平台借贷黑名单查询服务异常------>>>" + e);
        }
        return retvalue;
    }

    /**
     * 身份证查验服务
     * 
     * @return 2017/4/21
     */
    @RequestMapping(value = "/QueryIdentiCard", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryIdentiCard(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行身份证查验服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String name = obj.getString("identity_name");
            String id = obj.getString("identity_code");
            retvalue = Token.queryIdentiCard("custName=" + name + "&custCertNo=" + id);
            if (retvalue == null)
                logger.error("身份证查验服务返回为空!");
        } catch (Exception e) {
            logger.error("执行身份证查验服务异常------>>>" + e);
        }
        return retvalue;
    }

    /**
     * @Title: ValidationHighRisk
     * @Description: 银行卡三（四）要素认证服务
     * @author xt
     * @date 2017年4月21日 上午9:11:53
     * @param request
     * @return
     */
    @RequestMapping(value = "/ValidationBankCard", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ValidationBankCard(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行银行卡三（四）要素认证服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String name = obj.getString("name");
            String bankcard = obj.getString("bankcard");
            String idcard = obj.getString("idcard");
            String mobile = obj.getString("mobile");
            retvalue = Token.getValidationBankCardFromKYC(name, bankcard, idcard, mobile);
            if (retvalue == null)
                logger.error("银行卡三（四）要素认证服务返回为空!");
        } catch (Exception e) {
            logger.error("银行卡三（四）要素认证服务异常-------->>>" + e);
        }
        return retvalue;
    }

    /**
     * @Title: QueryBlacklist
     * @Description: 犯罪吸毒记录查询服务
     * @author xt
     * @date 2017年4月21日 上午9:16:08
     * @param request
     * @return
     */
    @RequestMapping(value = "/QueryBadRecords", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBadRecords(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行犯罪吸毒记录查询服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String name = obj.getString("identity_name");
            String id = obj.getString("identity_code");
            retvalue = Token.getQueryBadRecordsFromKYC(id, name);
            if (retvalue == null)
                logger.error("犯罪吸毒记录查询服务返回为空!");
        } catch (Exception e) {
            logger.error("犯罪吸毒记录查询服务异常-------->>>" + e);
        }
        return retvalue;
    }

    /**
     * @Title: QueryCredit
     * @Description: 失信综合查询服务
     * @author xt
     * @date 2017年4月21日 上午9:46:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/QueryCredit", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryCredit(HttpServletRequest request) {

        String retvalue = null;
        try {
            logger.info("正在执行犯罪吸毒记录查询服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String name = obj.getString("identity_name");
            String id = obj.getString("identity_code");
            retvalue = Token.getQueryCreditFromKYC(id, name);
            if (retvalue == null)
                logger.error("失信综合查询服务返回为空!");
        } catch (Exception e) {
            logger.error("失信综合查询服务异常-------->>>" + e);
        }
        return retvalue;
    }

    /**
     * @Title: QueryBlacklistAndInfoOut
     * @Description: 贷前信用黑名单-身份信息泄漏名单查询服务
     * @author xt
     * @date 2017年4月21日 上午9:47:01
     * @param request
     * @return
     */
    @RequestMapping(value = "/QueryBlacklistAndInfoOut", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryBlacklistAndInfoOut(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("贷前信用黑名单-身份信息泄漏名单查询服务。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String name = obj.getString("identity_name");
            String id = obj.getString("identity_code");
            retvalue = Token.getQueryBlacklistAndInfoOutFromKYC(id, name);
            if (retvalue == null)
                logger.error("贷前信用黑名单-身份信息泄漏名单查询服务返回为空!");
        } catch (Exception e) {
            logger.error("贷前信用黑名单-身份信息泄漏名单查询服务异常-------->>>" + e);
        }
        return retvalue;
    }

    /**
     * @Title: QueryInterRiskIndex
     * @Description: 中介风险指数查询服务
     * @author xt
     * @date 2017年4月21日 上午9:49:02
     * @param request
     * @return
     */
    @RequestMapping(value = "/QueryInterRiskIndex", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String QueryInterRiskIndex(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行中介风险指数查询服务 。。。。");
            String paramData = request.getParameter("paramData");
            retvalue = Token.getQueryInterRiskIndexFromKYC(paramData);
            if (retvalue == null)
                logger.error("中介风险指数查询服务 返回为空!");
        } catch (Exception e) {
            logger.error("中介风险指数查询服务异常-------->>>" + e);
        }
        return retvalue;
    }

    @RequestMapping(value = "/ICPC", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycICPC(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        JSONObject json = JSONObject.fromObject(paramData);
        String identity_code = (String) json.get("identity_code");
        String identity_name = (String) json.get("identity_name");
        String token = Token.getICPCFromKYC(identity_code, identity_name);
        return token;
    }

    @RequestMapping(value = "/dataQuery", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycdataQuery(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        JSONObject json = JSONObject.fromObject(paramData);
        String phone = (String) json.get("phone");
        String token = Token.getdataQueryFromKYC(phone);
        return token;
    }

/**
     * 金融标签查询服务
     * @param request
     * @return
     */
    
    @RequestMapping(value = "/kycBankingTag", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycFinancial(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行金融标签查询服务 。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String type = obj.getString("type");
            String id = obj.getString("id");
            retvalue = Token.getBankingTagFromKYC(id, type);
        if (retvalue == null)
            logger.error("金融标签查询服务 返回为空!");
    } catch (Exception e) {
        logger.error("金融标签查询服务异常-------->>>" + e);
    }
    return retvalue;
    }
    
    /**
     * 夜间活跃区域查询
     * @param request
     * @return
     */
    
    @RequestMapping(value = "/kycActiveNight", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycActiveNight(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行夜间活跃区域查询服务 。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String type = obj.getString("type");
            String id = obj.getString("id");
            retvalue = Token.getnightActiveFromKYC(id, type);
        if (retvalue == null)
            logger.error("夜间活跃区域查询服务 返回为空!");
    } catch (Exception e) {
        logger.error("夜间活跃区域查询服务异常-------->>>" + e);
    }
    return retvalue;
}
    
    
    /**
     *  常去场所类型查询服务
     * @param request
     * @return
     */
    
    @RequestMapping(value = "/kycOftenPlace", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycFrequented(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行常去场所类型查询服务 。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String type = obj.getString("type");
            String id = obj.getString("id");
            retvalue = Token.getOftenPlaceFromKYC(id, type);
        if (retvalue == null)
            logger.error("常去场所类型查询服务 返回为空!");
    } catch (Exception e) {
        logger.error("常去场所类型查询服务异常-------->>>" + e);
    }
    return retvalue;
    }
    
    
    
    
    
    /**
     * 用户金融 画像 服务 (一) 
     * @param request
     * @return
     */
    
    @RequestMapping(value = "/kycUserBanking", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycuserBanking(HttpServletRequest request) {
        String paramData = request.getParameter("paramData");
        JSONObject json = JSONObject.fromObject(paramData);
        String mobileid = (String) json.get("mobileid");
        String name = (String) json.get("name");
        String identityNo = (String) json.get("identityNo");
        String token = Token.getdataUserBankingFromKYC(mobileid, name, identityNo);
        return token;
    }
    
    /**
     * 应用兴趣标签查询服
     * @param request
     * @return
     */
    
    @RequestMapping(value = "/kycInterestApp", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycinterestApp(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行应用兴趣标签查询服 。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String type = obj.getString("type");
            String id = obj.getString("id");
            retvalue = Token.getinterestAppFromKYC(id, type);
        if (retvalue == null)
            logger.error("应用兴趣标签查询服 返回为空!");
    } catch (Exception e) {
        logger.error("应用兴趣标签查询服异常-------->>>" + e);
    }
    return retvalue;
    }
    
    /**
     *  线下消费偏好标签查询服务
     * @param request
     * @return
     */
    
    @RequestMapping(value = "/kycPreferenceTag", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String kycpreferenceTag(HttpServletRequest request) {
        String retvalue = null;
        try {
            logger.info("正在执行线下消费偏好标签查询服务 。。。。");
            String paramData = request.getParameter("paramData");
            JSONObject obj = JSONObject.fromObject(paramData);
            String type = obj.getString("type");
            String id = obj.getString("id");
            retvalue = Token.getpreferenceTagFromKYC(id, type);
        if (retvalue == null)
            logger.error("线下消费偏好标签查询服务 返回为空!");
    } catch (Exception e) {
        logger.error("线下消费偏好标签查询服务异常-------->>>" + e);
    }
    return retvalue;
    }
}
