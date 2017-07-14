package com.zz.service.br;

import java.util.Map;

/**
 * 
 * @ClassName: BankFourProService
 * @Description: 银行卡四要素查询
 * @author ly
 * @date 2017年6月27日 下午12:08:29
 *
 */
public interface BankFourProService {

    public int insert(Map<String, String> parmes);

    public void insertS(Map<String, String> parmes);

    public void updateS(Map<String, String> parmes);
}
