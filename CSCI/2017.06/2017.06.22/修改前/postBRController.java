package com.zz.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.log4testng.Logger;

import com.cf.common.utils.zz.APIUtil;

@Controller
public class postBRController {

    private static final Logger logger = Logger.getLogger(postBRController.class);

    @RequestMapping(value = "/EduLevel", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String EduLevel(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "EduLevel", 1, "学历查询");
    }

    /**
     * 
     * @Title: AccountChange_u
     * @Description: 月度收支等级评估
     * @author wyj
     * @date 2017年4月22日 下午2:56:38
     * @param request
     * @return
     */

    @RequestMapping(value = "/AccountChange_u", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String AccountChange_u(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "BankServer3Api", "AccountChange_u", 2, "月度收支等级评估");
    }

    /**
     * 特殊名单核查
     * 
     * @return
     */
    @RequestMapping(value = "/brSpecialList_c", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String brSpecialList_c(HttpServletRequest request) {

        return APIUtil.getPostResults(request, "BankServer3Api", "SpecialList_c", 2, "百融特殊名单核查");
    }

    /**
     * 法院被执行人—个人
     * 
     * @return
     */
    @RequestMapping(value = "/brExecution", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Execution(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "BankServer3Api", "Execution", 2, "百融法院被执行人—个人");
    }

    /**
     * 商品消费评估
     * 
     * @return
     */
    @RequestMapping(value = "brConsumption_c", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Consumption_c(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "BankServer3Api", "Consumption_c", 2, "百融商品消费评估");
    }

    /**
     * 手机在网状态
     * 
     * @return
     */
    @RequestMapping(value = "brTelStatus", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelStatusCMCC_f(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "TelStatus", 1, "百融手机在网状态");
    }

    /**
     *
     * @Title: brCrimeInfo
     * @Description: 个人不良记录
     * @author mekio
     * @date 2017年4月24日 下午8:01:34
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brCrimeInfo", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String brCrimeInfo(HttpServletRequest request) throws Exception {

        logger.info("后置接收个人不良信息查询请求");
        return APIUtil.getPostResults(request, "HainaApi", "CrimeInfo_Pro", 1, "百融个人不良信息记录");
    }

    /**
     * @Title: brTelStatusCMCCf
     * @Description:移动手机在网状态
     * @author mekio
     * @date 2017年4月20日 下午7:26:17
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brTelStatusCMCCf", produces = "text/html;charset=UTF-8")
    public String brTelStatusCMCCf(HttpServletRequest request) throws Exception {
        return APIUtil.getPostResults(request, "HainaApi", "TelStatusCMCC_f", 1, "百融移动手机在网状态");
    }

    /**
     * @Title: brTelPeriodCTCCy
     * @Description:电信手机在线时长
     * @author mekio
     * @date 2017年4月20日 下午7:27:30
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brTelPeriodCTCCy", produces = "text/html;charset=UTF-8")
    public String brTelPeriodCTCCy(HttpServletRequest request) throws Exception {
        return APIUtil.getPostResults(request, "HainaApi", "TelPeriodCTCC_y", 1, "百融电信手机在线时长");
    }

    /**
     * @Title: brTelPeriodCTCCy
     * @Description:联通手机在网时长
     * @author mekio
     * @date 2017年4月20日 下午7:27:30
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brTelPeriodCUCCd", produces = "text/html;charset=UTF-8")
    public String brTelPeriodCUCCd(HttpServletRequest request) throws Exception {
        return APIUtil.getPostResults(request, "HainaApi", "TelPeriodCUCC_d", 1, "百融联通手机在网时长");
    }

    /**
     * @Title: brTelPeriodCTCCy
     * @Description:移动手机在网时长
     * @author mekio
     * @date 2017年4月20日 下午7:27:30
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brTelPeriodCMCCf", produces = "text/html;charset=UTF-8")
    public String brTelPeriodCMCCf(HttpServletRequest request) throws Exception {
        return APIUtil.getPostResults(request, "HainaApi", "TelPeriodCMCC_f", 1, "百融移动手机在网时长");
    }

    /**
     * 
     * @Title: brAccountChangeDer
     * @Description: 百融收支等级记录
     * @author mekio
     * @date 2017年5月5日 下午7:50:50
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brAccountChangeDer", produces = "text/html;charset=UTF-8")
    public String brAccountChangeDer(HttpServletRequest request) throws Exception {
        return APIUtil.getPostResults(request, "BankServer3Api", "AccountChangeDer", 2, "百融收支等级记录");
    }

    /**
     * 
     * @Title: TelStateCUCC_dPost
     * @Description: 联通手机在网状态
     * @author wyj
     * @date 2017年4月22日 下午2:56:38
     * @param request
     * @return
     */

    @RequestMapping(value = "/TelStateCUCC_dPost", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelStateCUCC_dPost(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "TelStateCUCC_d", 1, "百融联通手机在网状态");
    }

    /**
     * 
     * @Title: TelStateCTCC_yPost
     * @Description: 电信手机在网状态
     * @author wyj
     * @date 2017年4月22日 下午2:56:38
     * @param request
     * @return
     */

    @RequestMapping(value = "/TelStateCTCC_yPost", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelStateCTCC_yPost(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "TelStateCTCC_y", 1, "百融电信手机在网状态");
    }

    /**
     * 
     * @Title: PerInvest_qPost
     * @Description: 个人对外投资v2查询
     * @author wyj
     * @date 2017年5月5日 下午6:45:13
     * @param request
     * @return
     */
    @RequestMapping(value = "/PerInvest_qPost", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String PerInvest_qPost(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "PerInvest_q", 1, "百融个人对外投资v2查询");
    }

    /**
     * 身份证二要素验证
     */
    @RequestMapping(value = "brIdTwo", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getIdTwo_z(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "IdTwo_z", 1, "身份证二要素验证");
    }

    /**
     * 银行卡三要素验证
     * 
     * @return
     */
    @RequestMapping(value = "brBankThree", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String brBankThree(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "BankThree", 1, "银行卡三要素验证");
    }

    /**
     * 银行卡四要素验证
     * 
     * @return
     */
    @RequestMapping(value = "brBankFourPro", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String brBankFourPro(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "BankFourPro", 1, "银行卡四要素验证");
    }

    /**
     * 身份证人脸识别验证
     * 
     * @return
     */
    @RequestMapping(value = "brFaceRecog", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String brFaceRecog(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "FaceRecog", 1, "身份证人脸识别验证");
    }

    /**
     * 身份证验证及照片查询
     */

    @RequestMapping(value = "brIdTwophotoServ", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String brFaceComp(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "IdTwo_photo", 1, "身份证验证及照片查询");
    }

    /**
     * 人脸识别对比服务
     * 
     * @return
     */
    @RequestMapping(value = "brFaceCompServ", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String brFaceCompServ(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "FaceComp", 1, "人脸识别对比服务接口");
    }

    /**
     * 
     * @Title: brLocation
     * @Description: 后置百融地址信息核对
     * @author mekio
     * @date 2017年5月8日 下午8:29:27
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/brLocation", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String brLocation(HttpServletRequest request) throws Exception {
        logger.info("brLocation 后置连接成功 ");
        return APIUtil.getPostResults(request, "BankServer3Api", "Location", 2, "百融地址信息核对");
    }

    /**
     * 
     * @Title: Consumption_c
     * @Description:收支等级评估专业版
     * @author xt
     * @date 2017年5月17日 下午5:04:00
     * @param request
     * @return
     */
    @RequestMapping(value = "brAccountChange_s", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String AccountChange_s(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "BankServer3Api", "AccountChange_s", 2, "收支等级评估专业版");
    }
}
