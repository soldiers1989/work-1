package com.zz.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.common.utils.zz.APIUtil;

@Controller
public class postBCController {

    /**
     * 
     * @Title: getResidencePost
     * @Description: 查询个人户籍信息
     * @author wyj
     * @date 2017年4月22日 下午2:56:38
     * @param request
     * @return
     */

    @RequestMapping(value = "/postBC_Residence", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getResidencePost(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "Residence", 1, "百川查询个人户籍信息");
    }

    /**
     * 
     * @Title: getVehInsurancePost
     * @Description: 车辆续保信息
     * @author ly
     * @date 2017年6月2日 17:08:15
     * @param request
     * @return
     */
    @RequestMapping(value = "/postBC_VehInsurance", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getVehInsurancePost(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "VehInsurance", 1, "车辆续保信息");
    }
    
    /**
     * 
     * @Title: getResidencePost
     * @Description: 房屋价格
     * @author
     * @date 2017年4月22日 下午2:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/postBC_Inquiry", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getInquiryPost(HttpServletRequest request) {
        return APIUtil.getPostResults(request, "HainaApi", "Inquiry", 1, "房屋价格");
    }

}
