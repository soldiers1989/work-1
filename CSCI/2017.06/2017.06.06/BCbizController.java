/**
     * 
     * @Title: VehInsurance
     * @Description: 车辆续保信息查询
     * @author ly
     * @date 2017年6月6日 上午11:27:13
     * @param request
     * @return
     */
    @RequestMapping(value = "/VehInsurance", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String VehInsurance(HttpServletRequest request) {
        String portrait_result = "";
        HashMap<String, String> tmap = new HashMap<String, String>();
        HashMap<String, String> mapp = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            logger.info("正在执行车辆续保信息查询！。。。。");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            tmap.put("API", "VehInsurance");
            tmap.put("REQ_PARAM", paramData);
            tmap.put("RAID", raid);
            tmap.put("TYPE", "1");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            tmap.put("MERCHANT_ID", merId);
            int re = transDtlService.insertTransDtl(tmap);// 记录一条客户请求的流水
            if (re != 1) {
                logger.error("车辆续保信息查询，插入请求流水失败！ ");
            }

            // 客户请求接口，需要调用一个数源接口，则记录一条map流水
            mapp.put("API", "bcVehInsurance");
            mapp.put("REQ_PARAM", paramData);
            mapp.put("RAID", raid);
            mapp.put("TYPE", "2");// 类型1表示 客户的请求报文；类型2表示 中证请求数源的报文
            mapp.put("MERCHANT_ID", merId);
            int ret = transDtlService.insertTransDtl(mapp);// 记录一条客户请求的流水
            if (ret != 1) {
                logger.error("车辆续保信息查询，插入请求数源流水失败！ ");
            }

            map.put("SID", raid);
            map.put("carNO", jsonObject.getString("carNO"));
            map.put("cityCode", jsonObject.getString("cityCode"));
            map.put("userName", jsonObject.getString("userName"));
            map.put("certCode", jsonObject.getString("certCode"));
            map.put("carCode", jsonObject.getString("carCode"));
            map.put("carDriverNO", jsonObject.getString("carDriverNO"));

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postBCUrl + "postBC_VehInsurance", "paramData=" + jsonObjectTr.toString());
            // -----------json解析修改start------------
            map = (HashMap<String, String>) JsonUtil.getJsonMap(portrait_result, map, null);
            // 如果查询无数据或者查询失败则不入库
            if (!map.get("CODE").equals("600000")) {
                return portrait_result;
            }

            logger.info("getVehInsuranceFaith_parmData 输出--->:" + portrait_result);
            // -----------json解析修改end------------
            vehInsuranceService.insert(map);
            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys1 = { "flag", "flag_vehinsurance" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys1, 2, flagmap);
                if (flagmap.get("FLAG_VEHINSURANCE").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }
        } catch (Exception e) {
            logger.error("getVehInsuranceFaith_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }