@RequestMapping(value = "/faith_Residence", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getResidenceFaith(HttpServletRequest request) {
        String portrait_result = "";
        HashMap<String, String> flagmap = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            logger.info("正在执行个人户籍信息查询！。。。。");
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
            int re = transDtlService.insertTransDtl("Residence", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("个人户籍信息查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("bcResidence", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("个人户籍信息查询，插入请求数源流水失败！ ");
            }

            map.put("SID", raid);
            map.put("ID", jsonObject.getString("id"));
            map.put("CELL", jsonObject.getString("cell"));
            map.put("NAME", jsonObject.getString("name"));

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            portrait_result = httpsUtil.postMsg(postBCUrl + "postBC_Residence", "paramData=" + jsonObjectTr.toString());
            // -----------json解析修改start------------
            String[] keyRep = { "code;last", "api_status;last" };
            map = (HashMap<String, String>) JsonUtil.getJsonMap(portrait_result, map, keyRep);
            // 如果查询无数据或者查询失败则不入库
            if (!map.get("code").equals("600000")) {
                return portrait_result;
            }

            logger.info("getResidenceFaith_parmData 输出--->:" + portrait_result);
            // -----------json解析修改end------------
            residenceService.insert(map);
            // 更新流水收费标志
            if ("".equals(portrait_result) || null == portrait_result) {
                logger.error("无返回 ");
            } else {
                logger.info(portrait_result + ":" + portrait_result);
                String[] keys1 = { "flag", "flag_residence" };
                flagmap = (HashMap<String, String>) JsonUtil.getJsonObject(portrait_result, keys1, 2, flagmap);
                if (flagmap.get("FLAG_RESIDENCE").equals("1")) {
                    flagmap.put("IS_FEE", "1");
                    flagmap.put("TID", raid);
                } else {
                    flagmap.put("IS_FEE", "0");
                    flagmap.put("TID", raid);
                }
                transDtlService.updateTransDtl(flagmap);
            }

        } catch (Exception e) {
            logger.error("getResidenceFaith_ERROR 输出--->:" + e);
        }
        return portrait_result;
    }