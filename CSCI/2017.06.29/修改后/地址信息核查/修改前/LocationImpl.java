package com.zz.service.br.impl;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfibatis.impl.template.MyBatisSessionTemplate;
import com.utils.http.HttpsUtil;
import com.zz.service.br.LocationService;

import net.sf.json.JSONObject;

/**
 * 地址信息核查操作类
 */
@Service("locationService")
public class LocationImpl implements LocationService {

    @Autowired
    private static final Logger logger = Logger.getLogger(LocationImpl.class);

    @Autowired
    private MyBatisSessionTemplate myBatisSessionTemplate;

    private static HttpsUtil httpsUtil = new HttpsUtil();

    @SuppressWarnings("static-access")
    @Override
    public String skipTo(String paramData, String postBRUrl, String raid) {

        String portrait_result = "";
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObject = JSONObject.fromObject(paramData);
        try {
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postBRUrl + "brLocation", "paramData=" + jsonObjectTr.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        JSONObject jsonObj = JSONObject.fromObject(portrait_result);
        String code = (String) jsonObj.get("code");
        if (!code.equals("00")) {
            return portrait_result;
        }

        map.put("ID_CODE", jsonObject.getString("id"));
        map.put("CELL", jsonObject.getString("cell"));
        map.put("NAME", jsonObject.getString("name"));
        if (jsonObject.has("biz_addr")) {
            map.put("BIZ_ADDR", jsonObject.getString("biz_addr"));
        }
        if (jsonObject.has("home_addr")) {
            map.put("HOME_ADDR", jsonObject.getString("home_addr"));
        }
        if (jsonObject.has("per_addr")) {
            map.put("PER_ADDR", jsonObject.getString("per_addr"));
        }
        if (jsonObject.has("apply_addr")) {
            map.put("APPLY_ADDR", jsonObject.getString("apply_addr"));
        }
        if (jsonObject.has("oth_addr")) {
            map.put("OTH_ADDR", jsonObject.getString("oth_addr"));
        }

        // String uid = Util.getUUID();
        map.put("SEQ", raid);
        try {
            // 数据入表
            map.put("FLAG_LOCATION", (String) jsonObj.get("flag_location"));
            map.put("LOCATION_BIZ_ADDR1", (String) jsonObj.get("location_biz_addr1"));
            map.put("LOCATION_BIZ_ADDR2", (String) jsonObj.get("location_biz_addr2"));
            map.put("LOCATION_BIZ_ADDR3", (String) jsonObj.get("location_biz_addr3"));
            map.put("LOCATION_BIZ_ADDR4", (String) jsonObj.get("location_biz_addr4"));
            map.put("LOCATION_BIZ_ADDR5", (String) jsonObj.get("location_biz_addr5"));
            map.put("LOCATION_HOME_ADDR1", (String) jsonObj.get("location_home_addr1"));
            map.put("LOCATION_HOME_ADDR2", (String) jsonObj.get("location_home_addr2"));
            map.put("LOCATION_HOME_ADDR3", (String) jsonObj.get("location_home_addr3"));
            map.put("LOCATION_HOME_ADDR4", (String) jsonObj.get("location_home_addr4"));
            map.put("LOCATION_HOME_ADDR5", (String) jsonObj.get("location_home_addr5"));
            map.put("LOCATION_PER_ADDR1", (String) jsonObj.get("location_per_addr1"));
            map.put("LOCATION_PER_ADDR2", (String) jsonObj.get("location_per_addr2"));
            map.put("LOCATION_PER_ADDR3", (String) jsonObj.get("location_per_addr3"));
            map.put("LOCATION_PER_ADDR4", (String) jsonObj.get("location_per_addr4"));
            map.put("LOCATION_PER_ADDR5", (String) jsonObj.get("location_per_addr5"));
            map.put("LOCATION_APPLY_ADDR1", (String) jsonObj.get("location_apply_addr1"));
            map.put("LOCATION_APPLY_ADDR2", (String) jsonObj.get("location_apply_addr2"));
            map.put("LOCATION_APPLY_ADDR3", (String) jsonObj.get("location_apply_addr3"));
            map.put("LOCATION_APPLY_ADDR4", (String) jsonObj.get("location_apply_addr4"));
            map.put("LOCATION_APPLY_ADDR5", (String) jsonObj.get("location_apply_addr5"));
            map.put("LOCATION_OTH_ADDR1", (String) jsonObj.get("location_oth_addr1"));
            map.put("LOCATION_OTH_ADDR2", (String) jsonObj.get("location_oth_addr2"));
            map.put("LOCATION_OTH_ADDR3", (String) jsonObj.get("location_oth_addr3"));
            map.put("LOCATION_OTH_ADDR4", (String) jsonObj.get("location_oth_addr4"));
            map.put("LOCATION_OTH_ADDR5", (String) jsonObj.get("location_oth_addr5"));

            myBatisSessionTemplate.insert("orm.mapper.zz.br.LocationMapper.insert", map);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return portrait_result;
    }

}
