package com.cf.biz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @ClassName: LoginController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mekio
 * @date 2017年4月20日 下午1:27:24
 *
 */
@Controller
public class BasePageController {

    @RequestMapping(value = "/tologin", method = { RequestMethod.GET, RequestMethod.POST })
    public String toLogin() {
        return "/login";
    }

    @RequestMapping(value = "/main", method = { RequestMethod.GET, RequestMethod.POST })
    public String main() {
        return "/main";
    }

    @RequestMapping(value = "/roleMaintain", method = { RequestMethod.GET, RequestMethod.POST })
    public String roleMaintain() {
        return "/sys/roleMaintain";
    }

    @RequestMapping(value = "/userMaintain", method = { RequestMethod.GET, RequestMethod.POST })
    public String userMaintain() {
        return "/sys/userMaintain";
    }

    @RequestMapping(value = "/qryLoginAudits", method = { RequestMethod.GET, RequestMethod.POST })
    public String qryLoginAudits() {
        return "/sysQry/qryLoginAudits";
    }

    @RequestMapping(value = "/qryOperateAudits", method = { RequestMethod.GET, RequestMethod.POST })
    public String qryOperateAudits() {
        return "/sysQry/qryOperateAudits";
    }

    @RequestMapping(value = "/onlineUserManage", method = { RequestMethod.GET, RequestMethod.POST })
    public String onlineUserManage() {
        return "/sys/onlineUserManage";
    }

    @RequestMapping(value = "/sysParamMaintain", method = { RequestMethod.GET, RequestMethod.POST })
    public String sysParamMaintain() {
        return "/sys/sysParamMaintain";
    }

    @RequestMapping(value = "/dictMaintain", method = { RequestMethod.GET, RequestMethod.POST })
    public String dictMaintain() {
        return "/sys/dictMaintain";
    }

    @RequestMapping(value = "/roleMaintainCheck", method = { RequestMethod.GET, RequestMethod.POST })
    public String roleMaintainCheck() {
        return "/sys/roleMaintainCheck";
    }

    @RequestMapping(value = "/userMaintainCheck", method = { RequestMethod.GET, RequestMethod.POST })
    public String userMaintainCheck() {
        return "/sys/userMaintainCheck";
    }

    @RequestMapping(value = "sysParamMaintainCheck", method = { RequestMethod.GET, RequestMethod.POST })
    public String sysParamMaintainCheck() {
        return "/sys/sysParamMaintainCheck";
    }

    @RequestMapping(value = "/dictMaintainCheck", method = { RequestMethod.GET, RequestMethod.POST })
    public String dictMaintainCheck() {
        return "/sys/dictMaintainCheck";
    }

    @RequestMapping(value = "/msgManage", method = { RequestMethod.GET, RequestMethod.POST })
    public String fsTransationData() {
        return "/zz/msgManage";
    }

    @RequestMapping(value = "/interfaceFee", method = { RequestMethod.GET, RequestMethod.POST })
    public String filedown() {
        return "/zz/interfaceFee";
    }

    @RequestMapping(value = "/uploadImportFile", method = { RequestMethod.GET, RequestMethod.POST })
    public String bizTranDetail() {
        return "/zz/bizTranDetail";
    }

    // 合作机构维护
    @RequestMapping(value = "/merchentMaintain", method = { RequestMethod.GET, RequestMethod.POST })
    public String merchentMaintain() {
        return "/zz/merchantDetail";
    }

    // 合作机构审核
    @RequestMapping(value = "/merchantMaintainCheck", method = { RequestMethod.GET, RequestMethod.POST })
    public String merchantMaintainCheck() {
        return "/zz/merchantMaintainCheck";
    }
}
