package com.zz.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.https.HttpsUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/bRCtl")
@SuppressWarnings("static-access")
public class BRCtl {

    private static final Logger logger = Logger.getLogger(BRCtl.class);

    @Value("${BRServiceUrl}")
    private String BRServiceUrl;

    private HttpsUtil httpsUtil = new HttpsUtil();

    /**
     * 
     * @Title: getEduLevel
     * @Description: 学历查询
     * @author mekio
     * @date 2017年6月2日 下午4:40:57
     * @param request
     * @param id
     * @param cell
     * @param name
     * @param merchantId
     * @return
     */
    @RequestMapping(value = "/EduLevel", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getEduLevel(HttpServletRequest request, String id, String cell, String name, String merchantId) {
        String portrait_result = "";
        try {
            logger.info("前置 学历查询接口");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("id", id);
            paramData.put("cell", cell);
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (name) : (new String(name.getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);

            // 将json格式的对象转换成字符串 faith_Residence
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "EduLevel", "paramData=" + paramData.toString());

            logger.info("getResidencePre_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getResidencePre_ERROR 输出--->:" + e);
        }

        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: AccountChange
     * @Description: 月度收支等级评估补充版接口
     * @author wyj
     * @date 2017年5月17日 上午11:24:39
     * @param request
     * @return
     */
    @RequestMapping(value = "/AccountChange", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String AccountChange(HttpServletRequest request, String id, String cell, String name, String merchantId) {
        String portrait_result = "";
        try {
            logger.info("前置 接收月度收支等级评估补充版本接口");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();

            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("id", id);
            paramData.put("cell", cell);
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (name) : (new String(name.getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);

            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "AccountChange", "paramData=" + paramData.toString());
            logger.info("CrimeInfo 输出：--->:" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * @Title: CrimeInfo
     * @Description: 个人不良信用查询接口
     * @author mekio
     * @date 2017年4月20日 下午2:34:35
     * @param request
     * @return @throws
     */
    @RequestMapping(value = "/CrimeInfo", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String CrimeInfo(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("前置接收 个人不良信息查询 请求！");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();

            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "CrimeInfo", "paramData=" + paramData.toString());
            logger.info("CrimeInfo 输出：--->:" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: AccountChangeDer
     * @Description: 收支等级评估基础版
     * @author mekio
     * @date 2017年4月25日 下午6:48:09
     * @param request
     * @return
     */
    @RequestMapping(value = "/AccChangeDer", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String AccChangeDer(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求 AccountChangeDer 收支等级评估接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "AccountChangeDer", "paramData=" + paramData.toString());
            logger.info("AccountChangeDer 输出：--->:" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     *
     * @Title: TelPeriodCMCCf
     * @Description: 移动手机在线时长
     * @author mekio
     * @date 2017年4月20日 下午5:54:11
     * @param request
     * @return
     */
    @RequestMapping(value = "/TelPeriodCMCCf", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String TelPeriodCMCCf(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求 TelPeriodCMCC_f 移动手机在网时长接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "TelPeriodCMCCf", "paramData=" + paramData.toString());
            logger.info("TelPeriodCMCC_f输出：--->" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * @Title: TelPeriodCUCCd
     * @Description: 联通手机在线时长
     * @author mekio
     * @date 2017年4月20日 下午5:56:50
     * @param request
     * @return
     */
    @RequestMapping(value = "/TelPeriodCUCCd", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String TelPeriodCUCCd(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求 TelPeriodCUCC_d 联通手机在网时长接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "TelPeriodCUCCd", "paramData=" + paramData.toString());
            logger.info("TelPeriodCUCC_d输出：--->" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * @Title: TelPeriodCTCCy
     * @Description:电信手机在线时长
     * @author mekio
     * @date 2017年4月20日 下午5:58:25
     * @param request
     * @return
     */
    @RequestMapping(value = "/TelPeriodCTCCy", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String TelPeriodCTCCy(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求 TelPeriodCTCC_y 电信手机在网时长接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "TelPeriodCTCCy", "paramData=" + paramData.toString());
            logger.info("TelPeriodCTCC_y输出：--->" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * @Title: TelStatusCMCCf
     * @Description:移动手机在网状态
     * @author mekio
     * @date 2017年4月20日 下午6:00:51
     * @param request
     * @return
     */
    @RequestMapping(value = "/TelStatusCMCCf", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String TelStatusCMCCf(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求 TelStatusCMCC_f 移动手机在网状态接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "TelStatusCMCCf", "paramData=" + paramData.toString());
            logger.info("TelStatusCMCC_f输出：--->" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * @author jyx 特殊名单核查
     * @return
     */

    @RequestMapping(value = "/SpecialListc", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String SpecialListcServcie(HttpServletRequest request) {
        String msg = null;
        try {
            logger.info("前置接收特殊名单核查请求");
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));// 身份证号
            paramData.put("cell", request.getParameter("cell"));// 手机号
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // paramData.put("linkman_cell",
            // request.getParameter("linkman_cell"));// 联系人手机号
            msg = HttpsUtil.postMsg(BRServiceUrl + "SpecialList_c", "parmes=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求发往特殊名单核查异常:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            msg = msg.substring(0, msg.lastIndexOf("}") + 1);
            return msg;
        } else {
            return msg;
        }
    }

    /**
     * @author jyx
     * 
     *         法院被执行人—个人
     * @return 2017/4/20
     */
    @RequestMapping(value = "/Execution", method = { RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Execution(HttpServletRequest request) {
        String msg = null;
        try {
            logger.info("请求正在发往法院被执行人-个人-------");
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));// 身份证号
            paramData.put("cell", request.getParameter("cell"));// 手机号
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            msg = HttpsUtil.postMsg(BRServiceUrl + "Execution", "parmes=" + paramData.toString());
        } catch (Exception e) {
            logger.error("法院被执行人—个人异常:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            msg = msg.substring(0, msg.lastIndexOf("}") + 1);
            return msg;
        } else {
            return msg;
        }
    }

    /**
     * @author jyx
     * 
     *         商品消费评估
     * @return 2017/4/20
     */
    @RequestMapping(value = "Consumption_c", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String Consumption_c(HttpServletRequest request) {
        String msg = null;
        try {
            logger.info("请求正在发往商品消费评估-------");
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            msg = HttpsUtil.postMsg(BRServiceUrl + "Consumption_c", "parmes=" + paramData.toString());
        } catch (Exception e) {
            logger.error("商品消费评估异常:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            msg = msg.substring(0, msg.lastIndexOf("}") + 1);
            return msg;
        } else {
            return msg;
        }
    }

    /**
     * @author jyx
     * 
     *         手机在网状态
     * @return 2017/4/20
     */
    @RequestMapping(value = "TelStatus", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String TelStatus(HttpServletRequest request) {
        String msg = null;
        try {
            logger.info("请求手机在网状态-------");
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            msg = HttpsUtil.postMsg(BRServiceUrl + "TelStatus", "parmes=" + paramData.toString());
        } catch (Exception e) {
            logger.error("手机在网状态请求异常:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            msg = msg.substring(0, msg.lastIndexOf("}") + 1);
            return msg;
        } else {
            return msg;
        }
    }

    /**
     * 
     * @Title: getResidencePre
     * @Description: 联通手机在网状态
     * @author wyj
     * @date 2017年4月22日 下午2:57:29
     * @param request
     * @return
     */
    @RequestMapping(value = "/TelStateCUCCd", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTelStateCUCCdPre(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求联通手机在网状态-------");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "TelStateCUCC_d", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求联通手机在网状态---getTelStateCUCCdPre_ERROR 输出--->:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: getTELSTATECTCCYPre
     * @Description: 电信手机在网状态
     * @author wyj
     * @date 2017年4月22日 下午2:57:29
     * @param request
     * @return
     */
    @RequestMapping(value = "/telstatectccy", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTELSTATECTCCYPre(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求电信手机在网状态-------");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "TelStateCTCC_y", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求电信手机在网状态---getTELSTATECTCCYPre_ERROR 输出--->:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: getPerInvestqPre
     * @Description: 个人对外投资v2
     * @author wyj
     * @date 2017年4月24日 上午9:33:18
     * @param request
     * @return
     */

    @RequestMapping(value = "/PerInvestq", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getPerInvestqPre(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("前置接收 个人对外投资v2 请求");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "PerInvest_q", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求个人对外投资v2---getPerInvestqPre_ERROR 输出--->:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 身份证二要素验证
     */
    @RequestMapping(value = "getIdTwo_z", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getIdTwo_z(HttpServletRequest request) {
        String retvalue = "";
        logger.info("正在请求身份证二要素验证........");
        try {
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            retvalue = httpsUtil.postMsg(BRServiceUrl + "IdTwoserver", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("身份证二要素验证请求异常:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            retvalue = retvalue.substring(0, retvalue.lastIndexOf("}") + 1);
            return retvalue;
        } else {
            return retvalue;
        }
    }

    /**
     * 身份证验证及照片查询
     */
    @RequestMapping(value = "getIdTwo_photo", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getIdTwo_photo(HttpServletRequest request) {
        String retvalue = "";
        logger.info("正在请求身份证验证及照片查询........");
        logger.info("请求到前置时间----****---->" + System.currentTimeMillis());
        try {
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            retvalue = httpsUtil.postMsg(BRServiceUrl + "IdTwophotoServ", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("身份证验证及照片查询请求异常:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            retvalue = retvalue.substring(0, retvalue.lastIndexOf("}") + 1);
            return retvalue;
        } else {
            return retvalue;
        }
    }

    /**
     * 人脸识别对比服务
     */
    @RequestMapping(value = "getFaceComps", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getFaceComp(HttpServletRequest request) {
        String retvalue = "";
        logger.info("正在请求人脸识别对比服务........");
        try {
            JSONObject paramData = new JSONObject();
            paramData.put("id", request.getParameter("id"));
            paramData.put("id_photo", request.getParameter("id_photo"));
            // test
            // paramData.put("id_photo",
            // "3/uXf5P+eKJsSmwSvKvlMcp6UxyfIu5f44mcqfSgst3c0KWyWzwo+/7kP9+qb214nyJczW6fwQ799Q6XI11aG5mO6WWFNx9avJO29PlX8v8AfoIKX2Nu9pbN7/aX5orb2J/cX8qKCz//2Q==");
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // paramData.put("daily_photo",
            // request.getParameter("daily_photo"));
            // paramData.put("id_type", request.getParameter("id_type"));
            // paramData.put("daily_type", request.getParameter("daily_type"));
            retvalue = httpsUtil.postMsg(BRServiceUrl + "FaceCompServ", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("人脸识别对比服务验证请求异常:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            retvalue = retvalue.substring(0, retvalue.lastIndexOf("}") + 1);
            return retvalue;
        } else {
            return retvalue;
        }
    }

    /**
     * 
     * @Title: BankFourPro
     * @Description: 银行卡三要素验证
     * @author xt
     * @date 2017年5月5日 上午9:33:18
     * @param request
     * @return
     */

    @RequestMapping(value = "/BankThree", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getBankThree(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求银行卡三要素验证-------");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("bank_id", request.getParameter("bank_id"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "BankThree", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求银行卡三要素验证--BankFourPro_ERROR 输出--->:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: BankFourPro
     * @Description: 银行卡四要素验证
     * @author xt
     * @date 2017年5月5日 上午9:33:18
     * @param request
     * @return
     */

    @RequestMapping(value = "/BankFourPro", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getBankFourPro(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求银行卡四要素验证-------");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            paramData.put("bank_id", request.getParameter("bank_id"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "BankFourPro", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求银行卡四要素验证--BankFourPro_ERROR 输出--->:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: FaceRecog
     * @Description: 身份证人脸识别验证
     * @author xt
     * @date 2017年5月5日 上午9:33:18
     * @param request
     * @return
     */

    @RequestMapping(value = "/FaceRecog", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getFaceRecog(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("请求身份证人脸识别验证-------");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // paramData.put("daily_photo",
            // request.getParameter("daily_photo"));
            // test
            paramData.put("daily_photo",
                    "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");
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            paramData.put("daily_type", request.getParameter("daily_type"));
            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "FaceRecog", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求身份证人脸识别验证--FaceRecog_ERROR 输出--->:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * 
     * @Title: Location
     * @Description: 地址信息核对
     * @author mekio
     * @date 2017年5月8日 下午7:53:22
     * @param request
     * @return
     */
    @RequestMapping(value = "/Location", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String Location(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("前置连接成功！");
            logger.info("请求 Location 地址信息核对接口 ");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);
            if (request.getParameter("biz_addr") != null) {
                paramData.put("biz_addr", new String(request.getParameter("biz_addr").getBytes("iso-8859-1"), "utf-8"));
            }
            if (request.getParameter("home_addr") != null) {
                paramData.put("home_addr", new String(request.getParameter("home_addr").getBytes("iso-8859-1"), "utf-8"));
            }
            if (request.getParameter("per_addr") != null) {
                paramData.put("per_addr", new String(request.getParameter("per_addr").getBytes("iso-8859-1"), "utf-8"));
            }
            if (request.getParameter("apply_addr") != null) {
                paramData.put("apply_addr", new String(request.getParameter("apply_addr").getBytes("iso-8859-1"), "utf-8"));
            }
            if (request.getParameter("oth_addr") != null) {
                paramData.put("oth_addr", new String(request.getParameter("oth_addr").getBytes("iso-8859-1"), "utf-8"));
            }

            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "Location", "paramData=" + paramData.toString());
            logger.info("Location 输出：--->:" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

    /**
     * @Title: AccountChange_s
     * @Description: 收支等级评估专业版
     * @author xt
     * @date 2017年5月17日 下午4:37:55
     * @param request
     * @return
     */
    @RequestMapping(value = "/AccountChange_s", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String AccountChange_s(HttpServletRequest request) {
        String portrait_result = "";
        try {
            logger.info("前置 接收收支等级评估专业版请求");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            // 判断是否测试方式是否为批量，批量测试时合作机构编号为ZZDAILYREPORT
            String name = (("ZZDAILYREPORT".equals(request.getParameter("merchantId")))) ? (request.getParameter("name")) : (new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            paramData.put("name", name);

            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BRServiceUrl + "AccountChange_s", "paramData=" + paramData.toString());
        } catch (Exception e) {
            logger.error("请求收支等级评估专业版--AccountChange_s_ERROR 输出--->:" + e);
        }
        if (!"ZZDAILYREPORT".equals(request.getParameter("merchantId"))) {
            // 截取返回时间
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }

}
