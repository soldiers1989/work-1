@RequestMapping(value = "/VehInsurance", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String VehInsurance(HttpServletRequest request) {
        String portrait_result = "";
        String backTime = "0";
        Map<String, String> map = new HashMap<String, String>();
        try {
            logger.info("server 处理车辆续保信息查询请求");
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
            int re = transDtlService.insertTransDtl("VehInsurance", paramData, raid, "1", merId);
            if (re != 1) {
                logger.error("车辆续保信息查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("bcVehInsurance", paramData, raid, "2", merId);
            if (ret != 1) {
                logger.error("车辆续保信息查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            JSONObject jsonObjectMc = jsonObjectTr;
            jsonObjectMc = jsonObjectMc.discard("userName");
            jsonObjectMc = jsonObjectMc.discard("certCode");
            jsonObjectMc = jsonObjectMc.discard("carCode");
            jsonObjectMc = jsonObjectMc.discard("carDriverNO");
            

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectMc, "VehInsurance");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                portrait_result = httpsUtil.postMsg(postBCUrl + "postBC_VehInsurance", "paramData=" + jsonObjectTr.toString());
                backTime = portrait_result.substring(portrait_result.lastIndexOf("}") + 1);
                portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(portrait_result);
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
                    vehInsuranceService.insert(map);
                    if (type == 0) {
                        vehInsuranceService.insertS(map);
                    } else {
                        vehInsuranceService.updateS(map);// 数据库入表实时表更新
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
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.bc.BCVehInsuranceMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            portrait_result = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------

            transDtlService.updateTransDtl_N(raid, String.valueOf(map.get("FLAG_VEHINSURANCE")), type);// 更新流水收费标志
        } catch (Exception e) {
            logger.error("车辆续保信息查询 :" + e);
        }
        return portrait_result + backTime;
    }