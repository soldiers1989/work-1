package com.zz.service.br;

import java.util.Map;

/**
 * 
 * @ClassName: ConsumptioncService
 * @Description: 商品消费评估查询
 * @author ly
 * @date 2017年6月26日 上午10:53:17
 *
 */
public interface ConsumptioncService {

    public int intsertConsumptionc(Map<String, String> map);

    public void intsertConsumptioncS(Map<String, String> map);

    public void updateConsumptioncS(Map<String, String> map);
}
