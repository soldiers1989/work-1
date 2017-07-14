/**
     * 
     * @Title: Execution
     * @Description: 法院被执行人—个人
     * @author ly
     * @date 2017年6月22日 下午5:51:20
     * @param request
     * @return
     */
    @RequestMapping(value = "/Execution", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Execution(HttpServletRequest request) {
        String retvalue = null;
        String backTime = "0";
        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, String> flagmap = new HashMap<String, String>();
        try {
            logger.info("正在执行Execution。。。。。");
            String parmes = request.getParameter("parmes");
            // 将json格式的字符串转换成JSONObject
            JSONObject jsonObject = JSONObject.fromObject(parmes);
            String raid = Util.getUUID();

            // 判别合作机构编号是否正确，如果不正确，则直接返回 —— 合作机构编号错误
            String merId = jsonObject.getString("merchantId");
            int MerRet = transDtlService.seleMerchant(merId);
            if (MerRet < 0) {
                retvalue = "{'code':700000 ,'msg':'合作机构编号错误' }";
                return retvalue;// 直接返回错误信息给接口调用方
            }

            // 记录客户请求流水
            int re = transDtlService.insertTransDtl("Execution", parmes, raid, "1", merId);
            if (re != 1) {
                logger.error("法院被执行人—个人，插入请求流水失败！ ");
            }

            // 记录请求数源流水
            int ret = transDtlService.insertTransDtl("brExecution", parmes, raid, "2", merId);
            if (ret != 1) {
                logger.error("法院被执行人—个人，插入请求数源流水失败！ ");
            }

            // 去后置请求数据
            JSONObject jsonObjectTr = jsonObject.discard("merchantId");
            retvalue = httpsUtil.postMsg(postBRUrl + "brExecution", "paramData=" + jsonObjectTr.toString());
            // id cell name 入IDENTI_MAPPING 表；且生成对应需要mapping的
            // marking_code(暂时以uuid的方式来生成marking_code) 将jsonObject作为参数传入进行
            // 如果id cell name 在表中已经存在对应的值则取该值，如果不存在则生成
            Map<String, String> map_code = transDtlService.getMarkingCode(request, jsonObjectTr, "Execution");
            String marking_cod = map_code.get("MARKING_CODE");
            String tim = Util.getCurrentDateTime();
            int type = Integer.parseInt(map_code.get("type"));// 0：需重新请求带三方接口获取数据1：数据库有数据超时，需重新获取2：数据库有数据未超时，正常
            // 判断数据库是否有数据，如果有则直接拼接，如果没有或超时需从第三方获取插入数据库后再拼接
            if (type != 2) {
                // retvalue =
                // "{'code':'00','flag_specialList_c':'1','sl_id_court_bad':'0','sl_id_court_executed':'0','swift_number':'3000659_20170519165528_2391'}";

                retvalue = httpsUtil.postMsg(postBRUrl + "brExecution", "paramData=" + jsonObjectTr.toString());
                backTime = retvalue.substring(retvalue.lastIndexOf("}") + 1);

                logger.info("法院被执行人—个人返回值正在进行入库");
                logger.info(retvalue + ":" + retvalue);
                // -----------json解析修改start------------
                map = JsonFormts.getInstantiation().getmap(retvalue);
                // -----------json解析修改end------------

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
                    // 判断数据库是有数据超时还是无数据，超时，实时表主表更新，历史表直接插入
                    // 主表插入
                    Map<String, String> onemap = new HashMap<>();
                    Map<String, String> twomap = new HashMap<>();
                    Map<String, String> threemap = new HashMap<>();
                    Map<String, String> fourmap = new HashMap<>();
                    Map<String, String> fivemap = new HashMap<>();
                    Map<String, String> sixmap = new HashMap<>();
                    Map<String, String> sevenmap = new HashMap<>();
                    Map<String, String> eightmap = new HashMap<>();
                    Map<String, String> ninetmap = new HashMap<>();
                    Map<String, String> tenmap = new HashMap<>();
                    Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> maps = iterator.next();
                        if (maps.getKey().indexOf("1") > 0) {
                            onemap.put(maps.getKey(), maps.getValue());
                        }
                        if (maps.getKey().indexOf("2") > 0) {
                            twomap.put(maps.getKey().replace("2", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("3") > 0) {
                            threemap.put(maps.getKey().replace("3", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("4") > 0) {
                            fourmap.put(maps.getKey().replace("4", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("5") > 0) {
                            fivemap.put(maps.getKey().replace("5", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("6") > 0) {
                            sixmap.put(maps.getKey().replace("6", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("7") > 0) {
                            sevenmap.put(maps.getKey().replace("7", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("8") > 0) {
                            eightmap.put(maps.getKey().replace("8", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("9") > 0) {
                            ninetmap.put(maps.getKey().replace("9", "1"), maps.getValue());
                        }
                        if (maps.getKey().indexOf("10") > 0) {
                            tenmap.put(maps.getKey().replace("10", "1"), maps.getValue());
                        }
                    }
                    if (onemap.size() > 0) {
                        onemap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        onemap.put("CREATE_TIME", tim);
                        onemap.put("identification", raid);
                        onemap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(onemap);
                    }

                    if (twomap.size() > 0) {
                        twomap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        twomap.put("CREATE_TIME", tim);
                        twomap.put("identification", raid);
                        twomap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(twomap);
                    }
                    if (threemap.size() > 0) {
                        threemap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        threemap.put("CREATE_TIME", tim);
                        threemap.put("identification", raid);
                        threemap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(threemap);
                    }
                    if (fourmap.size() > 0) {
                        fourmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        fourmap.put("CREATE_TIME", tim);
                        fourmap.put("identification", raid);
                        fourmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(fourmap);
                    }
                    if (fivemap.size() > 0) {
                        fivemap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        fivemap.put("CREATE_TIME", tim);
                        fivemap.put("identification", raid);
                        fivemap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(fivemap);
                    }
                    if (sixmap.size() > 0) {
                        sixmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        sixmap.put("CREATE_TIME", tim);
                        sixmap.put("identification", raid);
                        sixmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(sixmap);
                    }
                    if (sevenmap.size() > 0) {
                        sevenmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        sevenmap.put("CREATE_TIME", tim);
                        sevenmap.put("identification", raid);
                        sevenmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(sevenmap);
                    }
                    if (eightmap.size() > 0) {
                        eightmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        eightmap.put("CREATE_TIME", tim);
                        eightmap.put("identification", raid);
                        eightmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(eightmap);
                    }
                    if (ninetmap.size() > 0) {
                        ninetmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        ninetmap.put("CREATE_TIME", tim);
                        ninetmap.put("identification", raid);
                        ninetmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(ninetmap);
                    }
                    if (tenmap.size() > 0) {
                        tenmap.put("MARKING_CODE", marking_cod);// MARKING_CODE加入入表map中
                        tenmap.put("CREATE_TIME", tim);
                        tenmap.put("identification", raid);
                        tenmap.put("id_card", Util.getUUID());
                        exEcution.insertExecution(tenmap);
                    }

                    if (type == 0) {
                        if (onemap.size() > 0) {
                            exEcution.insertExecutionS(onemap);
                        }

                        if (twomap.size() > 0) {
                            exEcution.insertExecutionS(twomap);
                        }
                        if (threemap.size() > 0) {
                            exEcution.insertExecutionS(threemap);
                        }
                        if (fourmap.size() > 0) {
                            exEcution.insertExecutionS(fourmap);
                        }
                        if (fivemap.size() > 0) {
                            exEcution.insertExecutionS(fivemap);
                        }
                        if (sixmap.size() > 0) {
                            exEcution.insertExecutionS(sixmap);
                        }
                        if (sevenmap.size() > 0) {
                            exEcution.insertExecutionS(sevenmap);
                        }
                        if (eightmap.size() > 0) {
                            exEcution.insertExecutionS(eightmap);
                        }
                        if (ninetmap.size() > 0) {
                            exEcution.insertExecutionS(ninetmap);
                        }
                        if (tenmap.size() > 0) {
                            exEcution.insertExecutionS(tenmap);
                        }
                    } else {
                        exEcution.deleteExecutionS(map_code);// 数据库入表实时表更新
                        if (onemap.size() > 0) {
                            exEcution.insertExecutionS(onemap);
                        }

                        if (twomap.size() > 0) {
                            exEcution.insertExecutionS(twomap);
                        }
                        if (threemap.size() > 0) {
                            exEcution.insertExecutionS(threemap);
                        }
                        if (fourmap.size() > 0) {
                            exEcution.insertExecutionS(fourmap);
                        }
                        if (fivemap.size() > 0) {
                            exEcution.insertExecutionS(fivemap);
                        }
                        if (sixmap.size() > 0) {
                            exEcution.insertExecutionS(sixmap);
                        }
                        if (sevenmap.size() > 0) {
                            exEcution.insertExecutionS(sevenmap);
                        }
                        if (eightmap.size() > 0) {
                            exEcution.insertExecutionS(eightmap);
                        }
                        if (ninetmap.size() > 0) {
                            exEcution.insertExecutionS(ninetmap);
                        }
                        if (tenmap.size() > 0) {
                            exEcution.insertExecutionS(tenmap);
                        }
                    }

                    // 更新流水收费标志
                    if (String.valueOf(map.get("FLAG_EXECUTION")).equals("1")) {
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
            List<Map<String, String>> listM = baseService.selectByStr("orm.mapper.zz.br.ExecutionMapper.selectByCode", marking_cod);
            if (null != listM && listM.size() > 0) {
                mapM = listM.get(0);
            }
            retvalue = JsonUtil.getJsonStr(mapM, jsonObjectTr, null);
            // -----------从数据库中获取数据进行json字符串拼接end------------
            transDtlService.updateTransDtl_N(raid, map.get("status"), type);// 流水表更新
        } catch (Exception e) {
            logger.error("法院被执行人—个人异常:" + e);
        }
        return retvalue + backTime;
    }