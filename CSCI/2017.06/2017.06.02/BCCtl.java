/**
     * 
     * @Title: getVehInsurance
     * @Description: 查询车辆续保信息
     * @author ly
     * @date 2017年6月2日 15:24:01
     * @param request
     * @return
     */
    @RequestMapping(value = "/VehInsurance", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getVehInsurance(HttpServletRequest request, String carNO, String cityCode, String userName
            , String certCode, String carCode, String carDriverNO, String merchantId) {
        String portrait_result = "";
        try {
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("carNO", new String(carNO.getBytes("iso-8859-1"), "utf-8"));
            paramData.put("cityCode", cityCode);
            paramData.put("userName", new String(StringUtil.isNull(userName).getBytes("iso-8859-1"), "utf-8"));
            paramData.put("certCode", StringUtil.isNull(certCode));
            paramData.put("carCode", StringUtil.isNull(carCode));
            paramData.put("carDriverNO", StringUtil.isNull(carDriverNO));
            // 将json格式的对象转换成字符串 faith_VehInsurance
            portrait_result = httpsUtil.postMsg(BCServiceUrl + "VehInsurance", "paramData=" + paramData.toString());

            logger.info("getVehInsurance_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getVehInsurance_ERROR 输出--->:" + e);
        }

        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }