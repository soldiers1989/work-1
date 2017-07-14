package com.zz.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.common.utils.zz.LTUtil;

@Controller
public class postLTController {

    private static final String address = "http://60.10.25.208:8080/crp_test/inter/";

    /**
     * 
     * @Title: userCheck
     * @Description: 三要素验证
     * @author wyj
     * @date 2017年5月14日 上午1:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/userCheck", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String userCheck(HttpServletRequest request) {
        return LTUtil.getPostResults(request, address + "check/userCheck.do", "三要素验证");
    }

    /**
     * 
     * @Title: gxminHouse
     * @Description: 居住地验证
     * @author wyj
     * @date 2017年5月14日 上午1:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/gxminHouse", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String gxminHouse(HttpServletRequest request) {
        return LTUtil.getPostResults(request, address + "distance/gxminHouse.do", "居住地验证");
    }

    /**
     * 
     * @Title: gxminWork
     * @Description: 工作地验证
     * @author wyj
     * @date 2017年5月14日 上午1:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/gxminWork", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String gxminWork(HttpServletRequest request) {
        return LTUtil.getPostResults(request, address + "distance/gxminWork.do", "工作地验证");
    }

    /**
     * 
     * @Title: sexQuery
     * @Description: 性别查询
     * @author wyj
     * @date 2017年5月14日 上午1:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/sexQuery", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String sexQuery(HttpServletRequest request) {
        return LTUtil.getPostResults(request, address + "check/sexQuery.do", "性别查询");
    }

    /**
     * 
     * @Title: queryAge
     * @Description: 年龄查询
     * @author wyj
     * @date 2017年5月14日 上午1:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryAge", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryAge(HttpServletRequest request) {
        return LTUtil.getPostResults(request, address + "check/queryAge.do", "年龄查询");
    }

    /**
     * 
     * @Title: userWhite
     * @Description: 白名单查询
     * @author wyj
     * @date 2017年5月14日 上午1:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/userWhite", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String userWhite(HttpServletRequest request) {
        return LTUtil.getPostResults(request, address + "call/userWhite.do", "白名单查询");
    }

    /**
     * 
     * @Title: newWoSix
     * @Description: 沃信用分查询
     * @author wyj
     * @date 2017年5月14日 上午1:56:38
     * @param request
     * @return
     */
    @RequestMapping(value = "/newWoSix", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String newWoSix(HttpServletRequest request) {
        return LTUtil.getPostResults(request, address + "check/newWoSix.do", "沃信用分查询");
    }

}
