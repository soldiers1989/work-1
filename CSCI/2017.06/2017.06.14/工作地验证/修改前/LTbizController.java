package com.zz.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.utils.http.HttpsUtil;

@Controller
@RequestMapping("/lt")
@SuppressWarnings("static-access")
public class LTbizController {

    private static final Logger logger = Logger.getLogger(LTbizController.class);

    @Value("${postLTUrl}")
    private String postLTUrl;

    private static HttpsUtil httpsUtil = new HttpsUtil();

    @RequestMapping("/CommFuz")
    public String CommFuz() {
        try {
            httpsUtil.postMsg(postLTUrl, "");
        } catch (Exception e) {
            logger.error(e);
        }
        return "";
    }

}
