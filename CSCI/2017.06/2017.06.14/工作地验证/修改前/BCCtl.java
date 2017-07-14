package com.zz.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.common.utils.string.StringUtil;
import com.utils.https.HttpsUtil;

@Controller
@RequestMapping("/bCCtl")
@SuppressWarnings("static-access")
public class BCCtl {

    private static final Logger logger = Logger.getLogger(BCCtl.class);

    @Value("${BCServiceUrl}")
    private String BCServiceUrl;

    private HttpsUtil httpsUtil = new HttpsUtil();

    /**
     * 
     * @Title: getResidencePre
     * @Description: 查询个人户籍信息
     * @author wyj
     * @date 2017年4月22日 下午2:57:29
     * @param request
     * @return
     */
    @RequestMapping(value = "/Residence", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getResidencePre(HttpServletRequest request) {
        String portrait_result = "";
        try {
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", request.getParameter("merchantId"));// 合作机构编号（作为客户标示来记录）
            paramData.put("id", request.getParameter("id"));
            paramData.put("cell", request.getParameter("cell"));
            paramData.put("name", new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8"));
            // 将json格式的对象转换成字符串 faith_Residence
            portrait_result = httpsUtil.postMsg(BCServiceUrl + "faith_Residence", "paramData=" + paramData.toString());

            logger.info("getResidencePre_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getResidencePre_ERROR 输出--->:" + e);
        }

        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }
    
    /**
     * 
     * @Title: getVehInsurance
     * @Description: 查询车辆续保信息
     * @author ly
     * @date 2017年6月2日 15:24:01
     * @param request
     * @return
     */
    @RequestMapping(value = "/VehInsurance", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getVehInsurance(HttpServletRequest request, String carNO, String cityCode, String userName
            , String certCode, String carCode, String carDriverNO, String merchantId) {
        String portrait_result = "";
        try {
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("carNO", new String(carNO.getBytes("iso-8859-1"), "utf-8"));
            paramData.put("cityCode", cityCode);
            paramData.put("userName", new String(StringUtil.isNull(userName).getBytes("iso-8859-1"), "utf-8"));
            paramData.put("certCode", StringUtil.isNull(certCode));
            paramData.put("carCode", StringUtil.isNull(carCode));
            paramData.put("carDriverNO", StringUtil.isNull(carDriverNO));
            // 将json格式的对象转换成字符串 faith_VehInsurance
            portrait_result = httpsUtil.postMsg(BCServiceUrl + "VehInsurance", "paramData=" + paramData.toString());

            logger.info("getVehInsurance_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getVehInsurance_ERROR 输出--->:" + e);
        }

        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }
    
    /**
     * 
     * @Title: Inquiry
     * @Description: 房屋价格查询
     * @author 
     * @date 
     * @param request
     * @return
     */
    @RequestMapping(value = "/Inquiry", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String AccountChange(HttpServletRequest request, String city_name, String house_type, String filter, String area,
             String merchantId, String room_type, String floor_building, String builted_time, String floor, String totalfloor, String toward, 
             String special_factors,String hall, String toilet, String house_number, String position, String renovation) {
        String portrait_result = "";
        try {
            logger.info("前置 房屋价格查询接口");
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();

            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("city_name", city_name);
            paramData.put("house_type", new String(house_type.getBytes("iso-8859-1"), "utf-8"));//必传住宅类型
            paramData.put("filter", new String(filter.getBytes("iso-8859-1"), "utf-8"));//必传小区名称
            paramData.put("area", area);//必传面积
            paramData.put("room_type", room_type);//选填
            paramData.put("floor_building", floor_building);
            paramData.put("builted_time", builted_time);
            paramData.put("floor", floor);
            paramData.put("totalfloor", totalfloor);
            paramData.put("toward", toward);
            paramData.put("special_factors", special_factors);
            paramData.put("hall", hall);
            paramData.put("toilet", toilet);
            paramData.put("house_number", house_number);
            if (request.getParameter("position") != null) {
                paramData.put("position", new String(position.getBytes("iso-8859-1"), "utf-8"));
            }
            if (request.getParameter("renovation") != null) {
                paramData.put("renovation", new String(renovation.getBytes("iso-8859-1"), "utf-8"));
            }
            // paramData.put("position", new
            // String(position.getBytes("iso-8859-1"), "utf-8"));
            // paramData.put("renovation", new
            // String(renovation.getBytes("iso-8859-1"), "utf-8"));


            // 将json格式的对象转换成字符串
            portrait_result = httpsUtil.postMsg(BCServiceUrl + "faith_Inquiry", "paramData=" + paramData.toString());
            logger.info("HousePrice 输出：--->:" + portrait_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }
}
