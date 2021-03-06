@RequestMapping(value = "/TelStateCTCC_y", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String TelStateCTCC_y(HttpServletRequest request) {
        String portrait_result = "";
        String backTime = "0";
        Map<String, String> map = new HashMap<String, String>();
        try {
            logger.info("请求电信手机在网状态-------");
            String paramData = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(paramData);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                portrait_result = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return portrait_result;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("TelStateCTCC_y", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("电信手机在网状态查询，插入请求流水失败！ ");
            }
            int ret = transDtlService.insertTransDtl("brTelStateCTCC_y", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("电信手机在网状态查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "TelStateCTCC_y");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                portrait_result = httpsUtil.postMsg(postBRUrl + "TelStateCTCC_yPost", "paramData=" + jsonObjectTr.toString());
                backTime = portrait_result.substring(portrait_result.lastIndexOf("}") + 1);
                portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
                // -----------json解析修改start------------
                map = JsonUtil.getJsonMap(portrait_result, map, null);
                map.put("CREATE_TIME", Util.getCurrentDateTime());
                map.put("SID", raid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // -----------json解析修改end------------
                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("CODE"), raid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }
                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("code".toUpperCase()).equals("600000")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    telStateCTCCyService.insert(map);
                    if (type == 0) {
                        telStateCTCCyService.insertS(map);
                    } else {
                        telStateCTCCyService.updateS(map);// 数据库入表实时表更新
                    }
                } else {
                    // 接口调用异常
                    portrait_result = "{\"code\":\"2005\" ,\"msg\":\"接口异常\"}";
                    if (type == 0) {
                        return portrait_result + backTime;
                    }
                }
                // -----------编码表的插入end------------
            }

            // -----------从数据库中获取数据进行json字符串拼接start------------
            // 根据markingCode找到最新的一条主表数据
            Map<String, String> mapM = new HashMap<>();

            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.TelStateCTCCyMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            portrait_result = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------

            transDtlService.updateTransDtl_N(raid, String.valueOf(map.get("FLAG_TelStateCTCC_y".toUpperCase())), type);// 更新流水收费标志
        } catch (Exception e) {
            logger.error("电信手机在网状态查询:" + e);
        }
        return portrait_result + backTime;
    }