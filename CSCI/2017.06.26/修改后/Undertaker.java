@RequestMapping(value = "/Undertaker", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUndertaker(HttpServletRequest request, String apiKey, String dataType, String queryName
            , String queryNo), String merchantId {
        String portrait_result = "";
        try {
            // 创建一个json格式的参数对象
            JSONObject paramData = new JSONObject();
            paramData.put("merchantId", merchantId);// 合作机构编号（作为客户标示来记录）
            paramData.put("apiKey", apiKey);
            paramData.put("dataType", StringUtil.isNull(dataType));
            paramData.put("queryName", new String(StringUtil.isNull(queryName).getBytes("iso-8859-1"), "utf-8"));
            paramData.put("queryNo", queryNo);
            // 将json格式的对象转换成字符串 faith_Undertaker
            portrait_result = httpsUtil.postMsg(BCServiceUrl + "Undertaker", "paramData=" + paramData.toString());

            logger.info("getUndertaker_parmData 输出--->:" + paramData);
        } catch (Exception e) {
            logger.error("getUndertaker_ERROR 输出--->:" + e);
        }

        if (!"ZZTEST0516".equals(request.getParameter("merchantId"))) {
            portrait_result = portrait_result.substring(0, portrait_result.lastIndexOf("}") + 1);
            return portrait_result;
        } else {
            return portrait_result;
        }
    }
