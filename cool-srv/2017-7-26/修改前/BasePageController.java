package com.cf.consoleAdmin.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasePageController{
    @RequestMapping("tologin")
    public String toLogin() throws Exception {
        return "login";
    }
    @RequestMapping("main")
    public String main() throws Exception {
        return "main";
    }
    @RequestMapping("deptMaintain")
    public String deptMaintain() throws Exception {
        return "sys/deptMaintain";
    }
    @RequestMapping("roleMaintain")
    public String roleMaintain() throws Exception {
        return "sys/roleMaintain";
    }
    @RequestMapping("userMaintain")
    public String userMaintain() throws Exception {
        return "sys/userMaintain";
    }
    @RequestMapping("qryLoginAudits")
    public String qryLoginAudits() throws Exception {
        return "sysQry/qryLoginAudits";
    }
    @RequestMapping("qryOperateAudits")
    public String qryOperateAudits() throws Exception {
        return "sysQry/qryOperateAudits";
    }
    @RequestMapping("onlineUserManage")
    public String onlineUserManage() throws Exception {
        return "sys/onlineUserManage";
    }
    @RequestMapping("sysParamMaintain")
    public String sysParamMaintain() throws Exception {
        return "sys/sysParamMaintain";
    }
    @RequestMapping("dictMaintain")
    public String dictMaintain() throws Exception {
        return "sys/dictMaintain";
    }
    @RequestMapping("inforMaintain")
    public String inforMaintain() throws Exception {
        return "api/inforMaintain";
    }


}
