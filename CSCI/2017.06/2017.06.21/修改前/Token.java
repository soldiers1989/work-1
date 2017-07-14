package com.utils.configurer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.cf.common.utils.security.MD5;
import com.utils.http.HttpsUtil;

import net.sf.json.JSONObject;

public class Token {

    private static final Logger logger = Logger.getLogger(Token.class);

    public static String KYC_KEY_ACCESS_TOKEN_URL = "api.kyc.access_token_url";// token获取地址

    public static String WX_KEY_APIKEY = "api.kyc.apikey";// EncodingAESKey

    public static String WX_KEY_APPTOKEN = "api.kyc.apitoken";// appid

    public static String WX_KEY_IDENTITY = "api.kyc.identity";// 身份，姓名，服务，接口地址

    private static String WX_KEN_dishonest = "api.kyc.dishonest";// 多平台信贷黑名单查

    public static String WX_KEY_BLIACKLIST = "api.kyc.blacklist";// 社交平台高危客户验证服务

    public static String WX_KEY_VERIFY = "api.kyc.verify";// 银行卡三（四）要素认证服务

    public static String WX_KEY_BLIACKMARK = "api.kyc.blackMark";// 犯罪吸毒记录查询服务

    public static String WX_KEY_CREDITBREAK = "api.kyc.creditbreak";// 失信综合查询服务

    public static String WX_KEY_LEAKBLACK = "api.kyc.leakblack";// 贷前信用黑名单-身份信息泄漏名单查询服务

    public static String WX_KEY_MALAGENT = "api.kyc.malagent";// 中介风险指数查询服务

    public static String WX_KEY_ICPC = "api.kyc.ICPC";// 职业资格证书查询服务

    public static String WX_KEY_DATAQUERY = "api.kyc.dataQuery";// 用户金融画像服务

    public static String WX_KEY_FOREGININVEST = "api.kyc.foregininvest";// 个人对外投资查询服务

    public static String WX_KEY_CRIME = "api.kyc.crime";// 犯罪详情查询服务

    public static String WX_KEY_queryHouseInfoDetail = "api.kyc.queryHouseInfoDetail";// 户籍信息详情查询服务

    private static String WX_KEN_queryIdentiCard = "api.kyc.queryIdentiCard";// 身份证查验服务

    private static String WX_KEN_ = "api.kyc.queryBlacklist";// 黑名单信息查询服务

    private static String WX_KEN_queryMultiBlacklist = "api.kyc.queryMultiBlacklist";// 多平台信贷黑名单查询

    private static String WX_KEN_userBanking = "api.kyc.userBanking";// 用户金融画像服务(一)

    private static String WX_KEN_nightActive = "api.kyc.nightActive";// 夜间活跃区域查询服务

    private static String WX_KEN_interestApp = "api.kyc.interestApp";// 应用兴趣标签查询服务

    private static String WX_KEN_preferenceTag = "api.kyc.preferenceTag";// 线下消费偏好标签查询服务

    private static String WX_KEN_BankingTag = "api.kyc.BankingTag";// 金融标签查询服务

    private static String WX_KEN_OftenPlace = "api.kyc.OftenPlace";// 常去场所类型查询服务

    /**
     * 获取微信配置
     * 
     * @param key
     * @return
     */
    public static String getkycProperty(String key) {
        Properties prop = new Properties();
        InputStream in;
        in = HttpsUtil.class.getResourceAsStream("/configs/env/kyc.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            logger.error("token获取properties异常！" + e);
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                logger.error("token获取properties关闭流异常！" + e);
            }
        }
        return prop.getProperty(key);
    }

    /**
     * 
     * @return
     */
    public static String getAccessTokenFromKYC() {
        // 应用ID
        String access_token_url = "https://api.talkingdata.com/tdmkaccount/authen/app/v2?apikey=b631f4e75ebf442ab10758b4769a4802&apitoken=f58b8b69b155410a84b9faed6121e809";// Token.getkycProperty(Token.KYC_KEY_ACCESS_TOKEN_URL);
        String access_token_str = null;
        try {
            access_token_str = HttpsUtil.sendHttpsRequest(access_token_url, "GET", null);
        } catch (Exception e) {
            logger.error("Token-->getAccessTokenFromKYC----" + e);
        }
        JSONObject obj = JSONObject.fromObject(access_token_str);
        JSONObject jo = (JSONObject) obj.get("data");
        String token = (String) jo.get("token");
        return token;
    }

    /**
     * @Title: getHouseInfoFromKYC
     * @Description: 户籍信息查询服务
     * @author xt
     * @date 2017年4月20日 下午7:10:47 @param id 身份证号
     * @param name
     *            姓名
     * @return 返回的结果
     */
    public static String getHouseInfoFromKYC(String id, String name) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = getkycProperty("api.kyc.identity");
        // 参数替换
        houseinfourl = houseinfourl.replace("{identity_code}", id);
        houseinfourl = houseinfourl.replace("{service}", "qDomicile");
        String access_token_str = null;
        try {
            houseinfourl = houseinfourl.replace("{identity_name}", URLEncoder.encode(name, "UTF-8"));
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getHouseInfoFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getQueryDisDebtorInfoFromKYC
     * @Description: 失信被执行人信息查询服务
     * @author xt
     * @date 2017年4月20日 下午8:12:28
     * @param id
     * @param name
     * @return
     */
    public static String getQueryDisDebtorInfoFromKYC(String id, String name) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEN_dishonest);
        // 参数替换
        houseinfourl = houseinfourl.replace("{identity_code}", id);
        String access_token_str = null;
        try {
            houseinfourl = houseinfourl.replace("{name}", URLEncoder.encode(name, "UTF-8"));
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getQueryDisDebtorInfoFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getValidationHighRiskFromKYC
     * @Description: 社交平台高危客户验证服务
     * @author xt
     * @date 2017年4月20日 下午8:30:51
     * @param mobile
     * @return
     */
    public static String getValidationHighRiskFromKYC(String mobile) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_BLIACKLIST);
        // 参数替换
        houseinfourl = houseinfourl.replace("{enc_m}", MD5.getMD5("86" + mobile));
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getValidationHighRiskFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * 户籍信息详情查询
     * 
     * @param apiName
     * @param apid
     * @return
     */
    public static String getQueryHouseInfoDetailKYC(String apiName, String apid) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_queryHouseInfoDetail);
        // 参数替换

        houseinfourl = houseinfourl.replace("{identity_code}", apid);
        houseinfourl = houseinfourl.replace("{service}", "qDomicileDetail");
        String access_token_str = null;
        try {
            apiName = URLEncoder.encode(apiName, "UTF-8");
            houseinfourl = houseinfourl.replace("{identity_name}", apiName);
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getQueryHouseInfoDetailKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getValidationHighRiskFromKYC
     * @Description: 银行卡三（四）要素认证服务
     * @author xt
     * @date 2017年4月20日 下午8:32:19
     * @param mobile
     * @return
     */
    public static String getValidationBankCardFromKYC(String name, String bankcard, String idcard, String mobile) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_VERIFY);
        // 参数替换
        houseinfourl = houseinfourl.replace("{name}", name);
        houseinfourl = houseinfourl.replace("{bankcard}", bankcard);
        houseinfourl = houseinfourl.replace("{idcard}", idcard);
        if ("".equals(mobile)) {
            mobile = "无";
        }
        houseinfourl = houseinfourl.replace("{mobile}", mobile);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getValidationBankCardFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getValidationBankCardFromKYC
     * @Description: 犯罪吸毒记录查询服务
     * @author xt
     * @date 2017年4月20日 下午8:38:05
     * @param name
     * @param bankcard
     * @param idcard
     * @param mobile
     * @return
     */
    public static String getQueryBadRecordsFromKYC(String id, String name) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_BLIACKMARK);
        // 参数替换
        houseinfourl = houseinfourl.replace("{identity_code}", id);
        houseinfourl = houseinfourl.replace("{service}", "qBlackmark");
        String access_token_str = null;
        try {
            houseinfourl = houseinfourl.replace("{name}", URLEncoder.encode(name, "UTF-8"));
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getQueryBadRecordsFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * 
     * @Title: getForeignInvest
     * @Description: 个人对外投资查询服务
     * @author wyj
     * @date 2017年4月24日 下午7:39:47
     * @param key
     * @return
     */
    public static String getForeignInvest(String key) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_FOREGININVEST);
        // 参数替换
        houseinfourl = houseinfourl.replace("{identity_code}", key);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getForeignInvest----" + e);
        }
        return access_token_str;
    }

    /**
     * 
     * @Title: getCrime
     * @Description: 犯罪详情查询服务
     * @author wyj
     * @date 2017年4月24日 下午7:40:13
     * @param code
     * @param service
     * @param name
     * @return
     */
    public static String getCrime(String code, String name) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_CRIME);
        // 参数替换
        houseinfourl = houseinfourl.replace("{identity_code}", code);
        String access_token_str = null;
        try {
            name = URLEncoder.encode(name, "UTF-8");
            houseinfourl = houseinfourl.replace("{identity_name}", name);
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getCrime----" + e);
        }
        return access_token_str;
    }

    /**
     * 身份证查验服务
     * 
     * @param apiName
     * @param apid
     * @return
     */
    public static String queryIdentiCard(String str) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEN_queryIdentiCard);
        // 参数替换
        String access_token_str = null;
        try {
            /* apiName = URLEncoder.encode(apiName, "UTF-8"); */
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsPost(houseinfourl, token, str);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->queryIdentiCard----" + e);
        }
        return access_token_str;
    }

    /**
     * 黑名单信息查询服务
     * 
     * @param apiName
     * @param apid
     * @return
     */
    public static String WX_KEN(String apid) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEN_);
        // 参数替换
        houseinfourl = houseinfourl.replace("{identity_code}", apid);
        houseinfourl = houseinfourl.replace("{service}", "qBlacklist");
        String access_token_str = null;
        try {
            /* apiName = URLEncoder.encode(apiName, "UTF-8"); */
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->WX_KEN----" + e);
        }
        return access_token_str;
    }

    /**
     * 多平台借贷黑名单查询
     * 
     * @param phoneNum
     * @return
     */
    public static String queryMultiBlacklist(String phoneNum) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEN_queryMultiBlacklist);
        String phonemd5 = MD5.getMD5("86" + phoneNum);
        houseinfourl = houseinfourl.replace("{enc_m}", phonemd5);
        String access_token_str = null;
        try {
            /* apiName = URLEncoder.encode(apiName, "UTF-8"); */
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->queryMultiBlacklist----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getQueryCreditFromKYC
     * @Description: 失信综合查询服务
     * @author xt
     * @date 2017年4月21日 上午9:20:50
     * @param id
     * @param name
     * @return
     */
    public static String getQueryCreditFromKYC(String idcard, String name) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_CREDITBREAK);
        // 参数替换
        houseinfourl = houseinfourl.replace("{idcard}", idcard);
        String access_token_str = null;
        try {
            houseinfourl = houseinfourl.replace("{name}", URLEncoder.encode(name, "UTF-8"));
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getQueryCreditFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getQueryBlacklistAndInfoOutFromKYC
     * @Description: 贷前信用黑名单-身份信息泄漏名单查询服务
     * @author xt
     * @date 2017年4月21日 上午9:36:22
     * @param idcard
     * @param name
     * @return
     */
    public static String getQueryBlacklistAndInfoOutFromKYC(String idcard, String name) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_LEAKBLACK);
        // 参数替换
        houseinfourl = houseinfourl.replace("{idcard}", idcard);
        String access_token_str = null;
        try {
            houseinfourl = houseinfourl.replace("{name}", URLEncoder.encode(name, "UTF-8"));
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(houseinfourl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getQueryBlacklistAndInfoOutFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getQueryInterRiskIndexFromKYC
     * @Description: 中介风险指数查询服务
     * @author xt
     * @date 2017年4月21日 上午9:42:21
     * @param idcard
     * @param name
     * @return
     */
    public static String getQueryInterRiskIndexFromKYC(String str) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取户籍信息查询服务接口连接
        String houseinfourl = Token.getkycProperty(Token.WX_KEY_MALAGENT);
        // 参数替换
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsPost(houseinfourl, token, str);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getQueryInterRiskIndexFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getICPCFromKYC
     * @Description: 职业资格证书查询
     * @author mekio
     * @date 2017年4月21日 上午11:30:25
     * @param idcard
     * @param name
     * @return
     */
    public static String getICPCFromKYC(String identity_code, String identity_name) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEY_ICPC);
        // 参数替换
        // 中文编码成utf-8 16
        try {
            identity_name = URLEncoder.encode(identity_name, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        icpcurl = icpcurl.replace("{identity_code}", identity_code);
        icpcurl = icpcurl.replace("{identity_name}", identity_name);
        icpcurl = icpcurl.replace("{service}", "qICPC");

        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = com.utils.http.HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getICPCFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getdataQueryFromKYC
     * @Description: 用户金融画像服务
     * @author mekio
     * @date 2017年4月21日 上午11:45:04
     * @param identity_code
     * @param identity_name
     * @return
     */
    public static String getdataQueryFromKYC(String phone) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEY_DATAQUERY);
        // 参数替换
        String phonemd5 = MD5.getMD5("86" + phone);
        icpcurl = icpcurl.replace("{phonemd5}", phonemd5);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getdataQueryFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getdataUserBankingFromKYC
     * @Description: 用户金融 画像 服务 （一 )
     * @author XT
     * @date 2017年4月21日 上午11:45:04
     * @param mobileid
     * @param name
     * @param identityNo
     * @return
     */
    public static String getdataUserBankingFromKYC(String mobileid, String name, String identityNo) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEN_userBanking);
        // 参数替换
        icpcurl = icpcurl.replace("{mobileid}", mobileid);
        icpcurl = icpcurl.replace("{identityNo}", identityNo);
        String access_token_str = null;
        try {
            icpcurl = icpcurl.replace("{name}", URLEncoder.encode(name, "UTF-8"));
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getdataUserBankingFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getnightActiveFromKYC
     * @Description:夜间活跃区域查询服务
     * @author XT
     * @date 2017年4月21日 上午11:45:04
     * @param id
     * @param type
     * @return
     */
    public static String getnightActiveFromKYC(String id, String type) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEN_nightActive);
        // 参数替换
        icpcurl = icpcurl.replace("{id}", id);
        icpcurl = icpcurl.replace("{type}", type);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getnightActiveFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getinterestAppFromKYC
     * @Description:应用兴趣标签查询服
     * @author XT
     * @date 2017年4月21日 上午11:45:04
     * @param id
     * @param type
     * @return
     */
    public static String getinterestAppFromKYC(String id, String type) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEN_interestApp);
        // 参数替换
        icpcurl = icpcurl.replace("{id}", id);
        icpcurl = icpcurl.replace("{type}", type);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getinterestAppFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getpreferenceTagFromKYC
     * @Description:线下消费偏好标签查询服务
     * @author XT
     * @date 2017年4月21日 上午11:45:04
     * @param id
     * @param type
     * @return
     */
    public static String getpreferenceTagFromKYC(String id, String type) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEN_preferenceTag);
        // 参数替换
        icpcurl = icpcurl.replace("{id}", id);
        icpcurl = icpcurl.replace("{type}", type);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getpreferenceTagFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getBankingTagFromKYC
     * @Description:金融标签查询服务
     * @author XT
     * @date 2017年4月21日 上午11:45:04
     * @param id
     * @param type
     * @return
     */
    public static String getBankingTagFromKYC(String id, String type) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEN_BankingTag);
        // 参数替换
        icpcurl = icpcurl.replace("{id}", id);
        icpcurl = icpcurl.replace("{type}", type);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getBankingTagFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getOftenPlaceFromKYC
     * @Description:常去场所 类型 查询服务
     * @author XT
     * @date 2017年4月21日 上午11:45:04
     * @param id
     * @param type
     * @return
     */
    public static String getOftenPlaceFromKYC(String id, String type) {
        // 获取token
        String token = getAccessTokenFromCache();
        // 获取职业资格证书查询接口连接
        String icpcurl = Token.getkycProperty(Token.WX_KEN_OftenPlace);
        // 参数替换
        icpcurl = icpcurl.replace("{id}", id);
        icpcurl = icpcurl.replace("{type}", type);
        String access_token_str = null;
        try {
            long start = System.currentTimeMillis(); // 获取开始时间
            access_token_str = HttpsUtil.sendHttpsRequest(icpcurl, "GET", token);
            long end = System.currentTimeMillis(); // 获取结束时间
            System.out.println("程序运行时间： " + (end - start) + "ms");
        } catch (Exception e) {
            logger.error("Token-->getOftenPlaceFromKYC----" + e);
        }
        return access_token_str;
    }

    /**
     * @Title: getAccessTokenFromCache @Description:
     *         从全局缓存中获取access_token @return String 返回类型 @throws
     */
    public static String getAccessTokenFromCache() {
        return CacheUtil.getApplicationValue(CacheUtil.GLOBAL_ACCESS_TOKEN).toString();
    }

    /**
     * @Title: getTimer @Description: 45分钟间隔刷新缓存中
     *         access_token、jssdk_ticket @return void 返回类型 @throws
     */
    public static void getTimer() {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                long starttime = Long.valueOf(CacheUtil.getApplicationValue(CacheUtil.GLOBAL_START_TIME).toString());
                long endtime = System.currentTimeMillis();
                long time = 1000 * 60 * 45;
                if ((endtime - starttime) > time) {
                    String access_token = getAccessTokenFromKYC();
                    CacheUtil.setApplicationValue(CacheUtil.GLOBAL_ACCESS_TOKEN, access_token);// 刷新token
                    CacheUtil.setApplicationValue(CacheUtil.GLOBAL_START_TIME, System.currentTimeMillis());// 刷新最后时间
                }
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 1000 * 60 * 15;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }
}
