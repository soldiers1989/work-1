/**
     * 
     * @Title: Consumption_c
     * @Description: 商品消费评估查询
     * @author ly
     * @date 2017年6月22日 下午5:51:20
     * @param request
     * @return
     */
    @RequestMapping(value = "/Consumption_c", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Consumption_c(HttpServletRequest request) {
        String retvalue = null;
        String flag = "";
        String backTime = "0";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行Consumption_c。。。。。");
            String parmes = request.getParameter("parmes");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String raid = Util.getUUID();

            long startHZBH = System.currentTimeMillis();
            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("Consumption_c", parmes, raid, "1", merId);
            if (re != 1) {
                logger.error("商品消费评估查询，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brConsumption_c", parmes, raid, "2", merId);
            if (ret != 1) {
                logger.error("商品消费评估查询，插入请求数源流水失败！ ");
            }
            long endHZBH = System.currentTimeMillis();
            logger.info("请求流水时间：" + (endHZBH - startHZBH));
            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "Consumption_c");
            String marking_cod = map_code.get("MARKING_CODE");
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                // retvalue =
                // "{'code':'00','flag_specialList_c':'1','sl_id_court_bad':'0','sl_id_court_executed':'0','swift_number':'3000659_20170519165528_2391'}";

                retvalue = httpsUtil.postMsg(postBRUrl + "brConsumption_c", "paramData=" + jsonObjectTr.toString());
                backTime = retvalue.substring(retvalue.lastIndexOf("}") + 1);

                logger.info("商品消费评估查询返回值正在进行入库");
                logger.info(retvalue + ":" + retvalue);
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(retvalue);
                map.put("TIME", Util.getCurrentDateTime());
                map.put("ID", raid);
                map.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                // 取flag
                flag = map.get("flag_consumption_c".toUpperCase());

                // 更新请求报文的返回状态及返回时间
                int rett = transDtlService.updateRequestMsg(map.get("code"), raid);
                if (rett < 0) {
                    logger.info("更新报文返回状态及时间失败");
                }

                // -----------编码表的插入start(根据第三方请求返回数据的Code进行判断是否请求成功有数据，若果是，则插入)------------
                if (map.get("CODE").equals("00")) {// 返回成功有数据的情况下，才会插入所有的表
                    if (type == 0) {// 两种处理情况，1、返回成功有数据 2、返回成功无数据
                        transDtlService.insertIdentiMapping(map_code);
                    }
                    conSumptionc.intsertConsumptionc(map);
                    if (type == 0) {
                        conSumptionc.intsertConsumptioncS(map);
                    } else {
                        conSumptionc.updateConsumptioncS(map);// 数据库入表实时表更新
                    }

                    // 更新流水收费标志
                    if (String.valueOf(map.get("FLAG_CONSUMPTION_C")).equals("1")) {
                        flagmap.put("IS_FEE", "1");
                        flagmap.put("TID", raid);
                    } else {
                        flagmap.put("IS_FEE", "0");
                        flagmap.put("TID", raid);
                    }
                    transDtlService.updateTransDtl(flagmap);

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
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.ConsumptioncMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            retvalue = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(raid, flag, type);// 流水表更新
        } catch (Exception e) {
            logger.error("商品消费评估查询异常:" + e);
        }
        return retvalue + backTime;
    }