/**
     * 用户年龄查询
     * 
     * @return
     */

    @RequestMapping(value = "/UserAge", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String UserAge(HttpServletRequest request) {
        String retvalue = null;
        String backTime = "0";
        Map<String, String> map = new HashMap<>();
        try {
            logger.info("server 处理用户年龄查询请求");
            String parmes = request.getParameter("paramData");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String[] orgMsg = baseService.getSysParam(request, "9005").split(";");
            String orgCode = orgMsg[0];
            String orgPwd = orgMsg[1];
            jsonObject.put("orgCode", orgCode);
            jsonObject.put("orgPwd", orgPwd);

            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{\"code\":\"2001\" ,\"msg\":\"合作机构编号错误\"}";
                return retvalue;// 直接返回错误信息给接口调用方
            }
            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("UserAge", parmes, raid, "1", merId);
            if (re != 1) {
                logger.error("用户年龄查询，插入请求流水失败！ ");
            }
            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("ltUserAge", parmes, raid, "2", merId);
            if (ret != 1) {
                logger.error("用户年龄查询，插入请求数源流水失败！ ");
            }
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");

            // id cell name 入IDENTI_MAPPING 表；且生成对应需要的markingCode
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "UserAge");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                // retvalue =
                // "{'code':'00','flag_specialList_c':'1','sl_id_court_bad':'0','sl_id_court_executed':'0','swift_number':'3000659_20170519165528_2391'}";

                retvalue = httpsUtil.postMsg(postLTUrl + "queryAge", "paramData=" + jsonObjectTr.toString());
                backTime = retvalue.substring(retvalue.lastIndexOf("}") + 1);

                logger.info("用户年龄查询返回值正在进行入库");
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmapLoweer(retvalue);
                map.put("TIME", Util.getCurrentDateTime());
                map.put("ID", raid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // -----------json解析修改end------------

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("code"), raid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("code").equals("00")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    userAgeService.Insert_UserAge(map);
                    if (type == 0) {
                        userAgeService.Insert_UserAge_S(map);
                    } else {
                        userAgeService.update_S(map);// 数据库入表实时表更新
                    }
                } else {
                    // 接口调用异常
                    retvalue = "{\"code\":\"2005\" ,\"msg\":\"接口异常\"}";
                    if (type == 0) {
                        return retvalue + backTime;
                    }
                }
                // -----------编码表的插入end------------

            }
            // -----------从数据库中获取数据进行json字符串拼接start------------
            // 根据markingCode找到最新的一条主表数据
            Map<String, String> mapM = new HashMap<>();
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.lt.UserAgeMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            retvalue = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(raid, map.get("status"), type);// 流水表更新
        } catch (Exception e) {
            logger.error("用户年龄查询异常:" + e);
        }
        return retvalue + backTime;
    }

}